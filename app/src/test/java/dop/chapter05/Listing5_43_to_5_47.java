package dop.chapter05;

import dop.chapter05.Listing5_43_to_5_47.Lifecycle.Draft;
import dop.chapter05.the.existing.world.Entities.*;
import dop.chapter05.the.existing.world.Repositories;
import dop.chapter05.the.existing.world.Repositories.FeesRepo;
import dop.chapter05.the.existing.world.Services;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.Approval;
import dop.chapter05.the.existing.world.Services.ContractsAPI;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static dop.chapter05.Listing5_43_to_5_47.ReviewedFee.*;

public class Listing5_43_to_5_47 {




    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.43 through 5.48
     * ───────────────────────────────────────────────────────
     * There are few black and white answers in software engineering.
     * We deal with tradeoffs and concessions.
     *
     * In this listing, we look at a possible implementation for
     * what buildDraft might look like if we just "plug in" whatever
     * we need from the outside world.
     *
     * Sometimes this is fine; sometimes it's not. The problem from a
     * modeling perspective is that it drags "our" method back into
     * being one that spends all of its time managing "other" stuff.
     */
    @Test
    public void example() {

        class Example {
            ContractsAPI contractsAPI;
            FeesRepo feesRepo;
            /**
             * ───────────────────────────────────────────────────────
             * Listing 5.43
             * ───────────────────────────────────────────────────────
             * One possible implementation for buildDraft
             * ───────────────────────────────────────────────────────
             */
            LateFee<Draft> buildDraft(Customer customer, List<PastDue> invoices){
                LocalDate today = LocalDate.now();          //  ─┐
                PaymentTerms terms = contractsAPI           //   │  Most of our method's implementation ends up
                        .getPaymentTerms(customer.getId()); //   │  devoted to managing "what's already there" rather
                BigDecimal feePercentage = feesRepo.get(    //   │  working on business logic.
                        customer.getAddress().getCountry()  //   │
                );                                          //  ─┘
                // plus anything else we need /*...*/

                // then only once all that is done can we
                // start to write any business logic
                // return new LateFee<>(/*...*/);


                return null; // (so the example compiles)
            }
        }


        /**
         * ───────────────────────────────────────────────────────
         * Listing 5.44
         * ───────────────────────────────────────────────────────
         * Refactoring to separate how we get data from how we use it
         * ───────────────────────────────────────────────────────
         */
        // ┌────────────────────────────────────────────────────────────────────┐
        // │         Separate how you get data from what you do with it         │
        // └────────────────────────────────────────────────────────────────────┘
        //
        //   LateFee<Draft> doAction(){    ─┐
        //      // [LOAD DATA]              │ Any method like this, that does all the
        //      // [BUSINESS LOGIC]         │ work of getting loading the data it needs
        //   }    ──────────────────────────┘
        //
        //                             ┌──────────  Can be refactored to one that ACCEPTS
        //                             ▼            that same data as an argument
        //   LateFee<Draft> doAction([DATA]){
        //      // [BUSINESS LOGIC]
        //   }       ▲
        //           └─── This frees the implementation to be devoted to business logic

        //
        // We can do this refactoring to buildDraft.
        class Example2 {
            LateFee<Draft> buildDraft(Customer customer,
                                      PaymentTerms terms,
                                      BigDecimal feePercentage,
                                      List<PastDue> invoices){
                // Now I can be pure business logic!
                // return new LateFee<>(/*... */);


                return null; // (so the example compiles)
            }
        }


        /**
         * ───────────────────────────────────────────────────────
         * Listing 5.45
         * ───────────────────────────────────────────────────────
         * Two buckets of different functionality
         * ───────────────────────────────────────────────────────
         */
        // This same refactoring can potentially be done to ALL of our methods!
        // ┌────────────────────────────────────────────────────────────────────┐
        // │                   Decouple, Decouple, Decouple!                    │
        // └────────────────────────────────────────────────────────────────────┘
        //
        //     public void processLateFees() {
        //         [LOAD ALL THE DATA WE NEED]
        //         CoutryCode country = customer.getBillingAddress().country();
        //         BigDecimal feePercentage = feeRepo.get(country);
        //         List<Invoice> allInvoices = invoiceRepo.findInvoices(customer.getId())
        //         // and so on  /*...*/
        //
        //
        //         [USE THE DATA IN BUSINESS LOGIC]
        //         List<Invoice> pastDue = collectPastDue(allInvoices, rating, today)
        //         // and so on /*...*/
        //     }

        // We can pull the "how we get the data" into its own method.
        // And give it its own data type!
        // This gives us opportunities to tighten up the data model.
        // For instance, rather than percentage being represented by a
        // vague BigDecimal (which is VERY easy to misunderstand), we can
        // make it IMPOSSIBLE to use incorrectly by leveraging a more semantic
        // data type -- here's the one we made in Chapter 03
        record Percent(double numerator, double denominator) {
            Percent {
                if (numerator > denominator) {
                    throw new IllegalArgumentException(
                        "Percentages are 0..1 and must be expressed " +
                        "as a proper fraction. e.g. 1/100");
                }
            }
        }
        record InvoicingData(
            Customer customer,
            List<Invoice> invoices,
            LocalDate currentDate,
            CustomerRating customerRating,
            PaymentTerms terms,
            Percent feePercent,
            Rules rules,
            Optional<Approval> approval
        ){}


        // ┌────────────────────────────────────────────────────────────────────┐
        // │              Using this refactoring in our main method             │
        // └────────────────────────────────────────────────────────────────────┘
        class FeeService {
            private Services.RatingsAPI ratingsApi;
            private ContractsAPI contractsApi;
            private ApprovalsAPI approvalsApi;
            private Services.BillingAPI billingApi;
            private Repositories.CustomerRepo customerRepo;
            private Repositories.InvoiceRepo invoiceRepo;
            private Repositories.RulesRepo rulesRepo;
            private FeesRepo feesRepo;

            /**
             * ───────────────────────────────────────────────────────
             * Listing 5.46
             * ───────────────────────────────────────────────────────
             * Isolating all the services calls and loading into its own method
             * ───────────────────────────────────────────────────────
             */
            // We can pull out all the gross management of these existing APIs, and
            // services, and entities into their own method. This frees up the rest
            // of our code to focus on what it cares about: the business logic.
            List<InvoicingData> loadInvoicingData() {
                // If this looks like a lot, that's because it IS!
                // When you aggregate it all together, it reveals how much time we wasted
                // in our original implementation just managing "how" we get what we need.
                // This feels different when pulled out because it's no longer amortized
                // across all the other business logic. It's true cost is laid bare.
                LocalDate today = LocalDate.now();
                return customerRepo.findAll()
                    .stream()
                    .map(customer -> {
                        BigDecimal uglyFee = feesRepo.get(customer.getAddress().getCountry());
                        Percent feePercent = new Percent(uglyFee.doubleValue(), 1);
                        return new InvoicingData(
                            customer,
                            invoiceRepo.findInvoices(customer.getId()),
                            today,
                            ratingsApi.getRating(customer.getId()),
                            contractsApi.getPaymentTerms(customer.getId()),
                            feePercent,
                            rulesRepo.loadDefaults(),
                            Optional.ofNullable(customer.getApprovalId())
                                    .flatMap(approvalsApi::getApproval));
                        }
                    ).toList();
            }


            /**
             * ───────────────────────────────────────────────────────
             * Listing 5.47
             * ───────────────────────────────────────────────────────
             * An example implementation using the new data grabber
             * ───────────────────────────────────────────────────────
             */
            public void processLateFees(){
                this.loadInvoicingData().forEach(invoicingData -> {
                    for (InvoicingData data: this.loadInvoicingData()) {
                        List<Invoice> pastDue = collectPastDue( /*...*/);
                        LateFee<Draft> draft = buildDraft( /*...*/);
                        ReviewedFee decision = assessDraft( /*...*/);
                    switch(decision) {
                        case Billable billable       ->  someAction();
                        case NeedsApproval na          ->  someAction();
                        case NotBillable notBillable ->  someAction();
                    }
                    }
                });
            }









            // here for compilation
            List<Invoice> collectPastDue() {
                return null; 
            }
            LateFee<Draft> buildDraft() {
                return null;
            }
            ReviewedFee assessDraft() {
                return null;
            }

            void someAction() {

            }
        }
    }



    public record PastDue(Invoice value) {}

    public sealed interface Lifecycle {
        record Draft() implements Lifecycle {}
        record Billed(String invoiceId) implements Lifecycle {}
        record Rejected(Reason reason) implements Lifecycle {}
        record InReview(ApprovalId approvalId) implements Lifecycle {}
    }

    public record LateFee<State extends Lifecycle>(
            String customerId,
            USD total,
            State state,
            LocalDate invoiceDate,
            LocalDate dueDate,
            List<Invoice> includedInFee
    ){}

    public sealed interface ReviewedFee {
        record Billable(LateFee<Draft> LateFee)
                implements ReviewedFee {}
        record NeedsApproval(LateFee<Draft> LateFee)
                implements ReviewedFee {}
        record NotBillable(LateFee<Draft> LateFee, Reason reason)
                implements ReviewedFee {}
    }


    interface TheMethodsSignatures {
        List<PastDue> collectPastDue(List<Invoice> invoices);
        LateFee<Draft> buildDraft(List<PastDue> invoices);
        ReviewedFee assesDraft(LateFee<Draft> invoice);
        LateFee<? extends Lifecycle> submitBill(Billable draft);
        LateFee<Lifecycle.InReview> startApproval(NeedsApproval needsApproval);
    }


    record USD(BigDecimal value) {}
    record Reason(String value) {}
    record ApprovalId(String value) {}
}


