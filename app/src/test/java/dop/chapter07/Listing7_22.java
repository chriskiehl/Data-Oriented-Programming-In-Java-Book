package dop.chapter07;

import org.junit.jupiter.api.Assertions;

public class Listing7_22 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.22
   * ───────────────────────────────────────────────────────
   * Adding floating point numbers is not an associative binary operation
   * ───────────────────────────────────────────────────────
   */
  void example() {
    double a = 1.00001;
    double b = 1.00002;
    double c = 1.00003;

    Assertions.assertEquals( // FALSE!
//                     ▲
//                     └──── Dang you, floating point!
        (a + (b + c)),
        ((a + b) + c)
    );
  }

}
