package dop.chapter02;

import org.junit.jupiter.api.Test;

public class Listing2_4 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.4
   * ───────────────────────────────────────────────────────
   * Trying to use identity on an object that's modeling a
   * value can lead to very strange results!
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    Integer x = Integer.valueOf(128);  // We’re creating different objects, which
    Integer y = Integer.valueOf(128);  // means unique object identities, right?

    System.out.println(x == y); // So far it seems that way. This will show false .

    // But, hang on...
    x = Integer.valueOf(127);  // What happens if we pick a slightly different
    y = Integer.valueOf(127);  // number...?

    System.out.println(x == y);  // What the heck?! They're now the same!
  }

}
