package dop.chapter03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


public class Listing3_2 {




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.2
     * ───────────────────────────────────────────────────────
     * Our first stab at representing this in Java might be a
     * mechanical translation. text-like things in the json
     * becomes Strings in Java. Numbers in the Json become ints
     * or doubles in Java.
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        record Measurement(
            String sampleId,      //  ◄───┐
            int daysElapsed,      //      │ A very JSON-like modeling
            double contactAngle   //      │
        ) {}
    }
}
