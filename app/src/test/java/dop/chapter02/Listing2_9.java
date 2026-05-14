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
public class Listing2_9 {

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



    // This section just sets up a bunch of fake interfaces
    // for our "API". It's ignorable.
    // ──────────────────────────────────────────────────────────┐
    interface Result {}

    interface QueryResponse {                                  //│
        List<Result> items();                                  //│
        String nextToken();                                    //│
    }

                                              //│
    interface DefinitelyNotDynamoDb {                          //│
        QueryResponse query(String request);                   //│
        QueryResponse query(String request, String nextToken); //│
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.9
     * ───────────────────────────────────────────────────────
     * Heads up! This listing looks very different from the one
     * in the book. Unlike plain text on a page, which doesn't
     * need to compile, Java does need that text to be compilable.
     * So, we have to do a lot of interface shimming in order to
     * get the (mostly) made up API in the example to compile.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
                                        //│
        class Customer {                                           //│
            Customer(Result result) {}                             //|
        };                                                         //│
                                                          //│
                                                                   //│
        // Ditto here.                                               │
        // These are just placeholders for compilation               │
        String request = null;                                     //│
        DefinitelyNotDynamoDb database = null;                     //│
        // ──────────────────────────────────────────────────────────┘

        // Here's the bulk of the listing. It's exploring how we often
        // use the Java collections as Identity Objects rather than
        // value objects.
        Supplier<List<Customer>> demo = () -> {
            List<Customer> customers = new ArrayList<>(); // ◄─────  Creating the collection gives us a
                                                          //         useful identity we can carry around
                                                          //         as we do work

            QueryResponse response = database.query(request);
            for (Result result : response.items()) {
                customers.add(new Customer(result));  //     ◄─────  Which we do here
            }

            while (response.nextToken() != null) {
                response = database.query(request, response.nextToken());
                for (Result result : response.items()) {
                    customers.add(new Customer(result));  // ◄─────  And then over-and-over again down here
                }
            }
            return customers;  //  ◄───────────────────────────────  Before finally returning it here
        };
    }
}
