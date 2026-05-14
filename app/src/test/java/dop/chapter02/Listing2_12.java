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
public class Listing2_12 {

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
     * Listing 2.12
     * ───────────────────────────────────────────────────────
     * Value objects are special. They can be used in places where
     * identity objects would be unsafe or potentially lead to
     * very unexpected behaviors.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        Map<List<String>, String> map = new HashMap<>();

        map.put(List.of("Bob", "Joe"), "Bob and Joe"); // This is perfectly safe, because it’s a value
        System.out.println(map.get(List.of("Bob", "Joe"))); // ◄─┐ We can query it back out using a totally
        // [out] "Bob and Joe"                                   │ different value object, because their state
        //                                                       │ and equality is the same (i.e. they’re the same)
        //

        List<String> mutable = new ArrayList<>(); // However, with an identity-based setup...
        map.put(mutable, "TBD");
        mutable.add("Bob");                           // ◄─┐
        System.out.println(map.get(mutable) == null); //   │ We'll never see ol' Bob again
    }
}
