package dop.chapter08;

import static dop.chapter08.Listing8_37.Attribute.COUNTRY;
import static dop.chapter08.Listing8_37.Attribute.REGION;
import static dop.chapter08.Listing8_37.Rule.eq;

public class Listing8_37 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.37
   * ───────────────────────────────────────────────────────
   * The incorrect states our data currently allows
   * ───────────────────────────────────────────────────────
   */
  void example() {
    eq(COUNTRY, "Nacho Country");
//                   ▲
//                   └──── until I’m elected and make some sweeping changes, this is not
//                         a valid country
    eq(REGION, "US");
//               ▲
//               └──── This is surely to be a frustrating error. "US" sure sounds
//                     like a region, but it’s completely invalid for the underlying enum.
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }

  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}

    static Equals eq(Attribute field, String value) {
      return new Equals(field, value);
    }
  }

}
