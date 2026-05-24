package dop.chapter11;

public class Listing11_19 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.19
     * ───────────────────────────────────────────────────────
     * Does everything still work if we set weird thresholds?
     * ───────────────────────────────────────────────────────
     */
    void example() {
        // Does everything still work if we set this to a smaller value?
        USD threshold = USD.valueOf(1_500);
        threshold = USD.valueOf(0.1);
        //                       ▲
        //                       └──── What about a really small value?
        threshold = USD.valueOf(0.0);
        //                        ▲
        //                        └──── Is this OK…?
    }
}
