package dop.chapter08;

public class Listing8_47 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.47
   * ───────────────────────────────────────────────────────
   * Comparing the change from hard coded to generic
   * ───────────────────────────────────────────────────────
   */
  void example() {
    /*
    case Equals(Attribute attr, String value) -> ...
                                   ▲
                                   └──── The old hard coded version that only worked with strings

    case Equals<?> eq -> ...
                ▲
                └──── The new generic version that works with any type. The ?
                      is a wildcard standing for “any type.”

    */
  }

}
