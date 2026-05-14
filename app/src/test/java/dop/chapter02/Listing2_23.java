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
public class Listing2_23 {

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
     * Listings 2.23
     * ───────────────────────────────────────────────────────
     * The really fun part of data-oriented programming is learning
     * to see the world differently. Data is everywhere! It can
     * be anything. It can even be abstract!
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // These two value classes model decisions that our
        // system could make (for a refresher on these types, checkout
        // the Listings from chapter 01).
        class ReattemptLater {
            LocalDateTime scheduledAt;
            // getters,final,equals, and hashcode omitted
        };
        // You might have to squint a bit to "see" these as being
        // data just like we would a sales or weather report. They
        // tick all the boxes, though!
        class RetryImmediately {
            LocalDateTime scheduledAt;
            int attempts;
            // getters,final,equals, and hashcode omitted
        };
    }
}
