package dop.chapter01;

import java.time.LocalDateTime;
import java.util.List;

public class Listing1_09_and_1_10 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 1.9 & 1.10
   * ───────────────────────────────────────────────────────
   * Good modeling has a simplifying effect on the entire
   * codebase. We can refactor other parts of the code to
   * use the domain concepts. It replaces "hmm... Well, null
   * must mean that..." style reasoning with concrete, declarative
   * code that tells you *exactly* what it means.
   * ───────────────────────────────────────────────────────
   */
  static class Scheduler {
    List<ScheduledTaskV2> tasks;

    // (Imagine a bunch of other methods here...)

    // Refactoring to use our explicit data type
    private void pruneTasks() {
      // Don't let this instanceof scare you off!
      // This would be "bad" when doing OOP and dealing with objects (with
      // identities), but we're not doing OOP. We're programming with data.
      this.tasks.removeIf((task) -> task.status() instanceof Abandoned);
      // Compare this to the original version!
      // [Original]: this.tasks.removeIf((task) -> task.scheduledAt() == null);
    }
  }







  /**
   * (This modeling is not shown in the book for brevity)
   * We're creating a family of related data types. The mechanics of this
   * construct will be covered in Chapters 3 and 4.
   */
  sealed interface RetryDecision permits RetryImmediately, ReattemptLater, Abandoned {
  }

  record RetryImmediately(LocalDateTime next, int attemptsSoFar) implements RetryDecision {
  }

  record ReattemptLater(LocalDateTime next) implements RetryDecision {
  }

  record Abandoned() implements RetryDecision {
  }

  static class ScheduledTaskV2 {
    private RetryDecision status;
    public RetryDecision status() {
      return this.status;
    }
  }

}
