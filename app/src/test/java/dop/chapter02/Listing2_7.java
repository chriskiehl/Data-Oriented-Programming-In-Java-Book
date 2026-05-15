package dop.chapter02;

import org.junit.jupiter.api.Test;

import java.util.*;

public class Listing2_7 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.7
     * ───────────────────────────────────────────────────────
     * Building new Value objects from existing value objects
     * ───────────────────────────────────────────────────────
     */
    class Ball {
        private Point pos;             //  ◄──┐ New values can be built on top of existing ones
        private Vector vector;        //      │
        public Ball(Point pos, Vector vector) {
            this.pos = pos;
            this.vector = vector;
        }
        public Point getPosition() { return this.pos; }            //  │ They just have to follow all the same rules. They must be
        public Vector getVector() { return this.vector; }          //  │ immutable and defined by their state.
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Ball ball = (Ball) o;
            return Objects.equals(pos, ball.pos) && Objects.equals(getVector(), ball.getVector());
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos, getVector());
        }
    }



    public static class Vector {
        Double x;
        Double y;
        public Vector(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        public Double x(){return x;}
        public Double y(){return y;}

        @Override
        public boolean equals(Object o) {
            // The default equals method includes an object check
            // but this is irrelevant.
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            // The state is what determines the equality!
            Listing2_6.Vector vector = (Listing2_6.Vector) o;
            return Objects.equals(x, vector.x)
                    && Objects.equals(y, vector.y);
        }
        @Override
        public int hashCode() {return Objects.hash(x, y);}
    }


    // We don't show the implementation of this in the book.
    static class Point {
        Double x;
        Double y;


    }
}
