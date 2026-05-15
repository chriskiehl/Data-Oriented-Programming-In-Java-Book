package dop.chapter03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


public class Listing3_28 {




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.8 through 3.9
     * ───────────────────────────────────────────────────────
     * Modeling isn't just informational. It can prevent very real
     * bugs from even being possible. Ambiguity is a dangerous thing
     * to have in a code base. People come from different backgrounds.
     * Codebases change hands. What's "obvious" to one group will be
     * not at all "obvious" to another.
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        // (Note: as usual, the below is only defined as an inline lambda in order to keep
        //       each listing isolated)
        //
        BiFunction<Double, Double, Double> computeFee = (Double total, Double feePercent) -> {
            return total * feePercent;
        }; //        ▲
           //        │
           //        └── What kind of tests would you write to 'prove' this does the right thing?

        // You can't write any! Whether it does the right thing or not depends entirely on the
        // caller knowing how to use it correctly. For instance, how should that fee percentage
        // be represented? 0.10? 10.0? The "obvious" way will vary from person to person!

        // We can completely eliminate this ambiguity and potential customer impacting bug
        // through better modeling. What does it *mean* to be a percentage? A value between 0..1?
        // 1..100? A rational?

        // We can use the design of our code to ensure that *only* correct notion of percents
        // can be created. We don't need to rely on secret team knowledge or everyone having the
        // same notion of "the obvious way" -- we make it absolutely explicit in code.
        record Percent(double numerator, double denominator) {
            Percent {
                if (numerator > denominator) {
                    throw new IllegalArgumentException(
                            "Percentages are 0..1 and must be expressed " +
                            "as a proper fraction. e.g. 1/100");
                }
            }
        }
        // Any departure from what percentages mean to us gets halted with an error (rather than
        // propagated out to very angry and confused customers)
    }
}
