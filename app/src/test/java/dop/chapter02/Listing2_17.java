package dop.chapter02;

import org.junit.jupiter.api.Test;

public class Listing2_17 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 2.17
   * ───────────────────────────────────────────────────────
   * The control we get with final also applies to classes.
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {

    // ┌ We mark the class as final, too. Inheritance of
    // │ values can be done (with a lot of caution), but
    // │ avoiding it cuts off a lot of potential problems
    // ▼
    final class Vector {

      final double x;  // │ Marking the instance variables as final ensures
      final double y;  // │ what this class *is*, value class, is communicated
                       // │ and enforced.
      public Vector(double x, double y) {
        this.x = x;
        this.y = y;
      }
    }
    // combine all of that with a final variable, and you've
    // got yourself a true value object!
    final Vector vector = new Vector(2, 3);
  }

}
