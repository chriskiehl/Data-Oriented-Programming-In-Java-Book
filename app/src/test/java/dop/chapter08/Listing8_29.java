package dop.chapter08;

public class Listing8_29 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.29
   * ───────────────────────────────────────────────────────
   * Following the data types
   * ───────────────────────────────────────────────────────
   */
  static boolean interpret(Rule rule, Account account) {
    return switch (rule) {
      case Rule.Equals(Attribute field, String value) ->
          get(account, field).equals(value);
//                           ┌─┐
      case Rule.Not(Rule r) ->!interpret(r, account);
//                           └─┘
//                            ▲
//                            └──── We just follow what the data types denote. “Not” becomes “!” in Java
      case Rule.And(Rule a, Rule b) ->
//                             “And” denotes logical &&
//                             ┌──┐
          interpret(a, account) && interpret(b, account);
//                             └──┘
      case Rule.Or(Rule a, Rule b) ->
//                             “Or” denotes logical ||
//                             ┌──┐
          interpret(a, account) || interpret(b, account);
//                             └──┘
    };
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }
  record Account() {}

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
