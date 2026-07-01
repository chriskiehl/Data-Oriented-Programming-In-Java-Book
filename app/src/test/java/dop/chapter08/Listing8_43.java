package dop.chapter08;

public class Listing8_43 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.43
   * ───────────────────────────────────────────────────────
   * Type safety! Hooray!
   * ───────────────────────────────────────────────────────
   */
  static Attr<SalesChannel> channel = new Attr<>(Attribute.CHANNEL); //  ┐
  static Attr<Region> region = new Attr<>(Attribute.REGION);         //  ┘
//                                                                   ◄── Statically defining instances for
//                                                                       each Attribute with the extra type
//                                                                       info we need
  // etc.


  void example() {
    // Now only valid values can be supplied to valid attributes!
    new Equals<>(channel, SalesChannel.Reseller);
    new Equals<> (region, Region.EMEA);


    // [NOPE] new Equals<>(channel, Region.EMEA)   [NOPE]
    //                        ▲              ▲
    //                        └──── If you try to mix invalid types, Java won’t even compile.
    //                              Pretty neat!
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }
  enum Region { LATAM, NA, EMEA /*...*/ }
  enum SalesChannel { Direct, Partner, Reseller /*...*/ }
  record Attr<A>(Attribute value) {}
  record Equals<A>(Attr<A> attribute, A value) {}

}
