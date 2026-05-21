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

public class Listing6_6_to_6_8 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.6 through 6.8
     * ───────────────────────────────────────────────────────
     * The convention we use throughout the book is to make
     * methods static whenever we intend for them to be deterministic
     * functions.
     *
     * Functions take everything they need as input and do absolutely
     * nothing else other than use those inputs to compute an output.
     */
    @Test
    void example() {
        class Example {
            //  ┌───────────────────────────────┐
            //  │   Now this is deterministic!  │
            //  └───────────────────────────────┘
            //
            //        ┌── Note that we've made it static!
            //        ▼
            private static LocalDate figureOutDueDate(
                    LocalDate today,     // ┐ Everything we need is
                    PaymentTerms terms   // ┘ passed in as an argument.
            ) {
                //┌─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┐
                //│  We've removed the old connection │
                //│  to the outside world.            │
                //└─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┘
                switch (terms) {
                    case PaymentTerms.NET_30:
                        return today.plusDays(30);
                    default:
                        // (other cases skipped for brevity)
                        return today;
                }
            }

            // Testing is now dead simple.
            // No mocking. No test doubles. No controlling for
            // the whims of stuff outside our function.
            public void testFigureOutDueDate() {
                //    ┌───── Inputs deterministically produce outputs.
                //    ▼
                var result = figureOutDueDate(
                   //  ┌──────────────────────┐
                        LocalDate.of(2026,1,1),
                        PaymentTerms.NET_30
                ); //  └──────────────────────┘
                   //  Everything we need to test is right here!
                   //  Outside world need not apply. We deal in
                   //  plain immutable data.
            }
        }
    }
}
