package dop.chapter02;

import org.junit.jupiter.api.Test;

public class Listing2_14_and_2_15 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 2.14
   * ───────────────────────────────────────────────────────
   * Code is how we communicate. If we don't make it say exactly
   * what we mean, then it's up to our readers to guess.
   *
   * Value classes look like any other identity class. It's very
   * easy for a newcomer to misunderstand that we want this class
   * to represent a value
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    class Point {
      private double x;
      private double y;
    }
    class Vector {
      private double x;         //   ◄──┐ The lack of setters hints towards our intentions,
      private double y;         //      │ but the code doesn’t enforce its value semantics
      // constructor, getters,
      // equals, hashcode
    }

    class Ball {                //      │
      private Point pos;        //      │
      private Vector vector;    //      │
      // constructor, getters,
      // equals, hashcode

      public void moveTo(Point newPos) { // Reasonable additions for identity objects,
        this.pos = newPos;               // but invalid additions for value objects
      }
    }
  }

}
