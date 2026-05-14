package dop.chapter04;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Chapter 4 builds on top of chapter 3's exploration of
 * learning to see through our code into what it actually
 * means and communicates. This is a fun one, because it
 * walks through the design process in all of its messy
 * glory. We'll make mistakes, refine, refactor -- and even
 * go back to the drawing board a few time.
 *
 * What I hope to show is that focusing on the data and getting its
 * representation pinned down *before* we start worrying about
 * its behaviors makes any mistakes low cost and fast to correct.
 * This approach enables rapid prototyping and immediate feedback.
 * We can learn from our mistakes before we start pouring concrete
 * in the form of implementation code.
 */
public class Listing4_36_to_4_40 {



    interface StepState {}

    // Note: sealing doesn't work locally inside a method.
    //       So, it's commented out here. Checkout the supplementary
    //       file `test.dop.chapter03.SealingExample` to see it in action
    sealed interface StepStateV2 {}
    record NotStartedV2() implements StepStateV2 {}
    record CompletedV2() implements StepStateV2 {}
    record SkippedV2() implements StepStateV2 {}

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

