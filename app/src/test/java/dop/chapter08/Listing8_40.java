package dop.chapter08;

import static dop.chapter08.Listing8_40.Attribute.COUNTRY;
import static dop.chapter08.Listing8_40.Attribute.REGION;

public class Listing8_40 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.40
   * ───────────────────────────────────────────────────────
   * Whoops!
   * ───────────────────────────────────────────────────────
   */
  void example() {
    // Whoops! These are both totally invalid pairings
    // despite being allowed by the type system
    new Equals<>(COUNTRY, Region.EMEA);
    new Equals<>(REGION, CountryCode.US);
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }
  enum Region { LATAM, NA, EMEA /*...*/ }
  enum CountryCode { AC, AD, AE, AU, BE, FR, US, /*...*/ }
  record Equals<A>(Attribute attribute, A value) {}

}
