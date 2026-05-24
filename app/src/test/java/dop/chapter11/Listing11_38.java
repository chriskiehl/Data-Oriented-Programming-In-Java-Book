package dop.chapter11;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class Listing11_38 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.38
     * ───────────────────────────────────────────────────────
     * The Stream.generate function
     * ───────────────────────────────────────────────────────
     */
    public static<T> Stream<T> generate(Supplier<? extends T> s) {
        // The implementation is elided in the book, but since you're
        // here, click on this and read the Javadoc:
        return Stream.generate(s);
        //                  ▲
        //                  └──── Generate will take any Supplier and generate an
        //                        infinite stream of data
    }
}
