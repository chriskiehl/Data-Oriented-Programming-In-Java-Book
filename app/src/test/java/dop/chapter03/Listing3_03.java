package dop.chapter03;

import org.junit.jupiter.api.Test;

public class Listing3_03 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 3.3
   * ───────────────────────────────────────────────────────
   * Taking a closer look at our data. Each of these fields
   * is about something. It has a meaning within its domain.
   * Have we captured that meaning in our code? Let's look
   * at the data again.
   * ───────────────────────────────────────────────────────
   */
  @Test
  void example() {
    // {
    //    "sampleId”: "UV-1",
    //    "daysElapsed": 3,    ◄─── What the heck kind of measurement is "daysElapsed"?
    //    "contactAngle": 17.4  //  ◄─── 17.4... *what*? Degrees? Radians? Other?
    // }
  }

}
