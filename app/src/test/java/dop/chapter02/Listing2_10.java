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
public class Listing2_10 {

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
     * Listing 2.10
     * ───────────────────────────────────────────────────────
     * We can convert Java collections from identity objects
     * into value objects using the Unmodifiable suite of wrapper
     * types. Super useful!
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // We start out with an identity object.
        List<String> letters = new ArrayList<>();
        letters.add("A");
        letters.add("B");  // we can freely mutate it as we go
        letters.add("...");
        letters.add("Z");
        // However here we convert it to a **Value Object** by wrapping it
        // up as an unmodifiableList.
        List<String> immutableLetters = Collections.unmodifiableList(letters);
        // Any attempts to continue treating it as an identity are
        // met with an error. The Unmodifiable wrapping classes turn off
        // every API method that might cause a mutation.
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            immutableLetters.add("1"); // If you're in an IDE like IntelliJ
                                       // it should highlight this as an invalid
                                       // method call.
        });
    }
}
