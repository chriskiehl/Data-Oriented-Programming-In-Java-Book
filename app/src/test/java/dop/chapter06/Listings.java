package dop.chapter06;

import dop.chapter05.the.existing.world.Entities;
import dop.chapter05.the.existing.world.Services;
import dop.chapter05.the.existing.world.Services.ContractsAPI;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter06.the.implementation.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

/**
 * Chapter 6 walks through implementing the domain model
 * we came up with in chapter 5. However, that'd be pretty
 * boring on its own, so that chapter is *really* about
 * the choices we make while designing. It's about functions!
 * And determinism! And testability! And a whole host of other
 * things! It's a fun one.
 */
public class Listings {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.1 through 6.3
     * ───────────────────────────────────────────────────────
     * We kick off this chapter in a predicable place: semantics.
     * Step one is defining what we mean when we say "function."
     */
    @Test
    public void listing6_1_to_6_3() {
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


    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.4
     * ───────────────────────────────────────────────────────
     * "Is this deterministic?" is an easy question we can ask
     * about any method we write.
     */
    @Test
    public void listing6_4() {


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


    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.5
     * ───────────────────────────────────────────────────────
     * Non-deterministic code is more tedious to test.
     * We have to control for all the non-deterministic things.
     */
    @Test
    void listing6_5() {
        // We need all of this setup before we can test anything.
        ContractsAPI mockApi = mock(ContractsAPI.class);
        when(mockApi.getPaymentTerms(anyString())).thenReturn(PaymentTerms.NET_30);
        LocalDate date = LocalDate.of(2021, 1, 1);
        try (MockedStatic<LocalDate> dateMock = mockStatic(LocalDate.class)) {
            dateMock.when(LocalDate::now).thenReturn(date);
            // we can finally start constructing the objects
            // with these mocks down here.
        }
        // Maybe out here we'll finally get around to asserting something...?
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.6 through 6.8
     * ───────────────────────────────────────────────────────
     * Functions are easier to test! 
     */
    @Test
    void listing6_6() {
        class Example {
            //┌───────────────────────────────┐
            //│   Now this is deterministic!  │
            //└───────────────────────────────┘
            //
            //        ┌── Note that we've made it static!
            //        ▼
            private static LocalDate figureOutDueDate(
                    LocalDate today,     // ┐ Everything we need is
                    PaymentTerms terms   // ┘ passed in as an argument.
            ) {
                //┌─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┐
                //│  We've removed the connection │
                //│  to the outside world.        │
                //└─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┘
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
