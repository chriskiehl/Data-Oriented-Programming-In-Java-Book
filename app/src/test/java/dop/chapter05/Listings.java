package dop.chapter05;

import dop.chapter05.the.existing.world.Entities.*;
import dop.chapter05.the.existing.world.Repositories;
import dop.chapter05.the.existing.world.Services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

/**
 * Chapter 5 takes all the modeling tools we've explored
 * so far and applies them to building a complex feature.
 * No more simple domains. No more isolated modeling. We
 * dive into the messy world of building software. That
 * means everything that makes it hard: databases, ORMS,
 * third party services (with APIs we don't control), and
 * the absolute worst thing of all: prior decisions.
 *
 * We'll learn how to work with all of these limitations
 * and produce clean, clear, data-oriented code.
 */
public class Listings {


    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.1
     * ───────────────────────────────────────────────────────
     * We're implementing the feature in an application that
     * already "exists." So, the first thing we do in the chapter
     * is set the stage.
     *
     * This pretend app is "modern" and "service oriented." These
     * are the external APIs we'll interact with.
     * We cheat a bit and ignore stuff like HTTP and failures.
     */
    public void listing5_1() {
        interface RatingsAPI {
            enum CustomerStanding {GOOD, ACCEPTABLE, POOR}
            CustomerStanding getRating(String customerId);
        }

        interface ContractsAPI {
            enum PaymentTerms {NET_30, NET_60, END_OF_MONTH, DUE_ON_RECEIPT}
            PaymentTerms getPaymentTerms(String customerId);
        }

        interface ApprovalsAPI {
            enum Status {Pending, Approved, Denied}
            record Approval(String id, Status status){}
            record CreateApprovalRequest(/*...*/) {}
            Approval createApproval(CreateApprovalRequest request);
            Optional<Approval> getApproval(String approvalId);
        }

        interface billingAPI {
            enum Status {ACCEPTED, REJECTED}
            record SubmitInvoiceRequest(/*...*/) {}
            record BillingResponse(
                    Status status,
                    String invoiceId,
                    String error
            ){}
            BillingResponse submit(SubmitInvoiceRequest request);
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.2
     * ───────────────────────────────────────────────────────
     * The bedrock set of identity objects on which our application
     * is built. These are playing the role of "what's already there"
     * in our pretend application.
     *
     * Getters, setters, etc are omitted for brevity. (Assume they're
     * auto-generated by the (made up) annotation processor)
     */
    public void listing5_2() {
        // @See dop.chapter05.the.existing.world.Entities
        // These Entities are the same throughout all examples.
        // Rather than re-define them over and over, they're defined
        // once as a set of static classes.
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.3 through 5.4
     * ───────────────────────────────────────────────────────
     * The starting point for our feature, and the general shape
     * in which so much of our programming work falls into.
     */
    public void listing5_3() {
        // These services are all defined in listing 5.1
        class LateFeeChargingService {               // ◄───────┐ The bet I make in the book is that this general
            private Services.RatingsAPI ratingsApi;         //  │ setup feels deeply familiar as a starting point.
            private Services.ContractsAPI contractsApi;     //  │ We have an army of Entities, Service Classes, and
            private Services.ApprovalsAPI approvalsApit;    //  │ Repositories (or "Data Access Objects" depending on
            private Services.BillingAPI billingApi;         //  │ your preferred lingo) all crammed into the top of a
            private Repositories.CustomerRepo customerRepo; //  │ a class that's usually called [Thing]Service.
            private Repositories.InvoiceRepo invoiceRepo;
            private Repositories.RulesRepo rulesRepo;
            private Repositories.FeesRepo feesRepo;

            // It's here that we'll assemble all of this
            // pre-existing *stuff* into something that satisfies
            // our requirements.
            //
            // The below will surely look different that how you
            // might implement the same set of requirements, but
            // I hope that it feels familiar -- like something you
            // could have written (I've written *lots* of code like
            // this). It's not terrible, but it's also not good.
            //
            // Let's walk through it.
            public void processLatefees() {
                LocalDate today = LocalDate.now();

                Rules rules = rulesRepo.loadDefaults();
                for (Customer customer : customerRepo.findAll()) {               
                    BigDecimal feePercentage = feesRepo.get(
                            customer.getAddress().getCountry()
                    );                                                             
                    List<Invoice> pastDueInvoices = getPastDueInvoices(customer);
                    BigDecimal totalPastDue = getTotalPastDue(pastDueInvoices);
                    BigDecimal latefee = totalPastDue.multiply(feePercentage);

                    Invoice latefeeInvoice = new Invoice();
                    latefeeInvoice.setInvoiceType(InvoiceType.LATEFEE);
                    latefeeInvoice.setCustomerId(customer.getId());
                    latefeeInvoice.setInvoiceDate(today);
                    latefeeInvoice.setDueDate(this.figureOutDueDate());
                    latefeeInvoice.setLineItems(List.of(
                        new LineItem(
                                null,
                                "Late Fee Charge",
                                latefee,
                                Currency.getInstance("USD")
                        ))
                    );
                    latefeeInvoice.setAuditInfo(new AuditInfo(null, pastDueInvoices, null));
                    if (latefee.compareTo(rules.getMinimumFeeThreshold()) <= 0) {
                        latefeeInvoice.getAuditInfo().setReason("too low to charge!");
                        invoiceRepo.save(latefeeInvoice);
                    } else {
                        if (latefee.compareTo(rules.maximumFeeThreshold()) > 0) {
                            if (customer.getApprovalId().isEmpty()) {
                                this.requestReview(latefeeInvoice, customer);
                                latefeeInvoice.setReason("Above default threshold");
                            } else {
                                ApprovalStatus status = getApprovalStatus(customer);
                                if (status.equals(ApprovalStatus.Pending)) {
                                    latefeeInvoice.setReason("Pending decision");
                                } else if (status.equals(ApprovalStatus.Rejected)) {
                                    latefeeInvoice.setReason("Exempt from large fees");
                                }
                            }
                            invoiceRepo.save(latefeeInvoice);
                            continue;
                        }

                        invoiceRepo.save(latefeeInvoice);
                        this.submitBill(latefeeInvoice);
                    }
                }
            }

            List<Invoice> getPastDueInvoices(Customer customer) {
                return invoiceRepo.findInvoices(customer.id())
                        .stream()
                        .filter(invoice -> {
                            if (customer.standing().equals(GOOD)) {
                                return invoice.getDueDate()
                                        .plusDays(60).isBefore(LocalDate.now());
                            }
                            else if (customer.standing().equals(ACCEPTABLE)) {
                                return invoice.getDueDate()
                                        .plusDays(30).isBefore(LocalDate.now());
                            } else {
                                return invoice.getDueDate()
                                        .with(lastDayOfMonth().isBefore(LocalDate.now());
                            }
                        })
                        .toList();
            }

            BigDecimal getTotalPastDue(List<Invoice> invoices) {
                // not defined in the book. We assume it does what it
                // says on the tin.
                return BigDecimal.ZERO; // (just to make things compile)
            }

            LocalDate figureOutDueDate() {
                // Ditto the above.
                // We gloss over it to focus on other things.
                return LocalDate.now(); // (just to make things compile)
            }
    }




}
