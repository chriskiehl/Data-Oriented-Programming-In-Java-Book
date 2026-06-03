package dop.chapter02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

public class Listing2_5 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.5
   * ───────────────────────────────────────────────────────
   * Value classes should be compared exclusively by their
   * state. The identities will vary, but that's OK.
   * They're irrelevant to values!
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    Integer a = Integer.valueOf(3042);
    Integer b = Integer.valueOf(3042);
    // a and b are the same value, even though they’re
    // assigned to different objects, and different
    // variables, and have unique object identities.
    assertNotSame(a, b);     // false
    assertEquals(a, b);      // true
  }

}
