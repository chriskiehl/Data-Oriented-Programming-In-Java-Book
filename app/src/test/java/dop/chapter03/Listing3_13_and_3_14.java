package dop.chapter03;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Listing3_13_and_3_14 {

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
  @Test
  void example() {

    record Measurement(
        String sampleId,
        Integer daysElapsed,
        double contactAngle
    ) {
      Measurement {
        if (!sampleId.matches("(HEAT|AIR|UV)-\\d+")) {             //  ◄────┐ Validating that the Sample ID
          throw new IllegalArgumentException(                      //       │ is in the right shape
              "Must honor the form {CuringMethod}-{number}" +
              "Where CuringMethod is one of (HEAT, AIR, UV), " +
              "and number is any positive integer"
          );
        }
        if (daysElapsed < 0) {                                     //  ◄────┐ And validating the remaining
          throw new IllegalArgumentException(                      //       │ constraints.
              "Days elapsed cannot be less than 0!");              //       │
        }                                                          //       │
        if (!(Double.compare(contactAngle, 0.0) >= 0               //       │
            && Double.compare(contactAngle, 360.0) < 0)) {         //       │
          throw new IllegalArgumentException( "Contact angle must be 0-360");
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
        .collect(groupingBy(                                      //    │ But then it gets transformed. We have to
            Measurement::sampleId,                                //    │ pay close attention to understand that
            mapping(Measurement::contactAngle,                    //    │ those doubles in the map still represent Degrees
                Collectors.toList())));

    // Comparing the first and last samples in each
    // group to see how much things changes while curing
    List<Double> totalChanges = bySampleId.values()  // ◄──┐ More transformations. More distance. Do these
        .stream()                                    //    │ still represent degrees...?
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

}
