package dop.chapter06;

import java.util.HashMap;
import java.util.Map;

public class Listing6_19 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.19
     * ───────────────────────────────────────────────────────
     * Pre-computing the output for every possible input
     * ───────────────────────────────────────────────────────
     */
    static int increment(int x) {   //  ┐
        return x + 1;               //  │◄── Every output of this function is pre-determined.
    }                               //  ┘

    void example() {
        Map<Integer, Integer> ANSWERS = new HashMap<>();                //  ┐
        for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE-1; i++) { //  │
                                                                        //  │◄── Conceptually speaking (don’t actually do this
            ANSWERS.put(i, increment(i));                               //  │    unless you want an OutOfMemoryError), we could
        }                                                               //  │    store every input and output in one big lookup
                                                                        //  │    table. Our code doesn’t need the function –
                                                                        //  ┘    only the mapping it describes!
    }

}
