package dop.chapter07;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Listing7_27 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.27
   * ───────────────────────────────────────────────────────
   * An exhaustive assertion against every possible row
   * ───────────────────────────────────────────────────────
   */
  void example() {
    for (RawData row : everyPossibleRow()) {
      assertTrue(someInvariant(row));
    }
  }








  record RawData(String id) {}

  static RawData[] everyPossibleRow() {
    return new RawData[0];
  }

  static boolean someInvariant(RawData row) {
    return true;
  }

}
