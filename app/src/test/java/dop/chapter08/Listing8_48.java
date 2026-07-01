package dop.chapter08;

import dop.chapter08.Listing8_48.Rule.Equals;

public class Listing8_48 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.48
   * ───────────────────────────────────────────────────────
   * Pattern matching without specifying concrete types
   * ───────────────────────────────────────────────────────
   */
  void example() {
    String example = switch (rule) {
//                   ┌───┐     ┌───┐
        case Equals(var attr, var value) -> __;
//                   └───┘     └───┘
//                     ▲         ▲
//                     └──── We can use record patterns even when we don’t know the types!

        default -> __;
    } ;
  }








  sealed interface Rule {
    record Equals(Listing8_37.Attribute field, String value) implements Rule {}
  }

  static Rule rule;
  String __;

}
