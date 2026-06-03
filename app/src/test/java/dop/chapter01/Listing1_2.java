package dop.chapter01;

public class Listing1_2 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 1.2
   * ───────────────────────────────────────────────────────
   * This example is all about the ambiguity that most of our
   * representations carry with them. Checkout this attribute
   * we've called ID. What goes there? What does the data type
   * of String communicate to us as we read the code? Nothing!
   * String could be any infinite number of things.
   * ───────────────────────────────────────────────────────
   */
  static class AmbiguousRepresentationExample {
    String id; // ◄──┐ DoP is largely just the act of noticing
               //    │ that code like this is extremely vague.
  }

}
