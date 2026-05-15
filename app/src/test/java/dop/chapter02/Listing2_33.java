package dop.chapter02;

public class Listing2_33 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.33
     * ───────────────────────────────────────────────────────
     * Enforcing invariants during construction
     * ───────────────────────────────────────────────────────
     */
    record Rational(int num, int denom) {
        Rational {
            if (denom == 0) {     //  ◄─────────────────────────┐ Checking during construction makes sure
                throw new IllegalArgumentException(   //        │ that only valid data is created.
                        "Hey! That’s not how math works!"   //  │
                );                                          //  │
            }
        }
    }
}
