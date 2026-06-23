package dop.chapter04;

import org.junit.jupiter.api.Test;

public class Listing4_09 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 4.9
   * ───────────────────────────────────────────────────────
   * Here's the messiness of the design process (which is both
   * expected, normal, and OK). We don't know where this concept
   * of `isCompleted` goes yet, but we *do* know from the previous
   * listing that it doesn't belong on `Step`.
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    record Step(String name /* (removed) */ ) {
    }      //                      ▲
    //                             └─ No more isCompleted here.
    //                                We have to figure out where it goes.
  }

}

