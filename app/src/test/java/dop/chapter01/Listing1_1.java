package dop.chapter01;

import java.util.Objects;

import org.junit.jupiter.api.Test;

public class Listing1_1 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 1.1
   * ───────────────────────────────────────────────────────
   * <p>
   * Here's an example of how we might traditionally model
   * data "as data" using a Java object.
   * ───────────────────────────────────────────────────────
   */
  @Test
  void example() {
    class Point {
      private final double x;   // ◄── The object is entirely defined by these attributes.
      private final double y;   // ◄── It has no behaviors. It has no hidden state. It's "just" data.



      // Note: The below is not included in the book's listing for brevity.
      // However, it's included here for completeness. You need
      // all this stuff in order to get an object in Java to behave like a
      // value rather than an identity (we'll explore that idea in detail
      // in chapter 02!)
      public Point(double x, double y) {
        this.x = x;
        this.y = y;
      }

      // Equality is very important when modeling data, but that's a topic
      // for chapter 02 ^_^
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(x, point.x) == 0 && Double.compare(y, point.y) == 0;
      }

      @Override
      public int hashCode() {
        return Objects.hash(x, y);
      }

      // Any accessors we define manually will be done without the
      // Java Bean style `get` prefix. This is so that our transition to
      // records is easy.
      public double x() {
        return x;
      }

      public double y() {
        return y;
      }
    }
  }

}
