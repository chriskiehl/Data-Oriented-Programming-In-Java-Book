package dop.chapter08;

public class Listing8_14 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.14
   * ───────────────────────────────────────────────────────
   * The finished algebra
   * ───────────────────────────────────────────────────────
   */
  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }

}
