package dop.chapter05;

import dop.chapter05.the.existing.world.Entities.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.*;

public class Listing5_34_to_5_37 {


    // This is yet another option for representing complex lifecycle
    // states without duplicating all the fields. Rather than extract
    // what's the same, we can extract what's unique into its own type.
    //
    record InvoiceId(String value){}
    record Rejection(String why){}
    record Draft() implements Lifecycle{}
    record Billed(InvoiceId id) implements Lifecycle {}
    record Rejected(Rejection why) implements Lifecycle {}

    sealed interface Lifecycle {}

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.34 - 5.39
     * ───────────────────────────────────────────────────────
     * Exploring even more representations.
     *
     * We'll seldom get things right on the first try -- and that's ok!
     * The nice thing about just playing around with data is how
     * low the stakes are. Refactoring data's representation is far
     * easier that refactoring code.
     */
    @Test
    public void example() {
        record PastDue(Invoice invoice) {}
        record InvoiceId(String value){}
        record Rejection(String why){}
        record USD(BigDecimal value) {
            // We don't define these in the listing, but they'd look
            // something like this.
            static USD zero() {return new USD(BigDecimal.ZERO);}
            static USD add(USD x, USD y){ return new USD(x.value().add(y.value()));}
        }


        record LateFee(
                Lifecycle state,  //    ◄── Now all the common stuff and the unique
                USD total,        //        lifecycle info can live on the same model
                LocalDate invoiceDate,
                LocalDate dueDate,
                List<PastDue> includedInFee
        ){}


        /**
         * ───────────────────────────────────────────────────────
         * Listing 5.34
         * ───────────────────────────────────────────────────────
         * patching around our own modeling limitations
         * ───────────────────────────────────────────────────────
         */
        class Example {
            // Programming with our data types is pleasant again.
            // We've restored some runtime flexibility. Computing aggregate stats
            // across all lifecycle states is now trivial.
            Map<Lifecycle, USD> totalsByLifecycle(List<LateFee> fees) {
                return fees.stream()
                    .collect(groupingBy(LateFee::state,
                        mapping(LateFee::total, reducing(USD.zero(), USD::add))));
            }
        }

        /**
         * ───────────────────────────────────────────────────────
         * Listing 5.35
         * ───────────────────────────────────────────────────────
         * Arg. We're back to defensive programming.
         * ───────────────────────────────────────────────────────
         */
        // ┌────────────────────────────────────────────────────────────────────┐
        // │                             HOWEVER!                               │
        // └────────────────────────────────────────────────────────────────────┘
        //
        // Not all is perfect with this representation.
        // In exchange for runtime flexibility, we lost the ability to
        // enforce important invariants at compile time.
        class BillingExample {

            //                    ┌─ Our code has started lying again :(
            //                    ▼
            LateFee submitBill(LateFee fee) {
                //
                //                   ┌─ And so we're back on the hook for defensive programming :(
                //                   ▼
                //  ┌────────────────────────────────────────┐
                if (!(fee.state() instanceof Draft)) {
                    throw new IllegalArgumentException(
                        "You're about to charge something you shouldn't!!!"
                    );
                }
                return null;
            }
        }

        /**
         * ───────────────────────────────────────────────────────
         * Listing 5.36
         * ───────────────────────────────────────────────────────
         * We've lost expressivity in our data model
         * ───────────────────────────────────────────────────────
         */
        // Another big problem with all of this is that we've lost the
        // ability to communicate. Our "story" has become truncated and imprecise.
        //
        // List<Invoice>
        // -> List<PastDue>
        //      ┌─────────────────── What happened to "Draft"?
        //      ▼
        // -> LateFee           ┌─── We've lost all the subtlety of how the Late Fee changes.
        // -> ???               ▼
        // -> BillableFee -> LateFee (??? OR ???)
        //
        // That's no good.
        //
        // Let's try this:
        //
        // ┌────────────────────────────────────────────────────────────────────┐
        // │             Capturing lifecycle state as a Type Variable           │
        // └────────────────────────────────────────────────────────────────────┘
        //
        //                ┌─ We can "parameterize" LateFee by its Lifecycle information
        //                │
        //                │                ┌─ The generic Type Variable, State, is bounded by
        //                ▼                ▼  the type Lifecycle. It won't accept anything else!
        record LateFeeV2<State extends Lifecycle>(
                State state, //  ◄──────────────┐ This type variable is referenced on the
                USD total,   //                 │ model, which unifies the compile-time and runtime
                LocalDate invoiceDate,//        │ representations!
                LocalDate dueDate,
                List<PastDue> includedInFee
        ){}

        // Generics aren't just for collections!
        // Expressing constraints in the type system allows us to
        // USE that type information while defining methods
        class LeveragingTypeInformationInMethods {
            void doSomething1(LateFeeV2<Draft> fee) {/*...*/}
            void doSomething2(LateFeeV2<Billed> fee) {/*...*/}
            void doSomething3(LateFeeV2<Rejected> fee) {/*...*/}
            void doSomething4(LateFeeV2<? extends Lifecycle> fee) {/*...*/}
            void doSomething5(LateFeeV2 fee) {/*...*/}
            //                   ▲
            //                   └─── We can completely leave off the type information.
            //                        Java will produce a helpful warning. This is a great
            //                        option for slowly refactoring legacy code towards
            //                        type safety.
        }

        // These types let us express rich domain information at all "levels" of
        // our program
        //
        // ┌────────────────────────────────────────────────────────────────────┐
        // │             Restoring the richness of our story                    │
        // └────────────────────────────────────────────────────────────────────┘
        // List<Invoice>
        // -> List<PastDue>
        // -> LateFee<Draft>
        // -> BillableFee -> LateFee<Billed> OR LateFee<Rejected>

        //
        // Neat, right?
    }
}


