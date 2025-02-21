package dop.chapter06;

import dop.chapter05.the.existing.world.Entities;
import dop.chapter05.the.existing.world.Entities.Invoice;
import dop.chapter05.the.existing.world.Services;
import dop.chapter05.the.existing.world.Services.ContractsAPI;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;
import dop.chapter06.the.implementation.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

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
     * The convention we use throughout the book is to make
     * methods static whenever we intend for them to be deterministic
     * functions.
     *
     * Functions take everything they need as input and do absolutely
     * nothing else other than use those inputs to compute an output.
     */
    @Test
    void listing6_6_to_6_8() {
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
    void listing6_9_to_6_10() {
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


    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.11 through 6.14
     * ───────────────────────────────────────────────────────
     * An interesting question: how many possible implementations
     * could we write for a given type signature?
     *
     * This will feel weird if you've never thought about it
     * before. Learning to think about what our code really
     * says -- or, more importantly, **enables** -- is a valuable
     * design skill.
     */
    @Test
    void listing6_11_to_6_14() {

        enum People {Bob, Mary}
        enum Jobs {Chef, Engineer}

        class SomeClass {
            // [Hidden]  // ◄──── Assume all kinds of instance state here

            // How many different implementations could we write
            // inside of this method?
            People someMethod(Jobs job) {
                // this.setCombobulator("large");
                // universe.runSimulation(job);
                // collapseSingularity(this)
                return null; // ◄── This null isn't in the book. It's only
            //                      here so the code will compile. Pretend
            //      ▲               the entire implementation is hidden.
            //      │
            //      │
            }//     The answer is infinite.
            //      Methods are allowed to do anything.
        }

        class __ {
            //            ┌───── But what if we make that same method static?
//            ▼
            static People someMethod(Jobs job) {
                // [hidden]
                return null; // (again, ignore this null.)
            }
            // This won't seem important at first, but its implications
            // are far-reaching.
            // There is now a finite number of ways this method
            // could be implemented. And it's entirely determined
            // by the types we choose.
            //
            // We can enumerate them all!
            static People optionOne(Jobs job) {
                return switch (job) {
                    case Chef -> People.Bob;
                    case Engineer -> People.Bob;
                };
            }

            static People optionTwo(Jobs job) {
                return switch (job) {
                    case Chef -> People.Mary;
                    case Engineer -> People.Mary;
                };
            }

            static People optionThree(Jobs job) {
                return switch (job) {
                    case Chef -> People.Bob;
                    case Engineer -> People.Mary;
                };
            }

            static People optionFour(Jobs job) {
                return switch (job) {
                    case Chef -> People.Mary;
                    case Engineer -> People.Bob;
                };
            }

            // For some fun background that's not in the book
            // The number of possible implementations, or, in
            // math speak, the number of ways of mapping from one
            // set to another, is computed by
            //     |InputType|^|OutputType|
            // i.e. the cardinality of the set of values in the input
            // type raised to the cardinality of the output type.
            //
            // You can see this in the example above. 2^2 = 4 possible
            // implementations this function can have.
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.15
     * ───────────────────────────────────────────────────────
     * Deterministic functions join together to form...
     *
     * ...
     *
     * another deterministic function!
     */
    @Test
    void listing6_15() {
        Function<Integer, Integer> square = (x) -> x * x;
        Function<Integer, Integer> inc = (x) -> x + 1;

        // Functions compose together!
        Function<Integer, Integer> incAndSquare =
                inc.andThen(square);
        // This example is different from the book because the
        // book cheats in order to show the composition in the
        // context of functions we've defined for our domain.
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.16
     * ───────────────────────────────────────────────────────
     * Functions are just tables of data in disguise.
     */
    @Test
    void listing6_16() {
        class __{
            // determinism means that every output for this function
            // is already pre-defined.
            static int increment(int x) {
                return x + 1;
            }

            // We could even compute all of its answers ahead of time.
            static Map<Integer, Integer> ANSWERS = new HashMap<>();
            // [NOTE] This different from the book in that we don't actually
            //        crawl over every integer (which would be very slow and
            //        consume a lot of memory).
            static Integer PRETEND_MIN_VALUE = -10;
            static Integer PRETEND_MAX_VALUE = 10;
            static {
                for (int i = PRETEND_MIN_VALUE; i < PRETEND_MAX_VALUE-1; i++) {
                    ANSWERS.put(i, increment(i));
                }
            }
            // We end up with a lookup table that maps inputs to outputs.
            // |  Input   |    Output   |
            // |    1     |      2      |
            // |    2     |      3      |
            // etc..
        }
    }

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
    void listing6_17_to_6_23() {
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

            // BUT
            // This is not to say there's one "right" way of modeling this requirements.
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
