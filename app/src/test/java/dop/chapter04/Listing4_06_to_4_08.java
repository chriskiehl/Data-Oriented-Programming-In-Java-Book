package dop.chapter04;

import java.util.List;

import org.junit.jupiter.api.Test;

public class Listing4_06_to_4_08 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 4.6 through 4.8
   * ───────────────────────────────────────────────────────
   * Here's our second stab at turning what we know about our
   * data into code.
   *
   * Once we've got the sketch in place, we start the important
   * part of the design process: taking a step back to look at
   * what our sketch of the data model *means*. What does it
   * communicate? What does it enable us to "say" with the code?
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    // Reusing the model from our initial stab at this in listing 4.1 just to kick things off
    record Step(String name, boolean isCompleted) {
    }

    // Note this refinement from the initial stab at this.
    // Steps live on a "Template", rather than what we ambiguously
    // called just a “Checklist” before.
    record Template(String name, List<Step> steps) {
    }

    // [IGNORE] (We haven't modeled this yet!)
    // This is here just to make the example below compilable.
    record Instance(List<Step> steps) {}
    // [END IGNORE]

    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.7
     * ───────────────────────────────────────────────────────
     * Bad modeling allows our code to "say" strange things.
     * For instance: "Socks are always already packed"
     * ───────────────────────────────────────────────────────
     */
    // The quick check we can always perform while design is seeing
    // if our code lets us express anything weird.
    Template travelPrep = new Template(
        "International Travel Checklist",
        List.of(
            new Step("Passport", false),
            new Step("Toothbrush", false),
            new Step("bring socks", true)
        ) //                     ▲
        //                       └─ What the heck does it mean if items on a template are pre-set
        //                          to true? This step is already always completed? All socks are
        //                          already pre-packed for all trips?
        //
        // Our data model allows us to express a bunch of nonsense!
    );

    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.8
     * ───────────────────────────────────────────────────────
     * We could always just defensively make sure that...
     * ───────────────────────────────────────────────────────
     */
    // This is a dangerous part in the design process that we have to train
    // ourselves to notice. The "programmer" part of our brain -- the one that
    // thinks in code, might look at this modeling error and start down an
    // innocent sounding, but critically damaging thought pattern: the one that
    // usually starts with "well, we could just..."
    //
    // For example:
    //
    // "We could just..." use a defensive check or transform during construction
    // to make sure that no funky states are created.
    new Instance(travelPrep.steps().stream()
        .map(step -> new Step(step.name(), false))
        .toList()); //                       └── Defensively resetting all the steps "just in case"
    //                                           when we create instances of our checklist.

    // This works under exactly one condition: you remember to do it.

    // We can do better. We can make the code enforce its own meaning and
    // help us, the fallible programmers, not make mistakes.
  }

}

