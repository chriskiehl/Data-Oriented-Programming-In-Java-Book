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

public class Listing2_37 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.37
     * ───────────────────────────────────────────────────────
     * Using the compact constructor to enforce invariants
     * ───────────────────────────────────────────────────────
     */
    record Person(String name, List<String> friends){
        Person {
            friends = List.copyOf(friends);  // ◄── The copyOf method produces a new UnmodifiableList
                                             //     behind the scenes. This guarantees we always see a
                                             //     value-based view of the collection, rather than a mutable one.
        }
    }
}
