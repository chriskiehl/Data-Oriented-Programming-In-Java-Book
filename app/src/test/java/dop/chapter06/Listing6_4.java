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
public class Listing6_4 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.4
     * ───────────────────────────────────────────────────────
     * "Is this deterministic?" is an easy question we can ask
     * about any method we write.
     */
    @Test
    public void example() {


        class Example {
            ContractsAPI contractsAPI;

            //┌───────────────────────────────┐
            //│ Is this method deterministic? │
            //└───────────────────────────────┘
            private LocalDate figureOutDueDate() {
                // Nope.
                // We're coupled to the environment. We'll get a different answer
                // each time we run this.
                LocalDate today = LocalDate.now();
                // We're also dependent on the whims of some system totally
                // outside our own.                   ─┐
                //                                     ▼
                PaymentTerms terms = contractsAPI.getPaymentTerms("some customer ID");
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
