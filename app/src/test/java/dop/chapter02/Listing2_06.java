package dop.chapter02;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

public class Listing2_06 {

  class Listing2_06_inner {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.6
     * ───────────────────────────────────────────────────────
     * The holder doesn’t matter. If the state is the same, the value is the same
     * ───────────────────────────────────────────────────────
     */
    void example() {
      Vector a = new Vector(1.0, 3.0);
      Vector b = new Vector(1.0, 3.0);
      Vector c = new Vector(1.0, 3.0);

      assertTrue(
          a.equals(b)                                            //  ◄──┐ All of these are equivalent because they
          && a.equals(c)                                         //     │ represent the same value
          && new Vector(1.0, 3.0).equals(a)                      //     │
          && new Vector(1.0, 3.0).equals(b)                      //     │
          && a.equals(new Vector(1.0, 3.0))                      //     │
          && new Vector(1.0, 3.0).equals(new Vector(1.0, 3.0))   //     │
          && a.equals(b) && a.equals(c));                        //     │
    }
  }

  static class Vector {
    Double x;
    Double y;
    public Vector(Double x, Double y) {
      this.x = x;
      this.y = y;
    }

    public Double x() { return x; }
    public Double y() { return y; }

    @Override
    public boolean equals(Object o) {
      // The default equals method includes an object check
      // but this is irrelevant.
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      // The state is what determines the equality!
      Vector vector = (Vector) o;
      return Objects.equals(x, vector.x)
          && Objects.equals(y, vector.y);
    }
    @Override
    public int hashCode() {return Objects.hash(x, y);}
  }

}
