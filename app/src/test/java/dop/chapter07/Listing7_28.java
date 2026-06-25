package dop.chapter07;

public class Listing7_28 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.28
   * ───────────────────────────────────────────────────────
   * Combining the same set of states
   * ───────────────────────────────────────────────────────
   */
  void example() {
    // These two loops allow us to explore every
    // single pairing of possible states
    for (RawData a : everyPossibleRow()) {
      for (RawData b : everyPossibleRow()) {
        // assert something about a and b   // ◄── And make assertions about them!
      }
    }
  }








  record RawData(String id) {}

  static RawData[] everyPossibleRow() {
    return new RawData[0];
  }

}
