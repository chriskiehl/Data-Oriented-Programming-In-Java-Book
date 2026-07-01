package dop.chapter08;

import static dop.chapter08.Listing8_39.Attribute.COUNTRY;
import static dop.chapter08.Listing8_39.Attribute.REGION;

public class Listing8_39 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.39
   * ───────────────────────────────────────────────────────
   * Using real types!
   * ───────────────────────────────────────────────────────
   */
  void example() {
    new Equals<>(COUNTRY, CountryCode.US);
    new Equals<>(REGION, Region.EMEA);
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }
  enum Region { LATAM, NA, EMEA /*...*/ }
  enum CountryCode { AC, AD, AE, AU, BE, FR, US, /*...*/ }
  record Equals<A>(Attribute attribute, A value) {}

}
