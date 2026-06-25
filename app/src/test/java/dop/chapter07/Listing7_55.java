package dop.chapter07;

import java.util.Optional;
import java.util.function.BinaryOperator;

public class Listing7_55 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.55
   * ───────────────────────────────────────────────────────
   * Refactoring to pass in the BinaryOperator
   * ───────────────────────────────────────────────────────
   */
  //                          ┌── This function is no longer about just adding Integers, so
  //                          ▼   we've given it a more generic name
  static Optional<Integer> apply(
//  ┌──────────────────────────────────┐
      BinaryOperator<Integer> operator,
//  └──────────────────────────────────┘
//                  ▲
//                  └──── Taking the binary operator as an argument lets us evaluate
//                        it inside the world of Optional
      Optional<Integer> a,
      Optional<Integer> b) {
    if (a.isPresent() && b.isPresent()) {
//                      ┌─────────────────────────────────┐
      return Optional.of(operator.apply(a.get(), b.get()));
//                      └─────────────────────────────────┘
//                                       ▲
//                                       └──── Taking the binary operator as an argument lets us
//                                             evaluate it inside the world of Optional
    } else {
      return Optional.empty();
    }
  }

}
