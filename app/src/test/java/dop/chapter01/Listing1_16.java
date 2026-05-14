package dop.chapter01;

import java.time.Instant;
import java.time.LocalDateTime;

import static java.time.Instant.now;


public class Listing1_16 {

/**
     * ───────────────────────────────────────────────────────
     * Listings 1.16
     * ───────────────────────────────────────────────────────
     * Letting the data types guide our refactoring of the
     * reschedule method.
     * ───────────────────────────────────────────────────────
     */
    class DataDrivenRefactoringOfScheduledTask {
        private RetryDecision status;

        static final int DELAY = 30;

        // What's powerful about this refactoring is that it makes our
        // code describe exactly what it is to everyone who reads it.
        // No wikis or external 'design docs' needed. There's no secret
        // institutional knowledge. The code teaches us what we need to
        // know about how retries are handled in this domain.
        RetryDecision reschedule(FailedTask failedTask) {
            return switch (failedTask) {
                case FailedTask t when someSuperComplexCondition(t) ->
                        new RetryImmediately(now(), attemptsSoFar(status) + 1);
                case FailedTask t when someOtherComplexCondition(t) ->
                        new ReattemptLater(now().plusSeconds(DELAY));
                default -> new Abandoned();
            };
        }














        // As with the prior listing. The implementation for all
        // the methods below are just placeholders. We don't care
        // (or even *want* to care) about their implementation details.
        // We should be able to reason "above" the details by looking
        // at the high level data types in the code.
        //───────────────────────────────────────────────────────┐
        boolean someSuperComplexCondition(FailedTask task) {  // │
            return false;                                     // │
        }                                                     // │
        boolean someOtherComplexCondition(FailedTask task) {  // │
            return false;                                     // │
        }                                                     // │
        int delay() {                                         // │
            return 0;                                         // │
        }                                                     // │
        private int attemptsSoFar(RetryDecision decision) {   // │
            return 1;                                         // │
        }                                                     // │
        //───────────────────────────────────────────────────────┘
    }


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
        // compilation of the listing above.
    }
}
