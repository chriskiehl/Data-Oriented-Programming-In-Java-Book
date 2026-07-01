package dop.chapter08;

import org.junit.jupiter.api.Test;

public class Listing8_01 {

  record AccountId(String value) {}
  enum Segment { Enterprise, Strategic, Existing, Public /*...*/ }
  enum SalesChannel { Direct, Partner, Reseller /*...*/ }
  enum Region { LATAM, NA, EMEA /*...*/ }
  enum CountryCode { AC, AD, AE, AU, BE, FR, US, /*...*/ }
  record Sector(String value) {}

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.1
   * ───────────────────────────────────────────────────────
   * This is the simplified view of an Account that we'll use
   * throughout the chapter.
   */
  @Test
  void example() {

    record Account(
        AccountId accountId,
        Region region,
        CountryCode country,
        Sector sector,
        Segment segment,
        SalesChannel channel
    ) {}
  }

}
