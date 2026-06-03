package dop.chapter03;

import org.junit.jupiter.api.Test;

public class Listing3_25_to_3_26 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 3.25
   * ───────────────────────────────────────────────────────
   * Back to the drawing board. What is it that we're trying to
   * represent? Let's look at what we know
   * ───────────────────────────────────────────────────────
   */
  @Test
  void example() {
    //       ┌─ Revisiting what we know about the domain
    //       │  independent of our code
    /**      ▼
     * CuringMethod:
     *     One of: (AIR, UV, HEAT)
     *
     * SampleID:
     *     The pair: (CuringMethod, positive integer (0 inclusive))
     *     Globally unique.
     */

    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.26
     * ───────────────────────────────────────────────────────
     * We can capture the meaning of Sample ID
     * ───────────────────────────────────────────────────────
     */
    // A sample ID isn't a string (despite the fact that it might be
    // serialized that way when it enters our system). The sample ID
    // is made up of multiple pieces of information. Each has its own
    // constraints and things that make it *it*.
    record PositiveInt(int value){
        // constructor omitted for brevity
    }

    enum CuringMethod {HEAT, AIR, UV}  // this is important info! It's these three things
                                       // *and nothing else* (a very important idea in modeling).

    // We can combine these into a refined representation for SampleID.
    record SampleId(
        CuringMethod method,
        PositiveInt sampleNum
    ) {
    // (Empty!)
    //   ▲
    //   └── Check out that the body of sample ID is now empty. We don't have
    //       to validate anything here. It's entirely described (and made safe) by
    //       the data types which it's built upon.
    }

    // With this, our code no longer "forgets" its meaning.
    // Everything is well typed and descriptive.
    CuringMethod method = new SampleId(CuringMethod.HEAT, new PositiveInt(1)).method();
  }

}
