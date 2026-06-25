package dop.chapter07;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing7_29 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.29
   * ───────────────────────────────────────────────────────
   * How we’ll make assertions about our algebra
   * ───────────────────────────────────────────────────────
   */
  void example() {
    for (RawData a : everyPossibleRow()) {
      for (RawData b : everyPossibleRow()) {
        // we can make assertions about the behavior of our
        // algebra 'for all' possible states
        assertEquals(
            add(a, b),
            /*???*/__/*???*/
//                    ▲
//                    └──── Of course, we don’t know what those properties are
//                          yet, but we’re getting closer
        );
      }
    }
  }








  record RawData(String id) {}

  static RawData[] everyPossibleRow() {
    return new RawData[0];
  }

  static RawData add(RawData a, RawData b) {
    return a;
  }

  RawData __;

}
