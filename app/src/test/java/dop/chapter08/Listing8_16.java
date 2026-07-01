package dop.chapter08;

public class Listing8_16 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.16
   * ───────────────────────────────────────────────────────
   * A few convenience static constructors
   * ───────────────────────────────────────────────────────
   */
  static Equals eq(Attribute attr, String val) {  //  ┐
    return new Equals(attr, val);                 //  │
  }                                               //  │◄── These wrap up the constructors and
  static Not not(Rule rule) {                     //  │    let us call them without the `new` ceremony
    return new Not(rule);                         //  │
  }                                               //  ┘








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }

  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}
  }

  record Equals(Attribute field, String value) implements Rule {}
  record Not(Rule rule) implements Rule {}

}
