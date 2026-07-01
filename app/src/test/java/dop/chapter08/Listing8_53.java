package dop.chapter08;

import static dop.chapter08.Listing8_53.CountryCode.BE;
import static dop.chapter08.Listing8_53.CountryCode.FR;
import static dop.chapter08.Listing8_53.CountryCode.US;
import static dop.chapter08.Listing8_53.Region.LATAM;
import static dop.chapter08.Listing8_53.Rule.contains;
import static dop.chapter08.Listing8_53.Rule.eq;
import static dop.chapter08.Listing8_53.Rule.not;
import static dop.chapter08.Listing8_53.Segment.Public;

public class Listing8_53 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.53
   * ───────────────────────────────────────────────────────
   * Rules as data
   * ───────────────────────────────────────────────────────
   */
  void example() {
    contains(country, US, BE, FR)
        .and(eq(segment, Public).or(not(eq(region, LATAM))));
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }
  enum Region { LATAM, NA, EMEA /*...*/ }
  enum CountryCode { AC, AD, AE, AU, BE, FR, US, /*...*/ }
  enum Segment { Enterprise, Strategic, Existing, Public /*...*/ }
  enum SalesChannel { Direct, Partner, Reseller /*...*/ }
  record Sector(String value) {}
  record Account(Region region, CountryCode country, Sector sector, Segment segment, SalesChannel channel) {}
  record Attr<A>(Attribute attribute, java.util.function.Function<Account, A> getter) {}

  static Attr<CountryCode> country = new Attr<>(Attribute.COUNTRY, Account::country);
  static Attr<Segment> segment = new Attr<>(Attribute.SEGMENT, Account::segment);
  static Attr<Region> region = new Attr<>(Attribute.REGION, Account::region);

  sealed interface Rule {
    record Equals<A>(Attr<A> field, A value) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}

    static <A> Equals<A> eq(Attr<A> field, A value) {
      return new Equals<>(field, value);
    }
    static Not not(Rule rule) {
      return new Not(rule);
    }
    static <A> Rule contains(Attr<A> field, A opt1, A... rest) {
      return eq(field, opt1);
    }
    default Rule or(Rule b) {
      return new Or(this, b);
    }
    default Rule and(Rule b) {
      return new And(this, b);
    }
  }

}
