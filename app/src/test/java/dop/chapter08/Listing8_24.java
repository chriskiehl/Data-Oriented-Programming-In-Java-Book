package dop.chapter08;

import java.util.Arrays;

import static dop.chapter08.Listing8_24.Rule.And;
import static dop.chapter08.Listing8_24.Rule.Or;
import static dop.chapter08.Listing8_24.Rule.eq;

public class Listing8_24 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.24
   * ───────────────────────────────────────────────────────
   * The code so far
   * ───────────────────────────────────────────────────────
   */
  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}

    static Rule eq(Attribute field, String value) {
      return new Equals(field, value);
    }
    static Rule not(Rule rule) {
      return new Not(rule);
     }
    default Rule and(Rule other) {
      return new And(this, other);
    }
     default Rule or(Rule other) {
      return new Or(this, other);
    }
  }

  static Rule contains(Attribute field, String opt1, String... rest) {
    return Arrays.stream(rest)
        .map(value -> eq(field, value))
        .reduce(eq(field, opt1), Rule::or);
  }

  static Rule any(Rule a, Rule... rest) {
    return Arrays.stream(rest).reduce(a, Or::new);
  }
  static Rule all(Rule a, Rule... rest) {
    return Arrays.stream(rest).reduce(a, And::new);
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }

}
