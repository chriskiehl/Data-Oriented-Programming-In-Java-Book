package dop.chapter07;

import java.util.function.BiFunction;

public class Listing7_02 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.2
   * ───────────────────────────────────────────────────────
   * Java’s BinaryOperator interface
   * ───────────────────────────────────────────────────────
   */
  @FunctionalInterface
  public interface BinaryOperator<T> extends BiFunction<T,T,T> {}
//                                ▲                     ▲ ▲ ▲
//                                │                     │ │ │
//                  Binary operations take and return the same type

}
