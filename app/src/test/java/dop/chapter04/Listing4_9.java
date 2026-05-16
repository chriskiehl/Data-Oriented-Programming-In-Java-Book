package dop.chapter04;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Listing4_9 {



    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.9
     * ───────────────────────────────────────────────────────
     * Here's the messiness of the design process (which is both
     * expected, normal, and OK). We don't know where this concept
     * of `isCompleted` goes yet, but we *do* know from the previous
     * listing that it doesn't belong on `Step`.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        record Step(String name /* (removed) */ ) {
        }      //                      ▲
        //                             └─ No more isCompleted here.
        //                                We have to figure out where it goes.
    }
}

