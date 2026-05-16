package dop.chapter04;

import org.junit.jupiter.api.Test;

public class Listing4_36 {

    interface StepState {}
    /**
     * ───────────────────────────────────────────────────────
     * Listing 4.36
     * ───────────────────────────────────────────────────────
     * Are these relevant to our domain?
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {

        record NotStarted() implements Listing4_36_to_4_36.StepState {}
        record Completed() implements Listing4_36_to_4_36.StepState {}
        record Skipped() implements Listing4_36_to_4_36.StepState {}

        // The problem here is that this isn't closed. It's completely
        // open to anyone who wants to extend the interface.
        // Are these members of our domain?
        record Blocked() implements Listing4_36_to_4_36.StepState {}   // ─┐
        record Paused() implements Listing4_36_to_4_36.StepState {}    //  │─ These are not relevant!
        record Started() implements Listing4_36_to_4_36.StepState {}   // ─┘
        // They might be valid in some *other* domain, but they aren't
        // valid in ours.
        //
        // This is the role of the `sealed` modifier.
        // We can tell Java that we only want to permit certain
        // data types to implement our interface.
    }
}
