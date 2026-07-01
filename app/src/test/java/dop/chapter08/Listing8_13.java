package dop.chapter08;

public class Listing8_13 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.13
   * ───────────────────────────────────────────────────────
   * An algebra appears!
   * ───────────────────────────────────────────────────────
   */
  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }

}
