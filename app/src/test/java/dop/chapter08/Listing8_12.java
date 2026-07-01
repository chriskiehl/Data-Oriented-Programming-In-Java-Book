package dop.chapter08;

public class Listing8_12 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.12
   * ───────────────────────────────────────────────────────
   * Refactoring to use an enum instead of wide-open String
   * ───────────────────────────────────────────────────────
   */
  enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL};
//     ▲
//     └──── Modeling all of the attributes as an enum

  // [OLD] record Equals(String attributeName, String value) {}
  
  record Equals(Attribute attribute, String value) {}  // NEW!
          //         ▲
          //         └──── Swapping out String for Attribute

}
