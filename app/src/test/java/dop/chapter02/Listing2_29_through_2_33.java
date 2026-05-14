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
public class Listing2_29_through_2_33 {

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
     * Listings 2.29 - 2.33
     * ───────────────────────────────────────────────────────
     * Heads up! We combine quite a few of the listings here so
     * that we can reuse the object definitions and explore the
     * various styles next to each other.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        final class Person {
            final String name;
            final int age;

            public Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
            public String name() {return name;}
            public int age() {return age;}

            // This method represents one of many possible ways
            // of creating data that models change. This creates
            // a *succession* of values. The inputs directly lead
            // to the outputs.
            public Person haveBirthday() {              // ◄─┐ Note that this method *can* live here! It doesn't
                return new Person(name, age + 1);  //   │ mutate anything. It computes a *new* value.
            }
            // public void haveBirthday() {     ◄─┐ Compare the above with this one, which needs an
            //    this.age++;                     │ Identity class in order to work
            // }
        }
        // We're defining this as a lambda just for sake of keeping
        // all the definitions local to each listing. In practice, it would
        // be a normal method.
        //
        // This is another way we could create a succession of values.
        // Just like the one we defined on the Value Class, this one takes
        // input and uses it to compute the next state.
        Function<Person, Person> haveBirthday = (p) -> {
            return new Person(p.name(), p.age() + 1);
        };

        // These two approaches lead to very different programming styles.
        // When methods are on the Value class, they give us a fluent API for "free".
        // For instance, we can give our person a bunch of birthdays back to back.
        Person example01 = new Person("Bob", 30)
                .haveBirthday()  // each method returns a *new* value object
                .haveBirthday()  // which makes chaining calls a breeze.
                .haveBirthday(); // Note that nothing was mutated here!
        System.out.println(example01.age());

        // Standalone methods have their own call flavor.
        Person example02 = haveBirthday
                .andThen(haveBirthday)
                .andThen(haveBirthday)
                .apply(new Person("Bob", 30));
        // One is not better than the other.
        // Both are useful in different situations.
    }
}
