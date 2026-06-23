package dop.chapter03;

import org.junit.jupiter.api.Test;

public class Listing3_04_through_3_06 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 3.4 through 3.6
   * ───────────────────────────────────────────────────────
   * This is a fun listing, because it gets to a very important
   * part of the software development process: stepping away from
   * the software. We're going to just sketch out what we know
   * about the stuff which makes up our domain.
   * ───────────────────────────────────────────────────────
   */
  @Test
  void example() {
    // You could do this with pen and paper, or on a whiteboard,
    // or in a comment like we do here. The important part is
    // giving yourself some space to think about what you're
    // trying to model before you let the quirks and limitations
    // of a programming language start influencing your thinking.

    /**
     * SampleID:
     *     Alpha-numeric + special characters  ◄───┐ As a first stab, this isn't bad, but it's
     *     Globally unique                         │ still a bit ambiguous. Do the characters matter at all?
     *
     * Days Elapsed:
     *     A positive integer (0 inclusive).   ◄────┐ The person taking the measurements (me) is
     *     Not strictly daily. Will be Sparse.      │ lazy. "days" is a hand-wavy unit of time, but about
     *                                              │ the highest fidelity I could muster.
     *
     * Contact Angle:                               ◄────┐
     *     degrees. Half open interval [0.0, 360)        │ Now we know what those numbers in the JSON
     *     Precision of 100th of a degree                │ are meant to be!
     *
     */
  }

}
