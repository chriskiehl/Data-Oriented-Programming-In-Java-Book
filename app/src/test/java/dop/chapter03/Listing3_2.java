package dop.chapter03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


/**
 * Chapter 3 is about starting to explore the semantics
 * that govern the data within a domain. It looks at the
 * gaps that usually exist between what we "know" in our
 * heads about the things we're modeling, versus how much
 * of that knowledge actually ends up in the code (very
 * little).
 *
 * This chapter will give you the tools to see "through"
 * your programs into the underlying sets of values that
 * it denotes.
 */
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
