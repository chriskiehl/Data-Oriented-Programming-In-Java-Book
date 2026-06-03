package dop.chapter02;

import org.junit.jupiter.api.Test;

public class Listing2_13 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.13
   * ───────────────────────────────────────────────────────
   * Identities are created when we assign values to
   * *mutable* variables!
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    int xPosition = 4;
    // This does not create a value!
    // It creates an identity, xPosition, against which
    // values will be tracked over time.
    // We can do something that should be impossible:
    xPosition = 5; // Change breaks the core contract of values!
  }

}
