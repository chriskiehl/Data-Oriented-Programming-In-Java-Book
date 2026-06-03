package dop.chapter02;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class Listing2_23 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 2.23
   * ───────────────────────────────────────────────────────
   * The really fun part of data-oriented programming is learning
   * to see the world differently. Data is everywhere! It can
   * be anything. It can even be abstract!
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    // These two value classes model decisions that our
    // system could make (for a refresher on these types, checkout
    // the Listings from chapter 01).
    class ReattemptLater {
      LocalDateTime scheduledAt;
      // getters,final,equals, and hashcode omitted
    };
    // You might have to squint a bit to "see" these as being
    // data just like we would a sales or weather report. They
    // tick all the boxes, though!
    class RetryImmediately {
      LocalDateTime scheduledAt;
      int attempts;
      // getters,final,equals, and hashcode omitted
    };
  }

}
