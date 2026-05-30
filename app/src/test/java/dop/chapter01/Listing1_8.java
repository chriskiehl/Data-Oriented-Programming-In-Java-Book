package dop.chapter01;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;

public class Listing1_8 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 1.8
   * ───────────────────────────────────────────────────────
   * The thing we strive for in data-oriented programming is
   * to be able to communicate effectively within the code.
   * We want to use a representation that tells any reader
   * exactly what we're trying to accomplish. Those opaque
   * variable assignments can be made clear by giving them
   * names. Naming is a magical thing with tons of power!
   * ───────────────────────────────────────────────────────
   */
  static class ScheduledTask {
    // We've replaced the ambiguous instance variables with
    // a new descriptive data type.
    private RetryDecision status;  // ◄── NEW!


    // Checkout how different this code *feels*.
    // it now tells us exactly what it does.
    // It chooses between 1 of 3 possible actions:
    //    * Retry Immediately
    //    * Attempt this later
    //    * Abandon it entirely
    void reschedule() {
      if (this.someSuperComplexCondition()) {
        this.status = new RetryImmediately(  // ◄── NEW!
          now().plusSeconds(this.delay()),
          this.attemptsSoFar(status) + 1
        );
      } else if (this.someOtherComplexCondition()) {
        this.status = new ReattemptLater(this.standardInterval()); // ◄── NEW!
      } else {
        this.status = new Abandoned();    // ◄── NEW!
      }
    }

    boolean someSuperComplexCondition() {
      return false;
    }
    boolean someOtherComplexCondition() {
      return false;
    }
    int delay() {
      return 0;
    }
    private LocalDateTime standardInterval() {
      return now();
    }
    private int attemptsSoFar(RetryDecision decision) {
      return switch (decision) {
        case RetryImmediately(var __, int attempts) -> attempts;
        default -> 0;
      };
    }
    private void setStatus(RetryDecision decision) {
      this.status = decision;
    }
    public RetryDecision status() {
      return this.status;
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
