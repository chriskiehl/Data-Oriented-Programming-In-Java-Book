package dop.chapter02;

import java.util.List;

public class Listing2_37 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.37
   * ───────────────────────────────────────────────────────
   * Using the compact constructor to enforce invariants
   * ───────────────────────────────────────────────────────
   */
  record Person(String name, List<String> friends){
    Person {
      friends = List.copyOf(friends);  // ◄── The copyOf method produces a new UnmodifiableList
                                       //     behind the scenes. This guarantees we always see a
                                       //     value-based view of the collection, rather than a mutable one.
    }
  }

}
