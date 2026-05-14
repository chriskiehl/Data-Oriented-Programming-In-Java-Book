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
public class Listing2_38 {

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
     * Listings 2.38 & 3.39
     * ───────────────────────────────────────────────────────
     * Things to watch our for: mutability is ever-present in Java!
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        record Person(
                String name,
                // Dangerous!
                List<String> friends  // ◄─┐ The value-ness of this entire data-type depends on the people who
        ){}                           //   │ use it remembering to give an unmodifiable version of the List interface.

        // We can "undo" the value-ness of records just like
        // we could with the value classes we created by hand.
        List<String> friends = Arrays.asList("Joe", "Jane");
        Person person = new Person("Bob", friends);

        System.out.println(person);
        //[out]  Person2[name=Bob, friends=[Joe, Jane]]

        friends.set(0, "Billy");
        System.out.println(person);
        // [out] Person2[name=Bob, friends=[Billy, Jane]]     #D
    }
}
