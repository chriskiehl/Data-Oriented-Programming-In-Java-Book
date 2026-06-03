package dop.chapter04;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Listing4_17_to_4_18 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 4.17
   * ───────────────────────────────────────────────────────
   * Defensive coding to the rescue?
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    // All defined in previous listings
    record Step(String name) {}
    record Template(String name, List<Step> steps) {}
    record Instance(String name, Instant date, Template template) {}
    record User(String value) {}

    record Status(
        Template template,
        Step step,
        boolean isCompleted,
        User completedBy,
        Instant completedOn
    ) {
      Status {
        // this is tedious to write, but it gets the job done.
        // We're kind of back on track. We've "prevented" invalid
        // data from being created.
        if (isCompleted && (completedBy == null || completedOn == null)) {
          throw new IllegalArgumentException(
            "completedBy and completedOn cannot be null " +
            "when isCompleted is true"
          );
        }
        if (!isCompleted && (completedBy != null || completedOn != null )) {
          throw new IllegalArgumentException(
            "completedBy and completedOn cannot be populated " +
            "when isCompleted is false"
          );
        }
      }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.18
     * ───────────────────────────────────────────────────────
     * No more ill-defined data!
     * ───────────────────────────────────────────────────────
     */
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      Template template = new Template("Cool Template", List.of(new Step("Step 1")));
      new Status(
        template,
        template.steps().getFirst(),
        false,
        null,  // Now any attempts at creating invalid states will be rejected
        Instant.now()
      );
    });
  }

}

