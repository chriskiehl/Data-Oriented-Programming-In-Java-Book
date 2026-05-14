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
public class Listing3_8 {




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.8
     * ───────────────────────────────────────────────────────
     * If we keep digging on that Sample ID, we can eventually
     * get to the bottom of it. IDs are one of my favorite things
     * to harp on because they so often contain hidden domain
     * information. That domain info ends up lost behind a generic
     * "anything goes here" type like "String".
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        /**
         * CuringMethod:                        ◄────┐
         *     One of: (AIR, UV, HEAT)               │ Totally new domain information!
         *                                           │
         * SampleNumber:                        ◄────┘
         *     A positive integer (0 inclusve)
         *
         * SampleID:
         *     The pair: (CuringMethod, SampleNumber)  ◄─── Now *this* is a precise definition!
         *     Globally unique.
         *
         * (other fields elided for brevity)
         *
         */
    }
}
