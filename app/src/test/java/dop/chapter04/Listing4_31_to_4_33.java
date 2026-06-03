package dop.chapter04;


import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Listing4_31_to_4_33 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 4.31
   * ───────────────────────────────────────────────────────
   * Defining a new data type with an Interface
   * ───────────────────────────────────────────────────────
   */
  interface ChecklistStatus {   // ◄── The interface name is treated a data type in our code
    // ◄── The body of the interface is empty, because we’re not defining any behaviors. [
  }


  @Test
  public void example() {
    // All defined in previous listings
    record Step(String name) {}
    record Template(String name, List<Step> steps) {}
    record Instance(String name, Instant date, Template template){}
    record User(String value){}

    /**
     * ───────────────────────────────────────────────────────
     * Listing 4.32
     * ───────────────────────────────────────────────────────
     * Implementing the ChecklistStatus interface
     * ───────────────────────────────────────────────────────
     */
    record NotStarted(
        Template template,
        Step step
    ) implements ChecklistStatus {}  // Each record type implements this interface

    record Completed(
        Template template,
        Step step,
        User completedBy,
        Instant completedOn
    ) implements ChecklistStatus {}  // Here, too

    record Skipped(
        Template template,
        Step step,
        User skippedBy,
        Instant skippedOn,
        String rationale
    ) implements ChecklistStatus {}  // and here.

    /**
     * ───────────────────────────────────────────────────────
     * Listing 4.33
     * ───────────────────────────────────────────────────────
     * Capturing the statuses as types
     * ───────────────────────────────────────────────────────
     */
    // Now we can use this interface to unite the disparate types.
    // All of them belong to / are "about" this idea of Step Statuses.
    Template template = new Template("Howdy", List.of(
        new Step("1"),
        new Step("2")
    ));
    Step step = template.steps().getFirst();

    //    ┌─ We can assign the Completed data type to StepStatus
    //    │  because the interface unites them under the same "family"
    //    │
    //    ▼                                       │
    ChecklistStatus completed = new Completed( //  ◄───┘
        template,
        step,
        new User("Bob"),
        Instant.now()
    );

    // This modeling lets us express *choice* within Java.
    // A StepStatus is either NotStarted, Completed, or Skipped.
  }

}

