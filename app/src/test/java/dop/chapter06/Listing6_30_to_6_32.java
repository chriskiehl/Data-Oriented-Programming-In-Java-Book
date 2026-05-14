package dop.chapter06;

import dop.chapter05.the.existing.world.Entities;
import dop.chapter05.the.existing.world.Entities.Invoice;
import dop.chapter05.the.existing.world.Entities.LineItem;
import dop.chapter05.the.existing.world.Services;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.ApprovalStatus;
import dop.chapter05.the.existing.world.Services.ContractsAPI;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;
import dop.chapter06.the.implementation.Core;
import dop.chapter06.the.implementation.Service;
import dop.chapter06.the.implementation.Types;
import dop.chapter06.the.implementation.Types.*;
import dop.chapter06.the.implementation.Types.Lifecycle.Draft;
import dop.chapter06.the.implementation.Types.ReviewedFee.Billable;
import dop.chapter06.the.implementation.Types.ReviewedFee.NotBillable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter05.the.existing.world.Entities.InvoiceStatus.OPEN;
import static dop.chapter05.the.existing.world.Entities.InvoiceType.STANDARD;
import static dop.chapter05.the.existing.world.Services.ApprovalsAPI.ApprovalStatus.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.mockito.Mockito.*;

/**
 * Chapter 6 walks through implementing the domain model
 * we came up with in chapter 5. However, that'd be pretty
 * boring on its own, so that chapter is *really* about
 * the choices we make while designing. It's about functions!
 * And determinism! And testability! And a whole host of other
 * things! It's a fun one.
 */
public class Listing6_30_to_6_32 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.30 through 6.32
     * ───────────────────────────────────────────────────────
     * "Just use maps"?
     */
    @Test
    void example() {
        class V1 {
            // A very popular recommendation for data-oriented programming
            // is the idea that you should "just use maps".
            //
            // When presented with an implementation like this:
            static TemporalAdjuster gracePeriod(CustomerRating rating) {
                return switch(rating) {
                    case CustomerRating.GOOD -> date -> date.plus(60, DAYS);
                    case CustomerRating.ACCEPTABLE -> date -> date.plus(30, DAYS);
                    case CustomerRating.POOR -> lastDayOfMonth();
                };
            }
            // A natural question, given what we know about determinism, would
            // be why we need the function at all. Why not express this as _data_?
            static Map<CustomerRating, TemporalAdjuster> gracePeriodV2 = Map.of(
                CustomerRating.GOOD, date -> date.plus(60, DAYS),
                CustomerRating.ACCEPTABLE, date -> date.plus(30, DAYS),
                CustomerRating.POOR, TemporalAdjusters.lastDayOfMonth()
            );

            // We could refactor like this:
            static boolean isPastDueV2(Invoice invoice, CustomerRating rating, LocalDate today) {
                return invoice.getInvoiceType().equals(STANDARD)
                        && invoice.getStatus().equals(OPEN)
                        && today.isAfter(invoice.getDueDate().with(gracePeriodV2.get(rating)));
                //                                                └───────────────────────────┘
                //                                                              ▲
                //               Replaces a function call with a map lookup! ───┘
            }

            // But This is a dangerous refactor.
            // What algebraic types and pattern matching give is *exhaustiveness* at
            // compile time. The compiler knows if you've checked every case and will
            // tell you if you didn't.
            //
            // You "know" the map has everything in it *today*, but there's no way to
            // guarantee it tomorrow. Semvar is a lie. "Minor" version bumps break software
            // all the time.
            //
            // Since you can't be sure, and the compiler can't help, you have to defend.
            static boolean isPastDueV3(Invoice invoice, CustomerRating rating, LocalDate today) {
                TemporalAdjuster WHAT_GOES_HERE = TemporalAdjusters.firstDayOfMonth();
                return invoice.getInvoiceType().equals(STANDARD)
                        && invoice.getStatus().equals(OPEN)
                        && today.isAfter(invoice.getDueDate()
                            .with(gracePeriodV2.getOrDefault(rating, WHAT_GOES_HERE)));
                //                             └───────────┘         └───────────┘
                //                                  ▲                      ▲
                //       We're forced to do this ───┘                      │
                //                                                         │
                //          But notice that we're inventing solutions to ──┘
                //          problems that ONLY exist because of our modeling
                //          choices. The domain doesn't define a "default"
                //          grace period.
            }
            // Good modeling should eliminate illegal states not introduce them!
        }
    }
}
