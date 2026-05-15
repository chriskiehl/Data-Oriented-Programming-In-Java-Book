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

public class Listing2_40_and_2_41 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.40
     * ───────────────────────────────────────────────────────
     * Creating immutable list types
     * ───────────────────────────────────────────────────────
     */
    record ImmutableList<A>(List<A> items) {
        ImmutableList {
            items = Collections.unmodifiableList(items);  // ◄── We can give ourselves a semantically meaningful wrapper type in just a few lines of code.
        }

        public static <A> ImmutableList<A> of(A... items) {    //   ◄──┐ And because records are just classes behind the
            return new ImmutableList<>(List.of(items));        //      │ scenes (more or less), we can give ourselves some
        }                                                      //      │ nice Quality of Life features that simplify usage.
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.41
     * ───────────────────────────────────────────────────────
     * Updating the person record
     * ───────────────────────────────────────────────────────
     */
    record Person(
            String name,
            ImmutableList<String> friends // ◄──┐ Now there is no way to accidentally provide the
            //        │ wrong kind of collection. We guide everyone who
            //        │ uses this class towards the right path
    ){}


    void example() {
        new Person(
            "Bob",
            ImmutableList.of("Jane", "Joe")
            //            ▲
            //            └───── We have to provide an ImmutableList. Anything else would fail type-checking.
        );
    }

}
