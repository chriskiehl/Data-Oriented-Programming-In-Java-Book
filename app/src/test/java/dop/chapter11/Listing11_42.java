package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.function.Function.identity;

public class Listing11_42 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.42
     * ───────────────────────────────────────────────────────
     * Concatenating an arbitrary number of streams
     * ───────────────────────────────────────────────────────
     */
    @SafeVarargs
    static <A> Stream<A> concat(Stream<A>... streams) {      //  ┐
        return Arrays.stream(streams).flatMap(identity());   //  │◄── A utility function for combining
    }                                                        //  ┘    arbitrary numbers of streams

    @Test
    void combiningArbitraryStreams() {
        concat(
            Stream.of("A","B"),          //  ┐
            Stream.of("C"),              //  │
            Stream.of("D", "E"),         //  │◄── This lets us combine more
            Stream.of("F", "G", "H"))    //  ┘    than the two streams allowed
           .forEach(System.out::print);  //       by Stream.concat
    }
    // [out]
    // ABCDEFGH
}
