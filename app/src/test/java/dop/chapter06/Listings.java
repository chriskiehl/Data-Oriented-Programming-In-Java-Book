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


    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.24
     * ───────────────────────────────────────────────────────
     * Don't drive yourself crazy over purity and referential
     * transparency. Close enough is good enough.
     */
    @Test
    void listing6_24() {
        class __ {
            //
            record PastDue(Invoice invoice) {}
            //               ▲
            //               └── We depend on a mutable identity object. We can never
            //                   truly be referential transparent because the "same" object
            //                   could lead to different results.
            //
            // But this is just being needlessly pedantic 99.999999999999% of the time.
            // As long as you're not sharing references around or performing mutation
            // the risk here is low enough to ignore.
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.25
     * ───────────────────────────────────────────────────────
     * Where do things go?
     * Divide them up by their determinism!
     */
    @Test
    void listing6_25() {
        /*
        Assume a file system like:

        com.dop.invoicing
          |- latefees
          |    |- Core  ◄─── We'll put all our deterministic code here.
          |    |- Service
          |    |- Types
         */
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.26 through 6.29
     * ───────────────────────────────────────────────────────
     * Implementation begins!
     * This is where we'll start to see our modeling efforts begin
     * to pay us back. Most of the functions will just follow the
     * types we designed.
     */
    @Test
    void listing6_26_to_6_29() {
       class V1 {
           // Here's where we left on in Chapter 5.
           public static List<PastDue> collectPastDue(
                   EnrichedCustomer customer,
                   LocalDate today,
                   List<Invoice> invoices) {
               // Implement me!
               return null;
           }
       }

       class V2 {
            static boolean TODO = true;
            // This is where all the function stuff we talked about comes into play.
            // Deterministic functions can only do what their types say.
            // That's all they can do.
            // Which means that our implementation just follows the types we designed.
            public static List<PastDue> collectPastDue(
                    EnrichedCustomer customer,
                    LocalDate today,
                    List<Invoice> invoices) {
                // everything other than the filter is just doing what the type
                // signature says.
                return invoices.stream()
//                        .filter(invoice -> ???)  ◄─── We just have to decide what goes here.
                        .map(PastDue::new)
                        .toList();
           }
       }
       class V3 {
           public static List<PastDue> collectPastDue(
                   EnrichedCustomer customer,
                   LocalDate today,
                   List<Invoice> invoices) {
               return invoices.stream()
                       //                    ┌──── Adding in the filter implementation
                       //                    ▼
                       .filter(invoice -> isPastDue(invoice, customer.rating(), today))
                       .map(PastDue::new)
                       .toList();
           }

           static boolean isPastDue(Invoice invoice, CustomerRating rating, LocalDate today) {
               return invoice.getInvoiceType().equals(STANDARD)
                       && invoice.getStatus().equals(OPEN)
                       && today.isAfter(invoice.getDueDate().with(gracePeriod(rating)));
               //         └────────────────────────────────────────────────────────────┘
               //                               │
               //                               └─ Note how much this reads like the requirement! Neat!
           }

           // (We defined this one a few listings ago.)
           static TemporalAdjuster gracePeriod(CustomerRating rating) {
               return switch(rating) {
                   case CustomerRating.GOOD -> date -> date.plus(60, DAYS);
                   case CustomerRating.ACCEPTABLE -> date -> date.plus(30, DAYS);
                   case CustomerRating.POOR -> lastDayOfMonth();
               };
           }
       }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.30 through 6.32
     * ───────────────────────────────────────────────────────
     * "Just use maps"?
     */
    @Test
    void listing6_30_to_6_32() {
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

    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.31 through 6.38
     * ───────────────────────────────────────────────────────
     * The right type can reveal shortcomings in the design of the system.
     */
    @Test
    void listing6_31_to_6_38() {
        class V1 {
            // I'll keep drawing attention to it.
            // Checkout this type signature. It takes a list of invoices and
            // returns a single LateFee draft.
            // Our implementation is forced into being "small." Functions can't go off and do
            // anything they way. They map inputs to outputs.
            static LateFee<Draft> buildDraft(LocalDate today, EnrichedCustomer customer, List<PastDue> invoices) {
                // Which means that this is pretty much the only implementation
                // that's even allowed by our types. It HAS to return this data type.
                return new LateFee<>(  //─┐
                    new Draft(),       // │ And all of this is pre-ordained.
                    customer,          // │
                    null,              // │ The only thing left for us to do is implement the
                    today,             // │ thing the computes the total and the due dates.
                    null,              // │
                    invoices           //─┘
                );
            }
        }
        class V2 {
            // I'll keep drawing attention to it.
            // Checkout this type signature. It takes a list of invoices and
            // returns a single LateFee draft.
            // Our implementation is forced into being "small." Functions can't go off and do
            // anything they way. They map inputs to outputs.
            static LateFee<Draft> buildDraft(LocalDate today, EnrichedCustomer customer, List<PastDue> invoices) {
                // Which means that this is pretty much the only implementation
                // that's even allowed by our types. It HAS to return this data type.
                return new LateFee<>(  //─┐
                        new Draft(),       // │ And all of this is pre-ordained.
                        customer,          // │
                        null,              // │ The only thing left for us to do is implement the
                        today,             // │ thing the computes the total and the due dates.
                        null,              // │
                        invoices           //─┘
                );
            }

            // Implementing the due date is easy. If follows the requirements.
            static LocalDate dueDate(LocalDate today, PaymentTerms terms) {
                // Note that well typed functions are small! Often the first thing
                // we do is start returning data.
                return switch (terms) {
                    case PaymentTerms.NET_30 -> today.plusDays(30);
                    case PaymentTerms.NET_60 -> today.plusDays(60);
                    case PaymentTerms.DUE_ON_RECEIPT -> today;
                    case PaymentTerms.END_OF_MONTH -> today.with(lastDayOfMonth());
                };
            }

            // computing the total is far more interesting, because it holds something
            // that feels gross.

            // The "outside world" speaks BigDecimal and Currency.
            // Our world speaks USD (per the requirements).
            // ┌──────────────────────────────────────────────────────────────────┐
            //            THE FACT THAT THIS FEELS AWFUL IS A FEATURE
            // └──────────────────────────────────────────────────────────────────┘
            //
            // We shouldn't do this conversion in "our world." In fact, we shouldn't
            // "do" it at all. In an ideal world, we'd enforce this USD invariant
            // on data as it enters our system -- a process far removed from our
            // feature.
            //
            // The types are telling us that something is wrong with the design **of the system**.
            //
            // We don't have to fix it now, but we should call it out.
            static USD unsafeGetChargesInUSD(LineItem lineItem) throws IllegalArgumentException {
                if (!lineItem.getCurrency().getCurrencyCode().equals("USD")) {
                    // If this ever throws, the system as a whole is in a bad state.
                    throw new IllegalArgumentException("Big scary message here");
                } else {
                    return new USD(lineItem.getCharges());
                }
            }

            // Putting it all together, we get:
            static USD computeTotal(List<PastDue> invoices) {
                return invoices.stream().map(PastDue::invoice)
                        .flatMap(x -> x.getLineItems().stream())
                        .map(V2::unsafeGetChargesInUSD)
                        .reduce(USD.zero(), USD::add);
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.39 through 6.43
     * ───────────────────────────────────────────────────────
     * The Optional holy war
     */
    @Test
    void listing6_39_to_6_43() {
        // [note] listings 6.39 and 6.40 are skipped here
        //        since they're covered in the implementation package.
        //        see: dop.chapter06.the.implementation
        //
        // Instead, we'll focus on.... Optional!
        class __{
            // (ignore this. It's just here to power the below examples)
            static Optional<String> tryToFindThing(String whatever) {
                return Optional.empty();
            }

            // Optionals are contentious because we can interact with them
            // both functionally and imperatively.
            //
            // Here's the imperative style
            static String imperativeExample(String thingId) {
                Optional<String> maybeThing = tryToFindThing(thingId);
                return maybeThing.isPresent()
                    ? maybeThing.get().toUpperCase()
                    : "Nothing found!";
            }
            // Here's the functional approach.
            static String functionalExample(String thingId) {
                return tryToFindThing(thingId)
                        .map(String::toUpperCase)
                        .orElse("Nothing Found!");
            }
            // The question is: which is better?

            // Is this:
            static String example1(LateFee<Draft> draft) {
                return draft.customer().approval().map(approval -> switch(approval.status()) {
                    case APPROVED -> "new Billable(draft)"; // (stringified to mirror the shortened book example)
                    case PENDING -> "new NotBillable(draft, ...)";
                    case DENIED -> "new NotBillable(draft, ...)";
                }).orElse("new NeedsApproval(draft)");
            }
            // better than this?
            static String example2(LateFee<Draft> draft) {
                return draft.customer().approval().isEmpty()
                    ? "new NeedsApproval(draft)"
                    :  switch (draft.customer().approval().get().status()) {
                        case APPROVED -> "new Billable(draft)";
                        case PENDING -> "new NotBillable(draft, ...)";
                        case DENIED -> "new NotBillable(draft, ...)";
                };
            }
            // Or are they just different?
            // The book makes an argument for each in different situations.
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.44 through 6.47
     * ───────────────────────────────────────────────────────
     * It's ok to introduce types! As many as you need!
     *
     * Clarity while reading > enjoyment while writing.
     */
    @Test
    void listing6_44() {
        class __ {
            // we being here.
            // This is an OK method, but it's visually assaulting. It's too dense to
            // understand without slowing down to study it.
            public static ReviewedFee assessDraft(Entities.Rules rules, LateFee<Draft> draft) {
                if (draft.total().value().compareTo(rules.getMinimumFeeThreshold()) < 0) {
                    return new NotBillable(draft, new Reason("Below threshold"));
                } else if (draft.total().value().compareTo(rules.getMaximumFeeThreshold()) > 0) {
                    return draft.customer().approval().isEmpty()
                        ? new ReviewedFee.NeedsApproval(draft)
                        : switch (draft.customer().approval().get().status()) {
                            case APPROVED -> new Billable(draft);
                            case PENDING -> new NotBillable(draft, new Reason("Pending decision"));
                            case DENIED -> new NotBillable(draft, new Reason("exempt from large fees"));
                    };
                } else {
                    return new Billable(draft);
                }
            }
        }

        // So what if we did this: separate where we make a decision from what we do with it.
        class V2 {
            // We can introduce our own private enum to explain the semantics behind
            // what the assessments mean.
            private enum Assessment {ABOVE_MAXIMUM, BELOW_MINIMUM, WITHIN_RANGE}

            // and we can use that while figuring out what's up with the total.
            // doing so visually (and cognitively!) simplifies the code. We only
            // worry about one thing at a time.
            static Assessment assessTotal(Entities.Rules rules, USD total) {
                if (total.value().compareTo(rules.getMinimumFeeThreshold()) < 0) {
                    return Assessment.BELOW_MINIMUM;
                } else if (total.value().compareTo(rules.getMaximumFeeThreshold()) > 0) {
                    return Assessment.ABOVE_MAXIMUM;
                } else {
                    return Assessment.WITHIN_RANGE;
                }
            }
            // Which in turn simplifies this method.
            // It's been reduced down to pattern matching. This is quick to skim
            // and quick to understand.
            public static ReviewedFee assessDraft(Entities.Rules rules, LateFee<Draft> draft) {
                return switch (assessTotal(rules, draft.total())) {
                    case Assessment.WITHIN_RANGE -> new Billable(draft);
                    case Assessment.BELOW_MINIMUM -> new NotBillable(draft, new Reason("Below threshold"));
                    case Assessment.ABOVE_MAXIMUM -> draft.customer().approval().isEmpty()
                        ? new ReviewedFee.NeedsApproval(draft)
                        : switch (draft.customer().approval().get().status()) {
                            case APPROVED -> new Billable(draft);
                            case PENDING -> new NotBillable(draft, new Reason("Pending decision"));
                            case DENIED -> new NotBillable(draft, new Reason("exempt from large fees"));
                    };
                };
            }
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.48 - 6.55
     * ───────────────────────────────────────────────────────
     * The final listings in the book are tours of the final
     * implementation.
     *
     * Rather than duplicate them here, I've added the full
     * implementation to java/dop/chapter06/the/implementation.
     * You can browse all the source there in context.
     */
    @Test
    void listing6_48() {
        // See: java/dop/chapter06/the/implementation
    }
}
