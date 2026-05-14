package dop.chapter01;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.time.LocalDateTime.now;

public class Listing1_11 {

/**
     * ───────────────────────────────────────────────────────
     * Listings 1.11
     * ───────────────────────────────────────────────────────
     * You might argue that the problem with the original code
     * was that it leaked information. It didn't define domain
     * level API methods for consumers. Fair! Let's see what
     * that looks like.
     * ───────────────────────────────────────────────────────
     */
    static class ScheduledTaskWithBetterOOP {
        private LocalDateTime scheduledAt;   // we might keep the design of
        private int attempts;                // these attributes the same


        void reschedule() {
            // body omitted for brevity
        }

        //──────────────────────────────────────┐
        public boolean isAbandoned() {        //│ And use this to hide the implementation details while
            return this.scheduledAt == null;  //│ also clarifying what the state assignments mean.
        }                                     //│ A nice improvement!
        //──────────────────────────────────────┘
    }
}
