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
public class Listing3_26_to_3_27 {




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.26 through 3.27
     * ───────────────────────────────────────────────────────
     * Back to the drawing board. What is it that we're trying to
     * represent?
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        //       ┌─ Revisiting what we know about the domain
        //       │  independent of our code
        /**      ▼
         * CuringMethod:
         *     One of: (AIR, UV, HEAT)
         *
         * SampleID:
         *     The pair: (CuringMethod, positive integer (0 inclusive))
         *     Globally unique.
         */

        // A sample ID isn't a string (despite the fact that it might be
        // serialized that way on the way into our system). The sample ID
        // is made up of multiple pieces of information. Each has it's own
        // constraints and things that make it *it*.
        record PositiveInt(int value){
            // constructor omitted for brevity
        }

        enum CuringMethod {HEAT, AIR, UV}  // this is important info! It's these three things
                                           // *and nothing else* (a very important idea in modeling).

        // We can combine these into a refined representation for SampleID.
        record SampleId(
                CuringMethod method,
                PositiveInt sampleNum
        ) {
        // (Empty!)
        //   ▲
        //   └── Check out that the body of sample ID is now empty. We don't have
        //       to validate anything here. It's entirely described (and made safe) by
        //       the data types which it's built upon.
        }

        // With this, our code no longer "forgets" its meaning.
        // Everything is well typed and descriptive.
        CuringMethod method = new SampleId(CuringMethod.HEAT, new PositiveInt(1)).method();
    }
}
