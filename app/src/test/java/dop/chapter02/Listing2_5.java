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

import static org.junit.jupiter.api.Assertions.*;

public class Listing2_5 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.5
     * ───────────────────────────────────────────────────────
     * Value classes should be compared exclusively by their
     * state. The identities will vary, but that's OK.
     * They're irrelevant to values!
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        Integer a = Integer.valueOf(3042);
        Integer b = Integer.valueOf(3042);
        // a and b are the same value, even though they’re
        // assigned to different objects, and different
        // variables, and have unique object identities.
        assertNotSame(a, b);        // false
        assertEquals(a, b);      // true
    }
}
