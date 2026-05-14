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
public class Listing2_18 {

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
     * Listings 2.18
     * ───────────────────────────────────────────────────────
     * Mutability is infectious. All it takes is a single mutable
     * reference on our value class to drag it back into the land
     * of identities.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {

        final class Person {
            final String name;
            final Date birthday;  // ◄─┐ Mutability can be sneaking in Java.
                                  //   │ The assignment to this variable is final, but
                                  //   │ the object itself is mutable
            public Person(String name, Date birthday) {
                this.name = name;
                this.birthday = birthday;
            }

            public String name() {return name;}
            public Date birthday() {return birthday;}
        }

        // which means we can violate the value-ness of our value object
        final Person person = new Person("Bob", new Date());

        SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String before = isoFormatter.format(person.birthday());
        // anyone can punch into the mutable reference and change it
        person.birthday().setTime(new Date().getTime() + 123456789);
        String after = isoFormatter.format(person.birthday());
        System.out.println("Before: " + before);
        System.out.println("After: " + after);

        Assertions.assertNotEquals(before, after);  //  :(
    }
}
