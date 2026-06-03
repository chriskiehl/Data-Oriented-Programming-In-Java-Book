package dop.chapter02;

import java.util.List;

public class Listing2_35 {
  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.35
   * ───────────────────────────────────────────────────────
   * Be careful with the standard Java collections
   * ───────────────────────────────────────────────────────
   */
  record Person(
    String name,
    List<String> friends  // ◄── The value-ness of this entire data-type depends on
                          //     the people who use it remembering to give an unmodifiable
                          //     version of the List interface.
  ) {}

}
