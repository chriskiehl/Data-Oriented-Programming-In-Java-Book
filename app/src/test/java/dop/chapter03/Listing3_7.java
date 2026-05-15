package dop.chapter03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


public class Listing3_7 {




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.7
     * ───────────────────────────────────────────────────────
     * Let's take another stab at understanding Sample ID
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {

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
}
