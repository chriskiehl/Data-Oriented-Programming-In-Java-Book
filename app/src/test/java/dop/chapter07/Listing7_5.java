package dop.chapter07;

import java.util.stream.Stream;

public class Listing7_5 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.5
     * ───────────────────────────────────────────────────────
     * More Binary operations
     * ───────────────────────────────────────────────────────
     */
    void example() {
        var example1 = Stream.concat(Stream.of(1,2), Stream.of(3));    //  ┐
        var example2 = true || false;                                  //  │◄── Despite the differing names and symbols,
        var example3 = true && false;                                  //  ┘    these are all binary operations!
    }
}
