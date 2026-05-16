package dop.chapter04;

public class Listing4_41 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 4.41
     * ───────────────────────────────────────────────────────
     * Counting how many types are sealed under our interface
     * ───────────────────────────────────────────────────────
     */
    sealed interface CountingExample {                // ◄── The new Sum Type we’re creating
        record One()  implements CountingExample {}   // ◄── and it has one…
        record Two()  implements CountingExample {}   // ◄── two…
        record Thee() implements CountingExample {}   // ◄── three options

        // As an aside:
        // In an early draft of the book, I tried to reference Count von Count
        // from Sesame Street in this example, but early reviewers didn't get the
        // reference at all.
        //
        // Am I out of touch?
        //
        // No...
        //
        // It's the kids who are wrong.
    }
}
