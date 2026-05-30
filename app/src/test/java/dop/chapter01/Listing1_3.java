package dop.chapter01;

import org.junit.jupiter.api.Test;

public class Listing1_3 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 1.3
   * ───────────────────────────────────────────────────────
   * The representation we've picked for the ID, despite being
   * trivial and one line, captures the soul of data oriented
   * programming. Our representation **creates** the possibility
   * for invalid program states. There are more wrong ways to create
   * this thing we've called ID than there are correct ones!
   * In fact, we still have no idea what ID really means. All we
   * can do with the current code is guess.
   * ───────────────────────────────────────────────────────
   */
  @Test
  void example() {
    // Every one of these is valid from the perspective of
    // how we've represented the idea in the type system.
    // Every one of these is invalid because they're all values
    // which don't belong to our domain (which hasn't been
    // communicated to us in the code)
    String id;
    id = "not-a-valid-uuid";
    id = "Hello World!";
    id = "2024-05-04";
    id = "2024-05-04";
    id = "1010011001011011";
  }

}
