package dop.chapter04;


import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

/**
 * ───────────────────────────────────────────────────────
 * Listings 4.19 through 4.23
 * ───────────────────────────────────────────────────────
 * More requirements! Steps can be skipped!
 * Skipping steps on a checklist is a big deal in rocketry.
 * We have to know who did them, when, and *why*.
 *
 * Should be a small lift, right? After all, we already figured
 * out how to track when the steps got completed. Doing the
 * same thing for skipped should be a breeze.
 * ───────────────────────────────────────────────────────
 */
public class Listing4_19_4_22 {

    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.19
     * ───────────────────────────────────────────────────────
     * Another "easy" update?
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // All defined in previous listings
        record Step(String name) {}
        record Template(String name, List<Step> steps) {}
        record Instance(String name, Instant date, Template template){}
        record User(String value){}

        record Status(
                Template template,
                Step step,
                boolean isCompleted,
                User completedBy,
                Instant completedOn,
                Boolean isSkipped,    //  ◄──┐
                User skippedBy,       //     │ Copy/pasting our existing modeling.
                Instant skippedOn,    //     │ We get another flag, another user, another
                String rationale      //     │ timestamp, plus a new field for tracking why
                                      //     │ the step was skipped.
        ){
            // Fun exercise for the reader:
            // Try to write constructor validation which can catch every illegal state.
            //
            // I initially had this in the book to point out how egregious the validation
            // becomes, but it grew beyond something which would fit on a page. Also, worth
            // noting: I repeatedly got that validation wrong. I'd miss a case, or mix up
            // two cases, disable things which should be allowed -- it's the kind of validation
            // that makes you sit there and work through "Ok, so when x is set, y and z should
            // NOT be set... but when... then..."
        }

        // What surely hops out with the modeling above is that it isn't "DRY".
        // We might try to refactor it "as code" -- meaning, ignoring what the
        // meaning of the underlying data is (what we're trying to capture) and
        // instead refactoring "mechanically" -- manipulating the symbols to factor
        // out the duplication.

        /**
         * ───────────────────────────────────────────────────────
         * Listings 4.20
         * ───────────────────────────────────────────────────────
         * Attempting to "solve" the redundant fields by "DRYing"
         * the code.
         * ───────────────────────────────────────────────────────
         */
        // For instance, we can refactor the multiple booleans into
        // a single Enum. Nice!
        enum State {NOT_STARTED, COMPLETED, SKIPPED};
        // Ditto for the non-DRY user definitions. 
        record StatusV2(
                String name,
                State state,
                User actionedBy,    // We factor them out into a shared "actionedBy"
                Instant actionedOn, // shape that's contextual based on the current state.
                User confirmedBy,
                String rational
        ) {
            /**
             * ───────────────────────────────────────────────────────
             * Listings 4.21
             * ───────────────────────────────────────────────────────
             * Protecting ourselves from the illegal states our modeling allows
             * ───────────────────────────────────────────────────────
             */
            StatusV2 {
                // But, ugh.. this hasn't actually made our lives that much easier.
                // Our code is still allowed to express nonsensical states. Which means
                // we're still on the hook for defending against them. And that remains
                // extremely tedious and error prone.
                //
                if (state.equals(State.NOT_STARTED)) {   // The implementation for each case is left
                    // ...                                  as an exercise to the reader.
                }
                if (state.equals(State.COMPLETED)) {
                    // ...
                }
                if (state.equals(State.SKIPPED)) {
                    // ...
                }
                // This only gets worse as our requirements get more complex.
                // Imagine adding a Blocked or InProgress state. Each one will
                // require thinking *very* hard about the validation you and
                // what it means for existing states.
            }
        }
        /**
         * ───────────────────────────────────────────────────────
         * Listings 4.22
         * ───────────────────────────────────────────────────────
         * Ongoing woes
         * ───────────────────────────────────────────────────────
         */
        // The woes with the design go beyond the difficulty in validating it.
        // We have that ongoing problem where our data is "forgetful". Aside from
        // when we're validating it, we have no idea what status it's in.
        // So it again falls to the frail humans working on the code to remember
        // that (a) all of those statuses exist, (b) only some of them apply to
        // certain behaviors in the system, and (c) we have to *remember* to check
        // before we do anything.
        Function<StatusV2, Void> doSomethingWithCompleted = (StatusV2 status) -> {
            if (!status.state().equals(State.COMPLETED)) {
                // If we remember to do this, then we know that
                // we can safely read the actionedBy/On attributes
                // without a Null Pointer getting thrown.
            }
            // otherwise, we have to throw an error.
            throw new IllegalArgumentException("Expected completed");
        };

        // The problem with our current "refactored" model is that it hasn't
        // really solved any of the problems. The way we've modeled the code
        // *creates* invalid states and potential bugs. We have to expend tons
        // of effort fighting against the monster we created.
    }
}

