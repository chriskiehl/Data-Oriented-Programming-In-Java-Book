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
public class Listing3_21_to_3_25 {




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.21 through 3.25
     * ───────────────────────────────────────────────────────
     * The most important part of this process is making sure we
     * don't accidentally slip into creating types "mechanically".
     * We want to remain thoughtful about what we're communicating
     * about the system. We want to make that our representation
     * captures the core ideas of what we're modeling.
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

        //  This is from the previous listing as well
        record PositiveInt(int value) {
            PositiveInt {
                if (value < 0) {
                    throw new IllegalArgumentException(
                            "Hey! No negatives allowed!"
                    );
                }
            }
        }

        //         ┌ This is the next logical data type to introduce, but... does
        //         │ it really capture what it means to be a Sample ID in our domain?
        //         ▼
        record SampleId(String value) {
            SampleId {
                if (!value.matches("(HEAT|AIR|UV)-\\d+")) {                 // │ This is all great validation. It
                    throw new IllegalArgumentException(                     // │ enforces what we know about the shape
                        "Must honor the form {CuringMethod}-{number}" +     // │ of the IDs inside of that String.
                        "Where CuringMethod is one of (HEAT, AIR, UV), " +  // │ However, there are some problems.
                        "and number is any positive integer"
                    );
                }
            }
        }

        record Measurement(
                SampleId sampleId,        // ◄────┐ What's wrong with this modeling?
                PositiveInt daysElapsed,  //      │ Let's take it for a spin and see how it feels.
                Degrees contactAngle
        ) {}

        // What if we wanted to do something super basic, say, bucket all the
        // measurements by their curing method.
        List<Measurement> measurements = List.of(); // (we don't need any items for the example to work)
        measurements.stream()
            .collect(groupingBy(m -> m.sampleId()/* ??? */));
        //                                           ▲
        //                                           │ Gah! We're back in that world where we "forget"
        //                                           │ what our code is. We know the shape of the string
        //                                           │ during validation in the constructor, but out here
        //                                           │ it's "just" another string.
        //                                           └─

        measurements.stream()
                .collect(groupingBy(
                        m -> m.sampleId().value().split("-")[0]
                )); //                             ▲
                    //                             └─ We are guaranteed that the string will be in a known shape, so
                    //                                we *could* "safely" access its individual pieces ("safely" here
                    //                                used very loosely and with disregard for potential future change)

        //         ┌ One option would be to steal some ideas from OOP and "hide" the internal
        //         │ details behind public methods.
        //         ▼
        record SampleIdV2(String value) {
            SampleIdV2 {
                if (!value.matches("(HEAT|AIR|UV)-\\d+")) {
                    throw new IllegalArgumentException(
                            "Must honor the form {CuringMethod}-{number}" +
                            "Where CuringMethod is one of (HEAT, AIR, UV), " +
                            "and number is any positive integer"
                    );
                }
            }
            public String curingMethod() {
                return this.value().split("-")[0];  // This gives the curing method without leaking "how"
            }

            public String sampleNumber() {
                return this.value().split("-")[1]; // ditto for the sample number.
            }
        }

        // This feels like progress, but we can again do the very simple gut check
        // of just seeing what happens when we poke the data type.
        String method = new SampleIdV2("HEAT-1").curingMethod();
        // ▲
        // └─ Ugh! We're back to just a plain string disconnected from its domain.
    }
}
