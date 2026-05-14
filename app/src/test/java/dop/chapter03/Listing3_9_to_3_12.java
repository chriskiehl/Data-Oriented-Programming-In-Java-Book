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
public class Listing3_9_to_3_12 {




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.9 to 3.12
     * ───────────────────────────────────────────────────────
     * What happens in so much Java code is that we forget to take
     * what we've learned about our domain and *actually* express it
     * in our code. Instead, it just lives in our heads. The code is
     * left to fend for itself. That leads to situations where our
     * model be used to create invalid data (rather than guide us
     * towards the right path).
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        /**                                                              ─┐
         * An individual observation tracking how water contact           │ What we often try to do in our
         * angles on a surface changes as oil curing progresses by day    │ code is put what we know about the
         *                                                                │ domain into the javadoc and variable
         * @param sampleId                                                │ names.
         *     A pair (CuringMethod, positive int) represented            │
         *     as a String of the form "{curingMethod}-{number}"          │ It looks professional, and it's better
         *     CuringMethod will be one of {AIR, UV, HEAT}                │ than nothing, but it has a lot of
         * @param daysElapsed                                             │ limitations. The biggest one being that
         *     A positive integer (0..n)                                  │ it depends on people both reading it (which
         * @param contactAngle                                            │ is rare) and honoring it (also rare).
         *    Water contact angle measured in degrees                     │
         *    ranging from 0.0 (inclusive) to 360 (non-inclusive)         ┘ Nothing enforces it.
         */
        record Measurement(
                String sampleId,
                Integer daysElapsed,
                double contactAngleDegrees  //  ◄──┐ Here's an example of trying to use variable
        ) {}                                //     │ names to encode what something means (degrees)

        new Measurement(
                UUID.randomUUID().toString(),  //  ◄──┐ Despite those variable names and a bunch of
                -32,                           //     │ extensive doc strings, anybody can still march into
                9129.912                       //     │ our codebase and complete invalid data.
        );                                     //     │ This breaks every invariant we know our data to have!
    }
}
