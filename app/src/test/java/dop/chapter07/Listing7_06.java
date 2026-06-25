package dop.chapter07;

import java.math.BigDecimal;
import java.util.function.Function;

public class Listing7_06 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.6
   * ───────────────────────────────────────────────────────
   * Binary Operations exposed as Unary Operations off objects
   * ───────────────────────────────────────────────────────
   */
  void example() {
      BigDecimal.ONE.add(BigDecimal.ONE);
//                    ▲
//                    └──── Sometimes you’ll have to squint to see the binary
//                          operations. Even though this only accepts a single
//                          argument (a Unary Operator), the other argument is
//                          being provided implicitly from the object itself.

      Function<Integer, Integer> addOne = (x) -> x + 1;
      Function<Integer, Double> divideBy10 = (x) -> x / 10.0;
      addOne.andThen(divideBy10);
//              ▲
//              └──── Same thing here. Despite how we interact with it in Java,
//                    this completely honors the binary operation contract.
  }

}
