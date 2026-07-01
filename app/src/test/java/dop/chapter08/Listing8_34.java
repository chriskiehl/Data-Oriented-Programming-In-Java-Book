package dop.chapter08;

import static dop.chapter08.Listing8_34.Attribute.Country;
import static dop.chapter08.Listing8_34.Rule.eq;

public class Listing8_34 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.34
   * ───────────────────────────────────────────────────────
   * JUnit style reporting on why a rule matched (or didn’t!)
   * ───────────────────────────────────────────────────────
   */
  void example() {
    Rule rule = eq(Country, US);
//             ▲
//             └──── Nothing has changed in how we declare the rules.
//                   It's still just plain data.
    interpret(rule, new Account(/*...*/ CountryCode.BE /*...*/));
//       ▲
//       └──── It's our evaluation that has been enriched. We've imbued it with
//             the ability to track the provenance of why it's making decisions.

    /*
    ┌────────OUTPUT───────────┐
    Result[
      matched=false,              We get a nice JUnit style report of what
      expected=(country=US),      we expected versus what we found.
      found=(country=BE)]
    └────────────────────────┘
    */
  }








  static String US = "US";
  enum Attribute { Country }
  enum CountryCode { BE }
  record Account(Object... values){}
  record Result(boolean matched, String expected, String found) {}

  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}

    static Equals eq(Attribute field, String value) {
      return new Equals(field, value);
    }
  }

  static Result interpret(Rule rule, Account account) {
    return new Result(false, "", "");
  }

}
