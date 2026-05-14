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
public class Listing2_47_and_2_48 {

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
     * Listings 2.47 & 2.48
     * ───────────────────────────────────────────────────────
     * Some record niceties: you can define them wherever you
     * need them.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        record Person(String name){}
        // Note! Here just for completeness of example.
        // Pretend it does some fancy graph traversing
        BiFunction<Person, List<Person>, Integer> friendGraph = (person, people) -> {
            return 0;
        };

        List<Person> people = List.of(
            new Person("Bob"),
            new Person("Joe"),
            // ...
            new Person("Mary")

        );
        // (As with all the listings. We're only defining this as a lambda
        // so everything stays within its particular listing)
        Supplier<Optional<Person>> mostPopular = () -> {
            //       ┌ Records can be defined directly in a method body!
            //       │ (We've actually seen this a ton so far)
            //       │ This is a huge improvement over tuples or lists.
            //       ▼
            record Popularity(Person person, Integer totalFriends){};
            return people.stream()
                    .map(person -> new Popularity(
                            person,
                            friendGraph.apply(person, people))
                    )
                    .sorted(Comparator.comparingDouble(Popularity::totalFriends).reversed())
                    .map(Popularity::person)
                    .findFirst();
        };
    }
}
