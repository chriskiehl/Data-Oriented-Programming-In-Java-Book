package dop.chapter02;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Listing2_11 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.11
     * ───────────────────────────────────────────────────────
     * As of Java 10, each of the major collection interfaces
     * exposes unmodifiable constructors! This makes it super
     * easy to create collections which act like value objects.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // Creating value objects.
        Set<Integer> a = Set.of(1, 2, 3, 4);
        List<Integer> b = List.of(1,2,3,4);
        Map<Integer, String> c = Map.of(1, "One", 2, "Two");
    }
}
