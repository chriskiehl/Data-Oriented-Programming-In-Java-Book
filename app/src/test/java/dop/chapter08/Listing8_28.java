package dop.chapter08;

public class Listing8_28 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.28
   * ───────────────────────────────────────────────────────
   * a basic interpreter
   * ───────────────────────────────────────────────────────
   */
  static boolean interpret(Rule rule, Account account) {
    return switch (rule) {
      case Rule.Equals(Attribute field, String value) ->
//       ┌───────────────────────────────────┐
          get(account, field).equals(value);
//       └───────────────────────────────────┘
//                       ▲
//                       └──── We can now evaluate and equality statement expressed as data!
      case Rule.Not(Rule r)         -> /*???*/__/*???*/;
      case Rule.And(Rule a, Rule b) -> /*???*/__/*???*/;
      case Rule.Or(Rule a, Rule b)  -> /*???*/__/*???*/;
    };
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }
  record Account() {}
  static boolean __;
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
