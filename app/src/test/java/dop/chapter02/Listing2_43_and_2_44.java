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
public class Listing2_43_and_2_44 {

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
     * Listings 2.43 & 2.44
     * ───────────────────────────────────────────────────────
     * Once again, we revisit the idea of representation. Can we
     * make our code communicate the constraints that live in our
     * heads? Yes! With another data type!
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        record ImmutableList<A>(List<A> items) {  // ◄── just like that, we introduce a new layer of
            ImmutableList {                       //     semantic precision into the code.
                items = Collections.unmodifiableList(items);
            }

            public static <A> ImmutableList<A> of(A... items) {
                return new ImmutableList<>(List.of(items));
            }
        }

        // Now we can use this new precise data type on our person model
        record Person(
                String name,
                ImmutableList<String> friends // Now there is no way to accidentally provide the wrong kind
        ){}                                   // of collection. It says exactly what it expects.


        Person person = new Person(
            "Bob",
            ImmutableList.of("Jane", "Joe") // And we HAVE to provide an ImmutableList here.
                                                   // anything else wouldn't compile. The code forces us
                                                   // to do the right thing.
        );
    }
}
