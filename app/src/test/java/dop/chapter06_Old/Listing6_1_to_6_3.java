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
public class Listing6_1_to_6_3 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.1 through 6.3
     * ───────────────────────────────────────────────────────
     * We kick off this chapter in a predicable place: semantics.
     * Step one is defining what we mean when we say "function."
     */
    @Test
    public void example() {
        class Example {
            public Integer plusOne(Integer x) {   // ◄───┐ Is this a function? A method? Both?
                return x + 1;
            }

            Function<Integer, Integer> plusSomething =  // ◄───┐ What about this one?
                (x) -> x + new Random().nextInt();      //     │
        }

        // Everything in Java is technically a method -- even those
        // things we call "anonymous functions"

        Stream.of(1,2,3).map((x) -> x + 1).toList();
        //                  └────────────┘
        //                        ▲
        //                        └─────────────────────────────────┐
        Stream.of(1,2,3).map(new Function<Integer, Integer>() {  // │ They de-sugar to classes with
            @Override                                            // │ methods behind the scenes.
            public Integer apply(Integer integer) {  // ◄───────────┘
                return integer + 1;
            }
        }).toList();


        class Example2 {
            // Semantics wise. We'd say this method is acting
            // as a function because it's **deterministic**
            public Integer plusOne(Integer x) {
                return x + 1;
            }
            // Whereas this one -- despite being called a Function
            // in Java -- does not meet our semantic meaning due to
            // its reliance on non-deterministic Randomness.
            Function<Integer, Integer> plusSomething =
                    (x) -> x + new Random().nextInt();
        }
    }
}
