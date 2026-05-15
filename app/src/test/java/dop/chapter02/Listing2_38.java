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

public class Listing2_38 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.38
     * ───────────────────────────────────────────────────────
     * Anti-poison technology
     * ───────────────────────────────────────────────────────
     */
    void example() {
        List<String> friends = Arrays.asList("Joe", "Jane");
        Person person = new Person("Bob", friends);        // ◄── Inside our constructor, we’ve defended against this common mistake

        friends.set(0, "Billy");                           //   ◄──┐ Out here, they can freely mutate their collection
        friends.set(1, "Michael");                         //      │ as much as they want

        System.out.println(person);                        //   ◄──┐ But we remain an unchanging, immutable value.
        // [out] Person2[name=Bob, friends=[Joe, Jane]]    //      │
    }





    record Person(String name, List<String> friends){
        Person {
            friends = List.copyOf(friends);
        }
    }
}
