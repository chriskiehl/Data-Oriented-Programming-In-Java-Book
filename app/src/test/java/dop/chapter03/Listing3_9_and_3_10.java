package dop.chapter03;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class Listing3_9_and_3_10 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 3.9 to 3.12
   * ───────────────────────────────────────────────────────
   * What happens in so much Java code is that we forget to take
   * what we've learned about our domain and *actually* express it
   * in our code. Instead, it just lives in our heads. The code is
   * left to fend for itself. That leads to situations where our
   * model be used to create invalid data (rather than guide us
   * towards the right path).
   * ───────────────────────────────────────────────────────
   */
  @Test
  void example() {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 3.9
     * ───────────────────────────────────────────────────────
     * Reminder of where we left off
     * ───────────────────────────────────────────────────────
     */
    record Measurement(
        String sampleId,
        Integer daysElapsed,
        double contactAngle
    ) {}                        //     │ names to encode what something means (degrees)


    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.10
     * ───────────────────────────────────────────────────────
     * Garbage data
     * ───────────────────────────────────────────────────────
     */
    new Measurement(
        UUID.randomUUID().toString(),  //  ◄──┐ Despite those variable names and a bunch of
        -32,                           //     │ extensive doc strings, anybody can still march into
        9129.912                       //     │ our codebase and enter completely invalid data.
    );                                     //     │ This breaks every invariant of our data!
  }

}
