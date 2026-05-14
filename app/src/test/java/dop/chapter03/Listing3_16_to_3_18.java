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
public class Listing3_16_to_3_18 {




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.16 through 3.18
     * ───────────────────────────────────────────────────────
     * The problem with the design in the previous listing is that
     * it is missing type information. Inside of the measurement
     * class we just had "naked" values like double and string.
     * Those primitive types can't enforce what they are -- because
     * they can be anything! We need types that capture the ideas
     * in our domain.
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        // Degrees is a core idea in our domain. It has a semantics.
        // A set of rules which make it, *it*.
        record Degrees(double value) {
            Degrees {
                // These are the same checks from listing 3.13, but now
                // used to guard our domain specific type
                if (!(Double.compare(value, 0.0) >= 0
                        && Double.compare(value, 360.0) < 0)) {
                    throw new IllegalArgumentException("Invalid angle");
                }
            }
        }
        // We can refactor the measurement data type to use
        // our new Degree type.
        record Measurement(
                String sampleId,
                Integer daysElapsed,
                Degrees contactAngle  // Nice!
        ) {}

        // And this yields something really cool.
        // The code no longer "forgets" what it is.
        Measurement measurement = new Measurement("HEAT-01", 12, new Degrees(108.2));
        Degrees angle = measurement.contactAngle();
        //  ▲
        //  └─  Look at this! We still know exactly what it is.
        //      Previously, we'd end up with a plain double that could
        //      be confused for anything.

        // (Note: here just as a placeholder to make the example work)
        BiFunction<Degrees, Degrees, Degrees> minus = (a, b) -> new Degrees(a.value() - b.value());

        // Let's look at that same transform from the previous listing, but
        // now using our new type.
        List<Measurement> measurements = List.of(
                new Measurement("UV-1", 1, new Degrees(46.24)),
                new Measurement("UV-1", 4, new Degrees(47.02)),
                // ...
                new Measurement("UV-2", 30, new Degrees(86.42))
        );

        //                 ┌─── A No more guesswork. The types are unambiguous
        //                 ▼
        Map<String, List<Degrees>> bySampleId = measurements.stream()
                .collect(groupingBy(Measurement::sampleId,
                        mapping(Measurement::contactAngle, Collectors.toList())));

        //     ┌─── Even as we transform and reshape the data
        //     ▼    we don't lose track of what it is were dealing with.
        List<Degrees> totalChanges = bySampleId.values()
                .stream()
                .map(x ->   minus.apply(x.getLast(), x.getFirst()))
                .toList();//  ▲
                          //  └─ Check this out. We're doing math on Degrees. That might
                          //     produce things that aren't valid degrees. The data type forces
                          //     us to consider these cases. Even if we don't it still watches
                          //     our back and will raise an error if we unknowingly drift away
                          //     from our domain of degrees.
    }
}
