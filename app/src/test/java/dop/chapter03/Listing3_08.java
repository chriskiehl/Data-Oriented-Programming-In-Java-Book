package dop.chapter03;

import org.junit.jupiter.api.Test;

public class Listing3_08 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 3.8
   * ───────────────────────────────────────────────────────
   * If we keep digging on that Sample ID, we can eventually
   * get to the bottom of it. IDs are one of my favorite things
   * to harp on because they so often contain hidden domain
   * information. That domain info ends up lost behind a generic
   * "anything goes here" type like "String".
   * ───────────────────────────────────────────────────────
   */
  @Test
  void example() {
    /**
     * CuringMethod:                        ◄────┐
     *     One of: (AIR, UV, HEAT)               │ Totally new domain information!
     *                                           │
     * SampleNumber:                        ◄────┘
     *     A positive integer (0 inclusve)
     *
     * SampleID:
     *     The pair: (CuringMethod, SampleNumber)  ◄─── Now *this* is a precise definition!
     *     Globally unique.
     *
     * (other fields elided for brevity)
     */
  }

}
