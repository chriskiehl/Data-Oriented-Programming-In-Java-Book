package dop.chapter12;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Listing12_5 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.5
     * ───────────────────────────────────────────────────────
     * Driving tests with data makes it easy to scale them up
     * ───────────────────────────────────────────────────────
     */
    void example() {
        Set<Row> accounts = Stream.generate(this::makeRow)
            // Every test can double as a load test when you generate your data
            // rather than hard code it! This is one of the coolest things about
            // driving your tests with data.
            .limit(2_500_000)
            .collect(toSet());
        // rest of the test
    }














    record Row(){}
    Row makeRow() {
        return null;
    }
    record Account(){}
}

