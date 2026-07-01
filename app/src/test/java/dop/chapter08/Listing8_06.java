package dop.chapter08;

import static dop.chapter08.Listing8_06.CountryCode.AU;
import static dop.chapter08.Listing8_06.CountryCode.BE;
import static dop.chapter08.Listing8_06.CountryCode.FR;

import java.util.Optional;
import java.util.Set;

public class Listing8_06 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.6
   * ───────────────────────────────────────────────────────
   * Our first rule! Only dozens more to go after this one…
   * ───────────────────────────────────────────────────────
   */
  /**
    * Rule #1
    * All accounts in EMEA excluding those in Belgium, Austria,
    * and France belong to SalesOrg=111
    */
  static Optional<SalesOrgId> ruleOrg1234(Account account) {
    if (account.region().equals(Region.EMEA)) {
      Set<CountryCode> excluded = Set.of(BE, AU, FR);
      if (!excluded.contains(account.country())) {
        return Optional.of(new SalesOrgId("111"));  //  ◄───┐
      } else {                    //                        │
        return Optional.empty();  //  ◄─────────────────────│ This code is okay(ish) but it's kind
      }                           //                        │ of amateurish. Maybe we could make it "better"?
    } else {                      //                        │
      return Optional.empty();    //  ◄─────────────────────┘
    }
  }








  record SalesOrgId(String value) {}
  record AccountId(String value) {}
  enum Region { LATAM, NA, EMEA /*...*/ }
  enum CountryCode { AC, AD, AE, AU, BE, FR, US, /*...*/ }

  record Account(
      AccountId accountId,
      Region region,
      CountryCode country
  ) {}

}
