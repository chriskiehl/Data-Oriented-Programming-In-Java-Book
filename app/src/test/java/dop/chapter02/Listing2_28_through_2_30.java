package dop.chapter02;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class Listing2_28_through_2_30 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.28
     * ───────────────────────────────────────────────────────
     * Another way of modeling immutable successions of values
     * ───────────────────────────────────────────────────────
     */
    @Data  // Lombok for brevity.
    @AllArgsConstructor
    static class Ball {
        private Point pos;
        private Vector vector;
        // constructor, getters, etc.

        public Ball step(float elapsedSeconds) {
            //        ▲
            //        └───── Note that this method is very different from the object-oriented one despite living on the class
            double dx = this.vector.x * elapsedSeconds;
            double dy = this.vector.y * elapsedSeconds;
            //                 ▲
            //                 └───── It’s allowed to read from the instance, but not allowed to modify
            return new Ball(
            //          ▲
            //          └───── It can only produce new data as output
                new Point(this.pos.x + dx, this.pos.y + dy),
                this.vector
            );
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.29
     * ───────────────────────────────────────────────────────
     * Fluent APIs for free
     * ───────────────────────────────────────────────────────
     */
    void listing2_29() {
        float dt = 1.0f;
        Ball ball = new Ball(new Point(0.0, 0.0), new Vector(1.0, 0.0));
        Ball updated = ball
                .step(dt)      //   ◄──┐ Fluent-style API’s “for free.”
                .step(dt)      //      │
                .step(dt);     //      │

        assertNotSame(updated, ball);    // ◄── But make no mistake, nothing is being modified.
        assertNotEquals(updated, ball);  //     Each function computes a new value.
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.30
     * ───────────────────────────────────────────────────────
     * Chaining method calls without a fluent API
     * ───────────────────────────────────────────────────────
     */
    @Test
    void listing2_30() {
        Float dt = 1.0f;
        Ball ball = new Ball(new Point(0.0, 0.0), new Vector(1.0, 0.0));

        step(step(step(ball, dt), dt), dt);        // ◄── We have to chain calls “by hand” by explicitly feeding the output of one into the input of another

        Function.<Ball>identity()              //   ◄──┐ Or we have to pull in some additional scaffolding to
                .andThen(b -> step(b, dt))     //      │ enable chaining. It’s not bad once you’re used to it,
                .andThen(b -> step(b, dt))     //      │ but it’s undeniably clunky by comparison
                .andThen(b -> step(b, dt))     //      │
                .apply(ball);                  //      │
    }


    // (from Figure 2.5)
    static Ball step(Ball current, float elapsedSeconds) {
        double dx = current.vector.x * elapsedSeconds;
        double dy = current.vector.y * elapsedSeconds;

        return new Ball(
                new Point(current.pos.x + dx, current.pos.y + dy),
                current.vector
        );
    }










    @Data
    @AllArgsConstructor
    static class Point {
        Double x;
        Double y;
    }

    @Data
    @AllArgsConstructor
    static class Vector {
        Double x;
        Double y;
    }
}