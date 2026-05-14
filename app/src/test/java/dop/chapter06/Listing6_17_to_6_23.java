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
public class Listing6_17_to_6_23 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.17 - 2.23
     * ───────────────────────────────────────────────────────
     * Determinism makes the line between where functions end
     * and data begins blurry.
     *
     * We can view functions AS themselves data. In fact, doing
     * so can make a lot of awkward modeling problems become clear.
     *
     * This section works from these requirements:
     * ---------------------------------------------------------
     * The customer shall have a Grace Period
     *   Customers in good standing receive a 60-day grace period
     * 	 Customers in acceptable standing receive a 30-day grace period
     * 	 Customers in poor standing must pay by end of month
     */
    @Test
    void example() {
//        [Note] (code is commented out since it relies on things that won't compile.)
//
//                ┌───── If we wanted to implement grace period
//                │      as a deterministic function. What would it return?
//                ▼
//        static ??? gracePeriod(CustomerRating rating) { #A
//               ???
//        }
//
//        We could try throwing a lot of "types" at it. Maybe we introduce
//        a Days data type?
//
//        Days gracePeriod(CustomerRating rating) {
//            return switch(rating) {
//                case CustomerRating.GOOD -> new Days(60);
//                case CustomerRating.ACCEPTABLE -> new Days(30);
//                case CustomerRating.POOR -> ???
//            }                                ▲
//        }                                    └── But we get stuck here because it
//                                                 depends on something other than rating
//
        class __ {
            //        ┌───── The requirement is expressing a *relationship* between
            //        │      two dates. The business rule is itself a function.
            //        │
            //        ▼
            static Function<LocalDate, LocalDate> gracePeriod(CustomerRating rating) {
                return switch(rating) {
                    case CustomerRating.GOOD -> date -> date.plusDays(60);
                    case CustomerRating.ACCEPTABLE -> date -> date.plusDays(30);
                    case CustomerRating.POOR -> date -> date.with(lastDayOfMonth());
                };
            }

            //        ┌───── We don't have to define our own function.
            //        │      Java has one built in.
            //        │
            //        ▼
            static TemporalAdjuster gracePeriodV2(CustomerRating rating) {
                return switch(rating) {
                    case CustomerRating.GOOD -> date -> date.plus(60, DAYS);
                    case CustomerRating.ACCEPTABLE -> date -> date.plus(30, DAYS);
                    case CustomerRating.POOR -> lastDayOfMonth();
                };
            }

            // The reward for this modeling is code that reads exactly like the
            // requirements.
            static boolean isPastDue(
                    LocalDate evaluationDate, Invoice invoice, CustomerRating rating) {
                return evaluationDate.isAfter(invoice.getDueDate().with(gracePeriodV2(rating)));

            }

            // BUT!

            // This is not to say there's one "right" way of modeling this requirement.
            // Equally fine would be something like this.
            // Instead of returning a function that produces data *later*, we pass in
            // more data so that it can compute the result *now*.
            static LocalDate mustHavePaidBy(Invoice invoice, CustomerRating rating) {
                return switch(rating) {
                    case CustomerRating.GOOD -> invoice.getDueDate().plusDays(60);
                    case CustomerRating.ACCEPTABLE -> invoice.getDueDate().plusDays(30);
                    case CustomerRating.POOR -> invoice.getDueDate().with(lastDayOfMonth());
                };
            }

            // These are all fine approaches!
        }
    }
}
