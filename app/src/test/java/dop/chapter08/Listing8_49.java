package dop.chapter08;

import java.util.function.Function;

import static java.lang.String.format;

public class Listing8_49 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.49
   * ───────────────────────────────────────────────────────
   * Finish up the equality implementation
   * ───────────────────────────────────────────────────────
   */
  static Result interpret(Account account, Rule rule) {
    return switch (rule) {
      case Rule.Equals(var attr, var value) -> {
        var found = attr.getter().apply(account);
//          ▲
//          └──── First we lookup the value inside of the account.
//                Note that we use var again as a placeholder since
//                we don’t know the type
        boolean result = found.equals(value);
//              ▲
//              └──── Then the equality check. This is guaranteed to
//                    be safe thanks to all the information we put
//                    into the type system
        yield new Result(result,
            format("%s=%s", attr.attribute(), value),  //  ┐
            format("%s=%s", attr.attribute(), found)); //  ┘◄── And then just attaching
                                                       //       the provenance info as usual.
      }
    };
  }








  enum Attribute { Region, Country, Sector, Segment, Channel }
  enum Region { LATAM, NA, EMEA /*...*/ }
  enum CountryCode { AC, AD, AE, AU, BE, FR, US, /*...*/ }
  enum Segment { Enterprise, Strategic, Existing, Public /*...*/ }
  enum SalesChannel { Direct, Partner, Reseller /*...*/ }
  record Sector(String value) {}
  record Attr<A>(Attribute attribute, Function<Account, A> getter) {}
  record Account(Region region, CountryCode country, Sector sector, Segment segment, SalesChannel channel) {}
  record Result(boolean matched, String expected, String found) {}

  sealed interface Rule {
    record Equals<A>(Attr<A> attr, A value) implements Rule {}
  }

}
