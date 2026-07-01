package dop.chapter08;

import static java.lang.String.format;

public class Listing8_54 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.54
   * ───────────────────────────────────────────────────────
   * Generating documentation automatically
   * ───────────────────────────────────────────────────────
   */
  static String document(Rule rule) {
    return switch (rule) {
      case Rule.Equals(var field, var value) -> format("%s = %s", field, value);
      case Rule.Not(Rule r) -> format("not (%s)", document(r));
      case Rule.Or(Rule a, Rule b) -> format("(%s or %s)", document(a), document(b));
      case Rule.And(Rule a, Rule b) -> format("(%s and %s)", document(a), document(b));
    };
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }

  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}
  }

}
