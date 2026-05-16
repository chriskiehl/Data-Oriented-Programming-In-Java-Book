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

public class Listing6_9_to_6_10 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.9 through 6.10
     * ───────────────────────────────────────────────────────
     * The static convention one is useful in codebases because
     * it adds a bit of friction. Instance methods can act as
     * pure deterministic functions, but it takes discipline to
     * keep them that way (easier said than done on large teams)
     *
     * static tilts the scales in our favor towards functions
     * remaining functions over the long haul.
     */
    @Test
    void example() {
        class Example {
            ContractsAPI contractsAPI;
            //        ┌── static acts as a protective barrier
            //        ▼
            private static LocalDate figureOutDueDate(LocalDate today, PaymentTerms terms) {
                // contractsAPI  ◄──── Even if we wanted to use that contractsAPI
                //                     on the instance we can't. The static keeps
                //                     us isolated (at least enough to make doing
                //                     the 'wrong' thing annoying).

                switch (terms) {
                    case PaymentTerms.NET_30:
                        return today.plusDays(30);
                    default:
                        // (other cases skipped for brevity)
                        return today;
                }
            }
        }
    }
}
