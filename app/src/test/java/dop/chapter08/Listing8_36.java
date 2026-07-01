package dop.chapter08;

import static dop.chapter08.Listing8_36.Attribute.COUNTRY;
import static dop.chapter08.Listing8_36.Attribute.REGION;
import static dop.chapter08.Listing8_36.Attribute.SEGMENT;
import static dop.chapter08.Listing8_36.Rule.contains;
import static dop.chapter08.Listing8_36.Rule.eq;
import static dop.chapter08.Listing8_36.Rule.not;

public class Listing8_36 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.36
   * ───────────────────────────────────────────────────────
   * Full information on why we matched or didn’t
   * ───────────────────────────────────────────────────────
   */
  void example() {
    Account account = new Account(
        new AccountId("1234"),
        Region.EMEA,
        CountryCode.BE,
        new Sector("Retail"),
        Segment.STRATEGIC,
        SalesChannel.Reseller
    );

    Rule rule = (not(eq(COUNTRY, "US")).and(eq(REGION, "AMER")))
        .or(contains(SEGMENT, "Enterprise", "Strategic"));

    interpret(rule, account);

    /*
    ┌────────────────────────OUTPUT─────────────────────────────────┐
    Result[
       matched=false,
       expected=((not(country=US) AND Region=AMER)
                   OR (Segment=Enterprise OR Segment=Strategic)),
          found=((country=BE AND Region=EMEA)
                   OR (Segment=Enterprise OR Segment=Strategic))]
   └────────────────────────────────────────────────────────────────┘

    */
  }








  record AccountId(String value) {}
  enum Attribute { COUNTRY, REGION, SEGMENT }
  enum Region { LATAM, NA, EMEA /*...*/ }
  enum CountryCode { AC, AD, AE, AU, BE, FR, US, /*...*/ }
  enum Segment { Enterprise, STRATEGIC, Existing, Public /*...*/ }
  enum SalesChannel { Direct, Partner, Reseller /*...*/ }
  record Sector(String value) {}
  record Account(
      AccountId accountId,
      Region region,
      CountryCode country,
      Sector sector,
      Segment segment,
      SalesChannel channel
  ) {}
  record Result(boolean matched, String expected, String found) {}

  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}

    static Equals eq(Attribute field, String value) {
      return new Equals(field, value);
    }
    static Not not(Rule rule) {
      return new Not(rule);
    }
    static Rule contains(Attribute field, String opt1, String... rest) {
      return eq(field, opt1);
    }
    default Rule and(Rule other) {
      return new And(this, other);
    }
    default Rule or(Rule other) {
      return new Or(this, other);
    }
  }

  static Result interpret(Rule rule, Account account) {
    return new Result(false, "", "");
  }

}
