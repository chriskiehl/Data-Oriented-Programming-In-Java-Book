package dop.chapter02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class Listing2_12 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.12
   * ───────────────────────────────────────────────────────
   * Value objects are special. They can be used in places where
   * identity objects would be unsafe or potentially lead to
   * very unexpected behaviors.
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    Map<List<String>, String> map = new HashMap<>();

    map.put(List.of("Bob", "Joe"), "Bob and Joe"); // This is perfectly safe, because it’s a value
    System.out.println(map.get(List.of("Bob", "Joe"))); // ◄─┐ We can query it back out using a totally
    // [out] "Bob and Joe"                                   │ different value object, because their state
    //                                                       │ and equality is the same (i.e. they’re the same)
    //

    List<String> mutable = new ArrayList<>(); // However, with an identity-based setup...
    map.put(mutable, "TBD");
    mutable.add("Bob");                           // ◄─┐
    System.out.println(map.get(mutable) == null); //   │ We'll never see ol' Bob again
  }

}
