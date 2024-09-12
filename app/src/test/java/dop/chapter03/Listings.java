package dop.chapter03;

import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Listings {


    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.1
     * ───────────────────────────────────────────────────────
     * We begin where all good software books begin: woodworking.
     * The best part of woodworking is the last step of applying the
     * finish. One of my favorite finishes is a type of drying oil
     * called Tung oil. I geek out over this stuff and have been
     * slowly collecting data around its drying behavior.
     *
     * In real life, nobody lets me talk about this because it's too
     * boring. But you're stuck reading my book, so I'm not going to
     * waste my one chance to drone on about my data on oil curing rates.
     *
     * It's interesting. I swear.
     * ───────────────────────────────────────────────────────
     */
    void listing_3_1() {
        // Here's our data in Json form. We're going to turn it
        // into Java and see what we can learn along the way.
        // {
        //    "sampleId”: "UV-1",
        //    "day": 3,             ◄─── Tung oil cures painfully slow. It's measured in full days.
        //    "contactAngle": 17.4  ◄──┐ We can't see it curing, but we can measure it! This is
        // }                           │ the angle that a droplet of water forms on the surface of the wood
    }



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
    void listing_3_2() {
        record Measurement(
            String sampleId,      //  ◄───┐
            int daysElapsed,      //      │ A very JSON-like modeling
            double contactAngle   //      │
        ) {}
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.3
     * ───────────────────────────────────────────────────────
     * Taking a closer look at our data. Each of these fields
     * is about something. It has a meaning within its domain.
     * Have we captured that meaning in our code? Let's look
     * at the data again.
     * ───────────────────────────────────────────────────────
     */
    void listing_3_3() {
        // {
        //    "sampleId”: "UV-1",
        //    "day": 3,       //  ◄─── What the heck kind of measurement is "days"?
        //    "contactAngle": 17.4  //  ◄─── 17.4... *what*?
        // }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.4 through 3.6
     * ───────────────────────────────────────────────────────
     * This is a fun listing, because it gets to a very important
     * part of the software development process: stepping away from
     * the software. We're going to just sketch out what we know
     * about the stuff which makes up our domain.
     * ───────────────────────────────────────────────────────
     */
    void listing_3_4_through_3_6() {
        // You could do this with pen and paper, or on a whiteboard,
        // or in a comment like we do here. The important part is
        // giving yourself some space to think about what you're
        // trying to model before you let the quirks and limitations
        // of a programming language start influencing your thinking.

        /**
         * SampleID:
         *     Alpha-numeric + special characters  ◄───┐ As a first stab, this isn't bad, but it's
         *     Globally unique                         │ still a bit ambiguous. Do the characters matter at all?
         *
         * Days Elapsed:
         *     A positive integer (0 inclusive).   ◄────┐ The person taking the measurements (me) is
         *     Not strictly daily. Will be Sparse.      │ lazy. "days" is a hand-wavy unit of time, but about
         *                                              │ the highest fidelity I could muster.
         *
         * Contact Angle:                               ◄────┐
         *     degrees. Half open interval [0.0, 360)        │ Now we know what those numbers in the JSON
         *     Precision of 100th of a degree                │ are meant to be!
         *
         */
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.7
     * ───────────────────────────────────────────────────────
     * Let's take another stab at understanding Sample ID
     * ───────────────────────────────────────────────────────
     */
    void listing_3_7() {

        /**
         * SampleID:
         *     A sequence of characters satisfying the regex /[A-Z]+-\d+/
         *     Globally unique.
         *       ▲
         *       └─── This is an improved statement. Now we know the *shape* of
         *            the IDs. Can we be more precise that this?
         *
         * (other fields elided for brevity)
         *
         */
    }




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
    void listing_3_8() {
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
    void listing_3_9_to_3_12() {
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



    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.13, 3.15
     * ───────────────────────────────────────────────────────
     * Our first stab at enforcing what our data means will be
     * a big improvement from where we started (we'll prevent bad
     * states from being created), but we'll still end up with
     * something that's a bit "off." It's "forgetful"
     * ───────────────────────────────────────────────────────
     */
    void listing_3_13_to_3_15() {

        record Measurement(
                String sampleId,
                Integer daysElapsed,
                double contactAngle
        ) {
            Measurement {
                if (!sampleId.matches("(HEAT|AIR|UV)-\\d+")) {             //  ◄────┐ Validating that the Sample ID
                    throw new IllegalArgumentException(                    //       │ is in the right shape
                            "Must honor the form {CuringMethod}-{number}" +
                                    "Where CuringMethod is one of (HEAT, AIR, UV), " +
                                    "and number is any positive integer"
                    );
                }
                if (daysElapsed < 0) {                                     //  ◄────┐ And validating the remaining
                    throw new IllegalArgumentException(                    //       │ constraints.
                            "Days elapsed cannot be less than 0!");        //       │
                }                                                          //       │
                if (!(Double.compare(contactAngle, 0.0) >= 0               //       │
                        && Double.compare(contactAngle, 360.0) < 0)) {     //       │
                    throw new IllegalArgumentException(
                            "Contact angle must be 0-360");
                }
            }
        }

        // This is a nice improvement.
        // If we try to create any data that's invalid for our domain
        // we'll get a helpful exception telling us where we went wrong.
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Measurement("1", -12, 360.2);
        });


        // However, this approach is "forgetful"
        // As soon as we're done with constructing the object, we forget
        // all the meaning we just ascribed to those values.

        // Here's what I mean:
        Measurement measurement = new Measurement("HEAT-01", 12, 108.2);
        double angle = measurement.contactAngle();
        //       ▲
        //       └─  this is back to being "just" a double.
        //
        // This "forgetfulness" of our data type has a massive effect
        // on how we reason about our programs.

        // For instance, as we read this code, lets notice how strongly
        // we can reason about what kind of thing we're dealing with.
        List<Measurement> measurements = List.of(  // ◄────┐
            new Measurement("UV-1", 1, 46.24),     //      │ Right here, we're probably 100% sure
            new Measurement("UV-1", 4, 47.02),     //      │ what this code means and represents.
            // ...
            new Measurement("UV-2", 30, 86.42)
        );

        Map<String, List<Double>> bySampleId = measurements.stream()  // ◄──┐
                .collect(groupingBy(                                  //    │ But then it gets transformed. We have to
                        Measurement::sampleId,                        //    │ pay close attention to understand that
                        mapping(Measurement::contactAngle,            //    │ those doubles in the map still represent Degrees
                                Collectors.toList())));

        // Comparing the first and last samples in each
        // group to see how much things changes while curing
        List<Double> totalChanges = bySampleId.values()  // ◄──┐ More transformations. More distance. Do these
                .stream()                                //    │ still represent degrees...?
                .map(x -> x.getLast() - x.getFirst())
                .toList();

        // computing some summary stats
        double averageChange = totalChanges.stream()
                .collect(averagingDouble(_angle -> _angle));
        // Note: the below is commented out simply because these methods
        //       don't exist in scope.
        //
        // By the time we’re down here, what these doubles represent is very blurry.
        // Still Degrees? The only way to understand this code is by working your way
        // backwards through the call stack to mentally track how the meaning of the
        // data changed as it was transformed.
        // double median = calculateMedian(totalChanges);
        // double p25 = percentile(totalChanges, 25);
        // double p75 = percentile(totalChanges, 75);
        // double p99 = percentile(totalChanges, 99);
    }





    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.16
     * ───────────────────────────────────────────────────────
     * The problem with the design in the previous listing is that
     * it is missing type information. Inside of the measurement
     * class we just had "naked" values like double and string.
     * Those primitive types can't enforce what they are -- because
     * they can be anything! We need types that capture the ideas
     * in our domain.
     * ───────────────────────────────────────────────────────
     */
    void listing_3_16() {
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
                .map(x -> minus.apply(x.getLast(), x.getFirst()))
                .toList();
    }


}
