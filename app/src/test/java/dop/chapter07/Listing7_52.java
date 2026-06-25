package dop.chapter07;

import java.util.Optional;
import java.util.function.BinaryOperator;

public class Listing7_52 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.52
   * ───────────────────────────────────────────────────────
   * This is OK because we never know what’s inside of the Optional
   * ───────────────────────────────────────────────────────
   */
  static <A> Optional<A> doSomething(BinaryOperator<A> operator) {
//        ▲           ▲                             ▲
//        │           │                             │
//        └ We have no idea what’s inside the Optional.
//          All we see is a generic type variable.

    return null; // (so it compiles)
  }

}
