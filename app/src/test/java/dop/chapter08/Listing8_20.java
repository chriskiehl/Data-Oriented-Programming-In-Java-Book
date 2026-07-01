package dop.chapter08;

import java.util.Arrays;

import static dop.chapter08.Listing8_20.Attribute.COUNTRY;
import static dop.chapter08.Listing8_20.Attribute.SEGMENT;
import static dop.chapter08.Listing8_20.Rule.And;
import static dop.chapter08.Listing8_20.Rule.Or;
import static dop.chapter08.Listing8_20.Rule.eq;

public class Listing8_20 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.20
   * ───────────────────────────────────────────────────────
   * defining new ways of interacting with the algebra
   * ───────────────────────────────────────────────────────
   */
  static Rule any(Rule a, Rule... rest) {
    return Arrays.stream(rest).reduce(a, Or::new);   //  ┐
  }                                                  //  │◄── We can create new rules by
  static Rule all(Rule a, Rule... rest) {            //  │    combining existing ones
    return Arrays.stream(rest).reduce(a, And::new);  //  ┘
  }

  void example() {
    // Despite the different APIs, these are equivalent to each other
    any(eq(COUNTRY, "US"), eq(COUNTRY, "FR"), eq(COUNTRY, "BE"));
    eq(COUNTRY, "NA").or(eq(COUNTRY, "FR")).or(eq(COUNTRY, "BE"));

    // And these, too!
    all(eq(COUNTRY, "US"), eq(SEGMENT, "Enterprise"));
    eq(COUNTRY, "US").and(eq(SEGMENT, "Enterprise"));
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }

  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}

    static Rule eq(Attribute field, String value) {
      return new Equals(field, value);
    }
    default Rule and(Rule other) {
      return new And(this, other);
    }
    default Rule or(Rule other) {
      return new Or(this, other);
    }
  }

}
