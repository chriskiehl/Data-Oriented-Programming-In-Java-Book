package dop.chapter01;

import java.time.LocalDateTime;
import java.util.List;

public class Listing1_7 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 1.7
   * ───────────────────────────────────────────────────────
   * The problem with the example above (listing 1.5 & 1.6) is
   * that we can't tell what any of it means. The code doesn't
   * tell us why it's setting those variables. Instead, we have
   * to piece it together "non-locally" by hunting down clues in
   * other parts of the codebase.
   * ───────────────────────────────────────────────────────
   */
  static class Scheduler {
    List<ScheduledTask> tasks;

    // (Imagine a bunch of other methods here...)

    // If we're lucky, we might eventually stumble on one that
    // explains what the heck a particular state means.
    // In this case, we figure out that if we set scheduledAt to
    // null, that implicitly means that the scheduler should remove
    // this tasks and give up on it.
    private void pruneTasks() {
      this.tasks.removeIf((task) -> task.scheduledAt() == null);
    }
  }

  static class ScheduledTask {
    private LocalDateTime scheduledAt;
    private int attempts;
    LocalDateTime scheduledAt() {
      return scheduledAt;
    }
  }

}
