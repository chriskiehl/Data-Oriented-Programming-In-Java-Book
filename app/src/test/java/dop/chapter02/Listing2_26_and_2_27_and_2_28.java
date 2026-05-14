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
public class Listing2_26_and_2_27_and_2_28 {

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
     * Listings 2.26 & 2.27 & 2.28
     * ───────────────────────────────────────────────────────
     * A new programming style emerges naturally when you model
     * identity and change using data. It emerges because there's
     * a fundamental constraint: data doesn't change. We can't mutate
     * an object in place, so we've got to do something else.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // Redesigning Person as a Value Class
        final class Person {        // ◄─┐
            final String name;      //   │ Everything is now final
            final int age;          //   │

            public Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            // public void haveBirthday() {  // ◄─┐ Note that this method *can't* live here
            //    this.age++;                //   │ anymore. Values don't change.
            // }
        }

        // The big question is: Uhh... what's next?
        // Where do we go from here? If everything is final, how
        // do we represent change...?
        final Person person = new Person("Bob", 32);

        // with more data!
        final Person older     = new Person("Bob", 33);  // ◄─┐ Nothing is mutated, but we can still see that
        final Person evenOlder = new Person("Bob", 34);  //   │ they’re all clearly “about” the same identity.
        final Person cruelTime = new Person("Bob", 35);  //   │ There's a steady progression of change.
    }
}
