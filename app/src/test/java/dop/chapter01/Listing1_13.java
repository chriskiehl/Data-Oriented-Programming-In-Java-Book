package dop.chapter01;

import java.time.LocalDateTime;

public class Listing1_13 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 1.13
   * ───────────────────────────────────────────────────────
   * Luckily, it's not one or the other. It's not OOP vs DoP.
   * We can combine the strengths of both approaches.
   * ───────────────────────────────────────────────────────
   */


  class ScheduledTaskWithBestOfBothWorlds {
    private RetryDecision status;

    void reschedule() {
      // ...
    }

    public boolean isAbandoned() {
      // By combing the approaches, we get a nice internal
      // representation to program against. We can still use
      // OOP to control the interfaces. Further, doesn't this
      // code feel almost like it's writing itself? Of course,
      // we'd expose this method from our object -- it's a
      // core idea we uncovered while modeling the data!
      return this.status instanceof Abandoned;
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

}
