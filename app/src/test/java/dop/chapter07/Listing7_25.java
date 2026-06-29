package dop.chapter07;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing7_25 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.25
   * ───────────────────────────────────────────────────────
   * asserting that an invariant holds for all possible values of an integer
   * ───────────────────────────────────────────────────────
   */
  void example() {
    for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++) {
//           ▲
//           └──── Our main design phrase: "For all possible Integers,
//                 it holds that..."
        assertEquals(i + i, 2 * i);
//      ▲
//      └──── Interesting, this law holds even as we overflow and wrap
//            around, which is pretty neat
    }
  }

}
