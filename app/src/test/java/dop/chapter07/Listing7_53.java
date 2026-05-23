package dop.chapter07;

import java.util.Optional;

public class Listing7_53 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.53
     * ───────────────────────────────────────────────────────
     * This knows too much about what’s inside of the optional
     * ───────────────────────────────────────────────────────
     */
    static void doSomething(Optional<Integer> maybeInt) {
//                        ▲
//                        └──── This usage would be suspect because we know
//                              what’s in the Optional
        if (maybeInt.isPresent()) {
            int myValue = maybeInt.get();
//                        ▲
//                        └──── The implementation ends up coupled to the
//                              details of what the optional is holding
        }
    }
}
