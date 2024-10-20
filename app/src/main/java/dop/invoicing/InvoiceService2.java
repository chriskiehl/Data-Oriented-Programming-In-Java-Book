package dop.invoicing;

import dop.invoicing.BillingSystem.ImprovedResponse;
import dop.invoicing.DataTypes.*;
import dop.invoicing.DataTypes.BillingState.Pending;
import dop.invoicing.Entities.Config;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class InvoiceService2 {
    Repositories.InvoiceRepo invoiceRepo;
    Repositories.CustomerRepo customerRepo;

    Repositories.FeeRepo feeRepo;
    Repositories.RulesRepo rulesRepo;
    private BillingSystem billingSystem;

    public void processLateFees(LocalDate accountingCloseDate, Locale.IsoCountryCode country) {
        LocalDate validCloseDate = verifyDate(accountingCloseDate);
        // can run in transaction
        List<ReviewedLatefee> reviewedFees = this.collectInvoicingData(validCloseDate, country)
                .map(Core3::generateFee)
                .toList();
        invoiceRepo.save(reviewedFees);
        // done!
        reviewedFees.forEach(this::dispatchNextAction);
    }

    @Springish.Async
    public void dispatchNextAction(ReviewedLatefee decision) {
        switch (decision) {
            case ReviewedLatefee.Billable b -> {
                Latefee<? extends BillingState> finalizeLatefee = this.sendBillDownstream(b.latefee());
                invoiceRepo.save(finalizeLatefee);
            }
            case ReviewedLatefee.HeldForReview b ->
                this.requestReview(b.latefee());
            case ReviewedLatefee.NotBillable b ->
                System.out.println("Taking no action. Fee is not billable.");
        }
    }

    Latefee<? extends BillingState> sendBillDownstream(Latefee<Pending> latefee) {
        return switch (billingSystem.improvedSubmit(latefee)) {
            case ImprovedResponse.Accepted accepted -> latefee.markAsBilled(accepted.invoiceId());
            case ImprovedResponse.Rejected rejected -> latefee.markAsFailed(rejected.reason());
        };
    }

    // How cool is this? We can say in the type system that this method CANNOT
    // change the state of the latefee it's processing.
    <A extends BillingState> Latefee<A> requestReview(Latefee<A> latefee) {
        // TODO;
        return latefee;
        // return latefee.markAsBilled("hello");  will not work!
    }


    // I contain no notable logic of any kind. I exist solely to get stuff out of all the
    // various storage locations (that are forced upon me by the outside world) and adapt
    // them to the data taht I want. Once I leave this method, I'm in the land of pure data
    // and inputs and outputs.
    Stream<InvoicingData> collectInvoicingData(LocalDate asOf, Locale.IsoCountryCode country) {
        return customerRepo.findAll().stream()
                .map(( x -> (DataTypes.Customer) (Object) x))// TODO: FIX ME
                .map(customer -> {
                    Double howMuchToCharge = feeRepo.get(customer.billingAddress().country());
                    Config config = rulesRepo.loadDefaults();
                    List<Entities.Invoice> invoices = invoiceRepo.findInvoices(customer.id());
                    return new InvoicingData(
                            customer,
                            invoices,
                            asOf,
                            country,
                            new Percent(1.0, 1.0),
                            config
                    );
                });
    }

    LocalDate verifyDate(LocalDate date) {
        return date;
    }


}
