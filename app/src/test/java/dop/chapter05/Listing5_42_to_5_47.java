package dop.chapter05;

import com.google.common.base.Strings;
import dop.chapter05.the.existing.world.Entities.*;
import dop.chapter05.the.existing.world.Repositories;
import dop.chapter05.the.existing.world.Repositories.FeesRepo;
import dop.chapter05.the.existing.world.Services;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.Approval;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.ApprovalStatus;
import dop.chapter05.the.existing.world.Services.ContractsAPI;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.util.stream.Collectors.*;

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
public class Listing5_42_to_5_47 {




    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.42 through 5.47
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

        record Draft(){}
        record Latefee<A>(){}
        record PastDue(){}

        class Example {
            ContractsAPI contractsAPI;
            FeesRepo feesRepo;
            Latefee<Draft> buildDraft(Customer customer, List<PastDue> invoices){
                LocalDate today = LocalDate.now();          //  ─┐
                PaymentTerms terms = contractsAPI           //   │  Most of our method's implementation ends up
                        .getPaymentTerms(customer.getId()); //   │  devoted to managing "what's already there" rather
                BigDecimal feePercentage = feesRepo.get(    //   │  working on business logic.
                        customer.getAddress().getCountry()  //   │
                );                                          //  ─┘
                // plus anything else we need...

                // then only once all that is done can we
                // start to write any business logic
                return new Latefee<>(/*...*/);
            }
        }

        // ┌────────────────────────────────────────────────────────────────────┐
        // │         Separate how you get data from what you do with it         │
        // └────────────────────────────────────────────────────────────────────┘
        //
        //   Latefee<Draft> doAction(){    ─┐
        //      // [LOAD DATA]              │ Any method like this, that does all the
        //      // [BUSINESS LOGIC]         │ work of getting loading the data it needs
        //   }    ──────────────────────────┘
        //
        //                             ┌──────────  Can be refactored to one that ACCEPTS
        //                             ▼            that same data as an argument
        //   Latefee<Draft> doAction([DATA]){
        //      // [BUSINESS LOGIC]
        //   }       ▲
        //           └─── This frees the implementation to be devoted to business logic

        //
        // We can do this refactoring to buildDraft.
        class Example2 {
            Latefee<Draft> buildDraft(Customer customer,
                                      PaymentTerms terms,
                                      BigDecimal feePercentage,
                                      List<PastDue> invoices){
                // Now I can be pure business logic!
                return new Latefee<>(/*...*/);
            }
        }

        // This same refactoring can potentially be done to ALL of our methods!
        // ┌────────────────────────────────────────────────────────────────────┐
        // │                   Decouple, Decouple, Decouple!                    │
        // └────────────────────────────────────────────────────────────────────┘
        //
        //     public void processLatefees() {
        //         [LOAD ALL THE DATA WE NEED]
        //         CoutryCode country = customer.getBillingAddress().country();
        //         BigDecimal feePercentage = feeRepo.get(country);
        //         List<Invoice> allInvoices = invoiceRepo.findInvoices(customer.getId())
        //         // and so on ...
        //
        //
        //         [USE THE DATA IN BUSINESS LOGIC]
        //         List<Invoice> pastDue = collectPastDue(allInvoices, rating, today)
        //         // and so on...
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
            public void processLateFees(){
                this.streamInvoicingData().forEach(invoicingData -> {
                    // Just business logic here!
                });

            }

            // We can pull out all the gross management of these existing APIs, and
            // services, and entities into their own method. This frees up the rest
            // of our code to focus on what it cares about: the business logic.
            Stream<InvoicingData> streamInvoicingData() {
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
                    );
            }
        }
    }
}


