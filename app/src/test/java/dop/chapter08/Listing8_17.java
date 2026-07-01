package dop.chapter08;

public class Listing8_17 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.17
   * ───────────────────────────────────────────────────────
   * Adding default methods to support chaining
   * ───────────────────────────────────────────────────────
   */
  sealed interface Rule {
    // . . .
    default Rule and(Rule other) {
      return new And(this, other);
    }
    default Rule or(Rule other) {
      return new Or(this, other);
    }
  }








  record And(Rule a, Rule b) implements Rule {}
  record Or(Rule a, Rule b) implements Rule {}

}
