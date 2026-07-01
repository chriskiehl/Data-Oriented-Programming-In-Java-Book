package dop.chapter08;

import static java.lang.String.format;

public class Listing8_33 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.33
   * ───────────────────────────────────────────────────────
   * Migrating our interpreter to the new Result type
   * ───────────────────────────────────────────────────────
   */
  static Result interpret(Rule rule, Account account) {
//         ▲
//         └──── Our new interpretation of the data returns a Result
//               type, rather than a plain Boolean
    return switch (rule) {
      case Rule.Equals(Attribute field, String value) -> {
        String found = get(account, field);              //  ┐
        boolean result = found.equals(value);            //  ┘◄── The main logic is still exactly
                                                         //       the same. We compare two strings.

        yield new Result(                    //  ┐
            result,                          //  │◄── But now we keep track of what those
            format("%s=%s", field, value),   //  │    comparisons were and whether they
            format("%s=%s", field, found));  //  ┘    matched what we expected
      }
      case Rule.Not(Rule r)         -> /*???*/TODO/*???*/;
      case Rule.And(Rule a, Rule b) -> /*???*/TODO/*???*/;
      case Rule.Or(Rule a, Rule b)  -> /*???*/TODO/*???*/;
    };
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }
  record Account() {}
  record Result(boolean matched, String expected, String found) {}

  static Result TODO;

  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}
  }

  static String get(Account account, Attribute attr) {
    return "";
  }

}
