package dop.chapter01;

import java.time.LocalDateTime;
import java.util.List;

public class Listing1_12 {

/**
     * ───────────────────────────────────────────────────────
     * Listings 1.12
     * ───────────────────────────────────────────────────────
     * The improvements from Listing 1.11 ripple outward in a
     * similar way to the improvements we made in listing 1.9 & 1.10.
     * ───────────────────────────────────────────────────────
     */
    static class SchedulerV3 {
        List<ScheduledTaskWithBetterOOP> tasks;

        // (Imagine a bunch of other methods here...)

        private void pruneTasks() {
            // The API method nicely clarifies what the state of the
            // task means. This is much better than an ambiguous null check.
            this.tasks.removeIf((task) -> task.isAbandoned());
        }
    }













    static class ScheduledTaskWithBetterOOP {
        private LocalDateTime scheduledAt;

        public boolean isAbandoned() {
            return this.scheduledAt == null;
        }
    }
}
