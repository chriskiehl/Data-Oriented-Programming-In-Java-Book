package dop.chapter03;

import org.junit.jupiter.api.Test;

public class Listing3_02 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 3.2
   * ───────────────────────────────────────────────────────
   * Our first stab at representing this in Java might be a
   * mechanical translation. text-like things in the json
   * becomes Strings in Java. Numbers in the Json become ints
   * or doubles in Java.
   * ───────────────────────────────────────────────────────
   */
  @Test
  void example() {
    record Measurement(
        String sampleId,      //  ◄───┐
        int daysElapsed,      //      │ A very JSON-like modeling
        double contactAngle   //      │
    ) {}
  }

}
