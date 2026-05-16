package dop.chapter04;


import org.junit.jupiter.api.Test;

public class Listing4_36_to_4_36 {


    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.35
     * ───────────────────────────────────────────────────────
     * 
     * ───────────────────────────────────────────────────────
     */


    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.36 through 4.40
     * ───────────────────────────────────────────────────────
     * A very important part of modeling is being able to say
     * what something *isn't*. This is the central value proposition
     * of enums. It allows us to model a closed domain.
     *
     * A gap with how we've modeled the StepState so far is that it
     * is *not* closed. It can be freely extended by anyone. Sometimes
     * this is what we want. Open to extension is a fantastic principle
     * for library development. However, just as often, we do not
     * want our model extended. There are only two booleans. There
     * are only 4 card suits. In our domain, there are only three
     * states a checlist step can be: NotStarted, Completed, or Skipped.
     * ───────────────────────────────────────────────────────
     */

    interface StepState {}

    @Test
    public void example() {

        record NotStarted() implements StepState {}
        record Completed() implements StepState {}
        record Skipped() implements StepState {}

        // The problem here is that this isn't closed. It's completely
        // open to anyone who wants to extend the interface.
        // Are these members of our domain?
        record Blocked() implements StepState {}
        record Paused() implements StepState {}
        record Started() implements StepState {}
        // They might be valid in some *other* domain, but they aren't
        // valid in ours.
        //
        // This is the role of the `sealed` modifier.
        // We can tell Java that we only want to permit certain
        // data types to implement our interface.

    }
}

