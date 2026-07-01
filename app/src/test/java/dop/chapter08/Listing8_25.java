package dop.chapter08;

public class Listing8_25 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.25
   * ───────────────────────────────────────────────────────
   * a Interpreting the data
   * ───────────────────────────────────────────────────────
   */
  static /*???*/__/*???*/ interpret(Rule rule, Account account) {
    //           ▲
    //           └──── We get to decide what the evaluation of our data produces
    return switch (rule) {
        case Rule.Eq(Attribute field, String value) -> /*???*/___/*???*/;   //  ┐
        case Rule.Not(Rule r)                       -> /*???*/___/*???*/;   //  │◄── As well as what each of its
        case Rule.And(Rule a, Rule b)               -> /*???*/___/*???*/;   //  │    data types mean
        case Rule.Or(Rule a, Rule b)                -> /*???*/___/*???*/;   //  ┘
    };
  }








  record __() {}
  static __ ___;

  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }
  record Account() {}

  sealed interface Rule {
    record Eq(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}
  }

}
