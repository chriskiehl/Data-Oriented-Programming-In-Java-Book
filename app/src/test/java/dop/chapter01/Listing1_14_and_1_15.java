package dop.chapter01;

import java.time.Instant;
import java.time.LocalDateTime;

public class Listing1_14_and_1_15 {

/**
     * ───────────────────────────────────────────────────────
     * Listings 1.14 & 1.15
     * ───────────────────────────────────────────────────────
     * We've made a lot of improvements to the implementation, but
     * the method signature is still super vague.
     * ───────────────────────────────────────────────────────
     */
    class FixingScheduledTasksRemainingAmbiguity {
        private RetryDecision status;

        // An informational black hole!
        //
        // ┌────────  It returns nothing!
        // ▼
        void reschedule( ) {   //  ◄─────────────────────────────────┐
            // ...      ▲                                            │ Compare how very different
        }   //          └────── It takes nothing!                    │ these two methods are in
        //                                                           │ terms of what they convey
        RetryDecision rescheduleV2(FailedTask failedTask) {  //  ◄───┘ to us as readers
        //    ▲                          ▲
        //    │                          └── takes a failed task
        //    │
        //    └── and tells us what to do with it

            return null; // {We'll implement this in a followup example}
        }
    }
















    /**
     * (This modeling is not shown in the book for brevity)
     * We're creating a family of related data types. The mechanics of this
     * construct will be covered in Chapters 3 and 4.
     */
    sealed interface RetryDecision permits RetryImmediately, ReattemptLater, Abandoned {
    }

    record RetryImmediately(Instant next, int attemptsSoFar) implements RetryDecision {
    }

    record ReattemptLater(Instant next) implements RetryDecision {
    }

    record Abandoned() implements RetryDecision {
    }

    static class FailedTask {
        // blank. Here just to enable
        // compilation of listing 1.14 above
        // The fact that it tells us quite a bit without
        // us implementing anything is pretty nice feature!
    }
}
