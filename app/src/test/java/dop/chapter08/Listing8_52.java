package dop.chapter08;

import java.util.Set;

import static dop.chapter08.Listing8_52.CountryCode.AU;
import static dop.chapter08.Listing8_52.CountryCode.BE;
import static dop.chapter08.Listing8_52.CountryCode.FR;
import static dop.chapter08.Listing8_52.Region.LATAM;
import static dop.chapter08.Listing8_52.Segment.PUBLIC;

public class Listing8_52 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.52
   * ───────────────────────────────────────────────────────
   * Rules as code
   * ───────────────────────────────────────────────────────
   */
  void example(Account account) {
    Set<CountryCode> included = Set.of(BE, AU, FR);         //  ┐
    boolean result = included.contains(account.country())   //  │
        && (account.segment().equals(PUBLIC)                //  │◄── How would we extract this and turn it
        || !account.region().equals(LATAM));                //  │    into documentation?
    /*...*/                                                 //  ┘
  }








  enum Region { LATAM, NA, EMEA /*...*/ }
  enum CountryCode { AC, AD, AE, AU, BE, FR, US, /*...*/ }
  enum Segment { Enterprise, Strategic, Existing, PUBLIC /*...*/ }
  record Account(Region region, CountryCode country, Segment segment) {}

}
