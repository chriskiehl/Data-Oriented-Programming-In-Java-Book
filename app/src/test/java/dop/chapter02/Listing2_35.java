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

public class Listing2_35 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.35
     * ───────────────────────────────────────────────────────
     * Be careful with the standard Java collections
     * ───────────────────────────────────────────────────────
     */
    record Person(
            String name,
            List<String> friends  // ◄── The value-ness of this entire data-type depends on
                                  //     the people who use it remembering to give an unmodifiable
                                  //     version of the List interface.
    ){}
}
