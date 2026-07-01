package dop.chapter08;

import java.util.Set;

import static dop.chapter08.Listing8_19.CountryCode.*;

public class Listing8_19 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.19
   * ───────────────────────────────────────────────────────
   * Can data handle this?
   * ───────────────────────────────────────────────────────
   */
  void example(Account account) {
    Set<CountryCode> allowed = Set.of(US, FR, BU);
    if (allowed.contains(account.country())) {
//       └────────────────────────────────────┘
//               ▲
//               └──── Can we use data to express complicated ideas like this?

      // ...
    }
  }








  enum CountryCode { US, FR, BU }
  record Account(CountryCode country) {}

}
