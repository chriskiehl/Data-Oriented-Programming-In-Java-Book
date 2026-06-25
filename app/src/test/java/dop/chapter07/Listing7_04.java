package dop.chapter07;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing7_04 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.4
   * ───────────────────────────────────────────────────────
   * A few familiar binary operations
   * ───────────────────────────────────────────────────────
   */
  void example() {
    assertEquals(1 + 1, 2);
    assertEquals(2.0 + 2.0,  4.0);
    assertEquals("Hello" + "World",  "HelloWorld");
  }

}
