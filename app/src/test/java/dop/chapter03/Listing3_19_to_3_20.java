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
public class Listing3_19_to_3_20 {




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.19 to 3.20
     * ───────────────────────────────────────────────────────
     * We can apply this idea to all the data in our domain.
     * We can make what we're talking about unambiguous.
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        // Here's the Degrees implementation from the previous listing.
        record Degrees(double value) {
            Degrees {
                if (!(Double.compare(value, 0.0) >= 0
                        && Double.compare(value, 360.0) < 0)) {
                    throw new IllegalArgumentException("Invalid angle");
                }
            }
        }

        //         ┌ Here's a new data type that captures the fact that
        //         │ we're only talking about integers >= 0
        //         ▼
        record PositiveInt(int value) {
            PositiveInt {
                if (value < 0) {
                    throw new IllegalArgumentException(
                            "Hey! No negatives allowed!"
                    );
                }
            }
        }
        // We can stick this new type on our data model.
        record Measurement(
                String sampleId,
                PositiveInt daysElapsed,  // ◄────┐ These changes have a compounding effect on our
                Degrees contactAngle      //      │ understanding. Now at a glance, we can tell
        ) {}                              //      │ exactly what these Measurement attributes *mean*.
    }
}
