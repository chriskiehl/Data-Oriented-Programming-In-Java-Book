package dop.chapter03;

import org.junit.jupiter.api.Test;


public class Listing3_18_and_3_19 {

    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.18
     * ───────────────────────────────────────────────────────
     * Capturing the constraints in a new type
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
        /**
         * ───────────────────────────────────────────────────────
         * Listings 3.19
         * ───────────────────────────────────────────────────────
         * Putting the domain information into our model
         * ───────────────────────────────────────────────────────
         */
        // We can stick this new type on our data model.
        record Measurement(
                String sampleId,
                PositiveInt daysElapsed,  // ◄────┐ These changes have a compounding effect on our
                Degrees contactAngle      //      │ understanding. Now at a glance, we can tell
        ) {}                              //      │ exactly what these Measurement attributes *mean*.
    }
}
