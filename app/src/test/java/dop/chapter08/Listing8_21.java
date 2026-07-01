package dop.chapter08;

import java.util.Arrays;

import static dop.chapter08.Listing8_21.Rule.eq;

public class Listing8_21 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.21
   * ───────────────────────────────────────────────────────
   * Adding more convenient ways of interacting with the algebra
   * ───────────────────────────────────────────────────────
   */
  static Rule contains(Attribute field, String opt1, String... rest) {
//                                             ▲       ▲        ▲
//                                             └──── We can tune the interfaces into our algebra however we want

    return Arrays.stream(rest)                //  ┐
        .map(value -> eq(field, value))       //  │◄── Inside, we transform the input into
        .reduce(eq(field, opt1), Rule::or);   //  ┘    data our algebra understands
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
    default Rule or(Rule other) {
      return new Or(this, other);
    }
  }

}
