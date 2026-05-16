package dop.chapter04;


import org.junit.jupiter.api.Test;

public class Listing4_34 {



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

