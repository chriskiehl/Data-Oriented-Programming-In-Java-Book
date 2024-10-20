package dop.invoicing;


import dop.invoicing.BillingSystem.BillStatus;
import dop.invoicing.BillingSystem.BillSubmitResponse;
import dop.invoicing.Entities.*;
import dop.invoicing.Repositories.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Springish.Service
public class InvoiceService {
    // injecting our dependencies like good citizens
    InvoiceRepo invoiceRepo;
    CustomerRepo customerRepo;
    FeeRepo feeRepo;
    RulesRepo rulesRepo;
    BillingSystem billingSystem;
    ApprovalsRepo approvalsRepo;



    public record BillRunRequest(
            String asOfDate,
            String countryCode
    ){}

    // So, what's wrong with this?
    // Nothing in isolation. (blah blah blah context)
    //  * This does everything at once
    //  * You have to read every single line to understand what the heck it does
    //  * Multiple / mixed failure modes which complicate reasoning about atomicity
    //  * To test this, you have to test **everything**.
    //  * The biggest crime, is that the semantics of the system are hidden away.
    //       * If you're the person that read the requirements, this'll mean something
    //         if you're just dropped into the codebase, there's a LOT to unpack.
    //
//    @Transaction
    public void processLatefees(BillRunRequest closeDate) {
        this.validateInput(closeDate);

        Config config = rulesRepo.loadDefaults();

        for (Customer customer : customerRepo.findAll()) {
            BigDecimal feePercentage = BigDecimal.valueOf(feeRepo.get(customer.billingAddress().country()));

            List<Invoice> pastDueInvoices = this.getPastDueInvoices(customer);
            USD totalPastDue = getTotalPastDue(pastDueInvoices);
            USD latefee = totalPastDue.multiply(feePercentage);

            Invoice latefeeInvoice = new Invoice();

            if (latefee.compareTo(config.minimumFeeThreshold()) <= 0) {
                // too low
                latefeeInvoice.setReason(CannotBillReasons.BelowMinimumThreshold);
                invoiceRepo.save(latefeeInvoice);
            } else {
                if (latefee.compareTo(config.maximumFeeThreshold()) > 0) {
                    if (customer.approvedForLargeFees() == null) {
                        this.requestReview(latefeeInvoice, customer);
                        latefeeInvoice.setReason(CannotBillReasons.AboveMaximumThreshold);
                    } else if (!customer.approvedForLargeFees()) {
                        latefeeInvoice.setReason(CannotBillReasons.AboveApprovedThreshold);
                    }
                    invoiceRepo.save(latefeeInvoice);
                    continue;
                }
                // Implicitly, if we made it here, then we're allowed to
                // charge the customer even if the fee is high.
                latefeeInvoice.setCharges(latefee);
                latefeeInvoice.setIncludedInFee(pastDueInvoices);
                invoiceRepo.save(latefeeInvoice); // save it no matter what
                this.sendBillDownstream(latefeeInvoice);
            }
        }
    }

    public void processLatefees2(BillRunRequest rawCloseDate) {

    }


    @Springish.Async
    public void sendBillDownstream(Invoice invoice) {
        // huh.. uh.. how do we check that we _should_ bill this?
        // What happens if we accidentally enqueue a regular invoice
        // or see the same message twice...?
        if (invoice.isLatefee()
                // and.. open? No that doesn't make sense.. we're missing something
                && invoice.getStatus().equals(InvoiceStatus.OPEN)) { // TODO: draft status?
            // oh -- wait -- it's not ANY latefee.
            // we have to re-check that this isn't one of the latefees
            // that we generate for our records but don't bill
            assert invoice.isCanCharge();
            // ok NOW we should be good.
            BillSubmitResponse response = billingSystem.submit(invoice);
            if (response.status().equals(BillStatus.ACCEPTED)) {
                if (response.invoiceId().isEmpty()) {
                    throw new RuntimeException("Invoice ID unexpectedly missing!");
                } else {
                    invoice.setInvoiceId(response.invoiceId().get());
                    invoice.setStatus(InvoiceStatus.OPEN);
                    invoiceRepo.save(invoice);
                }
            } else {
                throw new RuntimeException("rejected!");
            }
        } else {
            throw new IllegalStateException("I can't charge this!");
        }

    }

    public List<Invoice> getPastDueInvoices(Customer customer) {
        return invoiceRepo.findInvoices(customer.id())
                .stream()
                .filter(invoice -> {
                    if (customer.standing().equals(CustomerStanding.Good)) {
                        return invoice.getDueDate().plusDays(60).isBefore(LocalDate.now());
                    } else if (customer.standing().equals(CustomerStanding.Acceptable)) {
                        return invoice.getDueDate().plusDays(30).isBefore(LocalDate.now());
                    } else {
                        return invoice.getDueDate().isBefore(LocalDate.now());
                    }
                })
                .toList();
    }

    @Springish.Async
    public void requestReview(Invoice invoice, Customer customer) {
        // TODO: [note to self]
        // [potential example for "smart clients" and viewing it as ]
        // [being the job of the outside world to adapt to OUR data ]
        // [not the other way around                                ]
        // lookup where it needs to go

        // Where do we get the emails? Another service! Ugh!
        //
        // email service.send()
    }

    private void validateInput(BillRunRequest command) throws RuntimeException {
        LocalDate.parse(command.asOfDate());
        Locale.IsoCountryCode.valueOf(command.countryCode());
    }

    public USD getTotalPastDue(List<Invoice> invoices) {
        return invoices.stream().filter(invoice -> invoice.getDueDate().isBefore(LocalDate.now()))
                .map(Invoice::getCharges)
                .reduce(USD.zero(), USD::add);

    }

}
