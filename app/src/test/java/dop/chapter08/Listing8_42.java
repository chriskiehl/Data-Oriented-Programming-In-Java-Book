package dop.chapter08;

public class Listing8_42 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.42
   * ───────────────────────────────────────────────────────
   * Creating a wrapper type with extra type information
   * ───────────────────────────────────────────────────────
   */

//      ┌───────┐
  record Attr<A>(Attribute value) {}
//      └───────┘
//          We can do this, though!








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }

}
