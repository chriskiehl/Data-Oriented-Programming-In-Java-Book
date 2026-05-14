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
public class Listing2_24_and_2_25 {

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
     * Listings 2.24, 2.25
     * ───────────────────────────────────────────────────────
     * Object Identity gives continuity to all the changes
     * we make to the attributes inside the object throughout
     * the course of our program. Even if we change everything.
     * The object remains. It's still *that* object.
     *
     * This identity influences *how* we program. It nudges us to
     * towards a particular style of writing methods. Towards a
     * certain way of exposing behaviors. Towards a way of putting
     * our code together as a whole.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        class Person {
            String name;
            int age;

            public Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setAge(int age) {
                this.age = age;
            }

            // Note the special kind of method we can write when
            // modeling with Identity classes. This method takes
            // nothing and returns nothing. it exists only to mutate
            // the object in place.
            public void haveBirthday() {
                this.age++;
            }
        }

        // Identity Objects give continuity to changes
        // we make to their internal attributes over time.
        Person person = new Person("Bob", 32);
        person.setAge(33);
        person.setName("Bobbie");
        // Even if we change everything about the object, the obejct
        // itself remains the same. It's still the same object.
        person.setName("Robert");

        // Each time we change the object, its previous state is lost
        // to time. The holder remains, but what it held disappears.
        person.haveBirthday();
        person.haveBirthday();
        person.haveBirthday();
        // Down here the person is a new age. What they were before is overwritten
        System.out.println(person);
    }
}
