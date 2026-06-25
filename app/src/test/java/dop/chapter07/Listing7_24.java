package dop.chapter07;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Listing7_24 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.24
   * ───────────────────────────────────────────────────────
   * Algebraic style assertions in Java
   * ───────────────────────────────────────────────────────
   */
  void example() {
    for (State state : allPossibleStates()) {
      assertTrue(someInvariant(state));
    }
  }








  enum State { A }

  static State[] allPossibleStates() {
    return State.values();
  }

  static boolean someInvariant(State state) {
    return true;
  }

}
