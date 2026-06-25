package dop.chapter07;

import java.util.Optional;
import java.util.function.BinaryOperator;

public class Listing7_56 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.56
   * ───────────────────────────────────────────────────────
   * Refactoring to generic types
   * ───────────────────────────────────────────────────────
   */
  static <A> Optional<A> apply(
      BinaryOperator<A> operator,          //  ┐
      Optional<A> a,                       //  │◄── Making the apply method
      Optional<A> b) {                     //  │    work with any type <A> from
    if (a.isPresent() && b.isPresent()) {  //  ┘    the normal universe
      return Optional.of(operator.apply(a.get(), b.get()));
    } else {
      return Optional.empty();
    }
  }

}
