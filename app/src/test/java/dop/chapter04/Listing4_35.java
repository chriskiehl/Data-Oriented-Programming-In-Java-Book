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
public class Listing4_35 {



    interface StepState {}

    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.35
     * ───────────────────────────────────────────────────────
     * Expressing OR with records and interfaces is like having
     * super powered enums. The two modeling ideas are very similar.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        enum StepStatus {
            NotStarted,
            Completed,
            Skipped;
        }

                          // An alternative way of modeling the
        record NotStarted() implements StepState {}    // idea of mutual exclusivity.
        record Completed() implements StepState {}     // Thinking of them *as* fancy enums can be
        record Skipped() implements StepState {}       // a really useful mental model.
    }
}

