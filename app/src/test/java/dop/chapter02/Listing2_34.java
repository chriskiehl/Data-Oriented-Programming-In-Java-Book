package dop.chapter02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Chapter 2 explores the details of what it means
 * to model data "as data." We explore the different
 * kinds of objects we can create in Java (identity vs
 * value) and the effects that they have on our code.
 */
public class Listing2_34 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.7
     * ───────────────────────────────────────────────────────
     * We can make new value classes on top of existing ones!
     * ───────────────────────────────────────────────────────
     */
    static class Vector {
        Double x;
        Double y;
        //  ▲
        //  └────── There's *technically* one more thing
        //          we'd need to add here to really make
        //          this a value, but we'll come back to it later.
        public Vector(Double x, Double y) {
            this.x = x;
            this.y = y;
        }

        public Double x(){return x;}
        public Double y(){return y;}
        //            ▲
        //            └───── Getters are A-OK on value classes
        //
        //      ┌───── But note that there are no setters!
        //      ▼                   Values don't change!
        // (No setters here!)

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



    /**
     * ───────────────────────────────────────────────────────
     * Listings 2.34
     * ───────────────────────────────────────────────────────
     * Heavy emphasis on “broadly speaking”, “it depends,” and
     * “just my opinion” for the following!
     *
     * Generally speaking, the more "plain value" or "math-y"
     * something is, the more it benefits from defining the methods
     * on the value class.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        class Vector {
            double x,y;

            public Vector(double x, double y) {
                this.x = x;
                this.y = y;
            }

            // Defining all the methods on the value class
            // makes it nice to interact with when dealing
            // with operations that need to stack together.
            public Vector scale(double amount) {
                return new Vector(x*amount, y*amount);
            }
            public Vector subtract(Vector other) {
                return new Vector(x-other.x, y-other.y);
            }
            public Double magnitude() {
                return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            }
        }

        // For example:
        double result = new Vector(2.0, 9.1)
                .scale(10.0)
                .subtract(new Vector(1.0, 1.0))
                .magnitude();
    }
}
