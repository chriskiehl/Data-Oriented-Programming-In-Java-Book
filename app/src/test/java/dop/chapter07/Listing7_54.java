package dop.chapter07;

import java.util.Optional;

public class Listing7_54 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.54
     * ───────────────────────────────────────────────────────
     * The basic pattern lifters follow
     * ───────────────────────────────────────────────────────
     */
    //                          ┌────────────────────────────────────────┐
    static Optional<Integer> add(Optional<Integer> a, Optional<Integer> b) {
        //                      └────────────────────────────────────────┘
        //                                      ▲
        //                                      └─ (This violates our rule of thumb about knowing what's
        //                                          in the optional for sake of example)
        // First we check if both are defined
        if (a.isPresent() && b.isPresent()) {
        // If so, we grab the values and perform some operation on them
            return Optional.of(a.get() + b.get());
        } else {
            // Otherwise, we return Optional.empty()
            return Optional.empty();
        }
    }
}
