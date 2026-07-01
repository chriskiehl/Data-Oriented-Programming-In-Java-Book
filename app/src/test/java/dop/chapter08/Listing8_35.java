package dop.chapter08;

import static java.lang.String.format;

public class Listing8_35 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.35
   * ───────────────────────────────────────────────────────
   * finishing up the interpreter with AND, OR, and NOT
   * ───────────────────────────────────────────────────────
   */
  static Result interpret(Rule rule, Account account) {
//              ▲
//              └──── Not just needs to invert whatever it finds
    return switch (rule) {
      case Rule.Eq(Attribute field, String value) -> ELIDED;
      case Rule.Not(Rule r) -> {
          Result res = interpret(r, account);
//               ▲
//               └──── So we evaluate the rule
          yield new Result(
              !res.matched(),
//            ▲
//            └──── Then store the inverse on the result
//                  (note the ! on the boolean)
              format("not(%s)", res.expected),        //  ┐
              res.found());                           //  ┘◄── And lastly format our
                                                      //       provenance information
      }
      case Rule.And(Rule rule1, Rule rule2) -> {               //  ┐
          Result a = interpret(rule1, account);                //  │
          Result b = interpret(rule2, account);                //  │
          yield new Result(a.matched() && b.matched(),         //  │
              format("(%s AND %s)", a.expected, b.expected),   //  │
              format("(%s AND %s)", a.found, b.found));        //  │  AND and OR work exactly the same. Evaluate
      }                                                        //  │  each expression and record the findings.
      case Rule.Or(Rule rule1, Rule rule2) -> {                //  │
          Result a = interpret(rule1, account);                //  │
          Result b = interpret(rule2, account);                //  │
          yield new Result(a.matched() || b.matched(),         //  │
              format("(%s OR %s)", a.expected, b.expected),    //  │
              format("(%s OR %s)", a.found, b.found));         //  ┘

      }
    };
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }
  record Account() {}
  record Result(boolean matched, String expected, String found) {}
  static Result ELIDED;

  sealed interface Rule {
    record Eq(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}
  }

}
