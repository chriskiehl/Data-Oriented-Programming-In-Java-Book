package dop.chapter02;

import org.junit.jupiter.api.Test;

public class Listing2_16 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 2.16
   * ───────────────────────────────────────────────────────
   * We use the final keyword to control whether we're creating
   * an identity or a value.
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    // Now this is a value!
    final Integer xPosition = Integer.valueOf(4);
    // If we try to change it Java won't even let our code compile.
    // xPosition = Integer.valueOf(5);
  }

}
