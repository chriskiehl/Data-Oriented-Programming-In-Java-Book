package dop.chapter02;

import java.util.Objects;

import lombok.AllArgsConstructor;

public class Listing2_24_through_2_27 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.24
   * ───────────────────────────────────────────────────────
   * An immutable Ball
   * ───────────────────────────────────────────────────────
   */
  @AllArgsConstructor
  static class Ball {
    private Point pos;
    private Vector vector;
    // constructor, getters, etc.
  }



  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.25
   * ───────────────────────────────────────────────────────
   * An immutable Ball
   * ───────────────────────────────────────────────────────
   */
  void listing2_25() {
    //          ┌─ Once we create this is "stuck" as this value forever
    //          ▼
    final Ball ball = new Ball(new Point(0.0, 0.0), new Vector(0.0, 0.0));
    // uhh...
  }



  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.26
   * ───────────────────────────────────────────────────────
   * manually creating “frames” that represent our ball over time
   * ───────────────────────────────────────────────────────
   */
  void listing2_26() {
    // Nothing is mutated, but we can still see the steady progression of change.
    // They’re all clearly “about” the same identity, even though we don’t tie them
    // together with a single object or variable
    final Ball frame1 = new Ball(new Point(0.0, 0.0), new Vector(1.0, 0.0));
    final Ball frame2 = new Ball(new Point(1.0, 0.0), new Vector(1.0, 0.0));
    final Ball frame3 = new Ball(new Point(2.0, 0.0), new Vector(1.0, 0.0));
    final Ball frame4 = new Ball(new Point(3.0, 0.0), new Vector(1.0, 0.0));
    final Ball frame5 = new Ball(new Point(4.0, 0.0), new Vector(1.0, 0.0));
  }



  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.27
   * ───────────────────────────────────────────────────────
   * Computing the next state from the current one
   * ───────────────────────────────────────────────────────
   */
  void listing2_27() {
    float simulatedDuration = 1.0f;
    Ball ball = new Ball(new Point(0.0, 0.0), new Vector(1.0, 0.0)); // ◄──┐ We model the ball's movement through
    Ball updated = step(ball, simulatedDuration);                    //    │ time by creating a succession of values. The
    // [out] Ball(position=(1.0, 0.0), vector=(1.0, 0.0));           //    │ previous state is used to compute the next,
    Ball updatedAgain = step(updated, simulatedDuration);            //    │ which is used to compute the one after that,
    // [out] Ball(position=(2.0, 0.0), vector=(1.0, 0.0));           //    │ and after that, and so on
  }



  /**
   * ───────────────────────────────────────────────────────
   * Figure 2.5
   * ───────────────────────────────────────────────────────
   * This code is shown as a Figure in the book rather than
   * a listing. It's shown here for completeness.
   * ───────────────────────────────────────────────────────
   */
  static Ball step(Ball current, float elapsedSeconds) {
    double dx = current.vector.x * elapsedSeconds;
    double dy = current.vector.y * elapsedSeconds;

    return new Ball(
        new Point(current.pos.x + dx, current.pos.y + dy),
        current.vector
    );
  }











  @AllArgsConstructor
  static class Point {
    Double x;
    Double y;
  }

  @AllArgsConstructor
  static class Vector {
    Double x;
    Double y;

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
