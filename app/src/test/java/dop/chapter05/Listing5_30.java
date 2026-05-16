package dop.chapter05;

public class Listing5_30 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.30
     * ───────────────────────────────────────────────────────
     * I just want to compute some stats, but the types make it
     * so hard!
     * ───────────────────────────────────────────────────────
     */
    // As we design the representation, we have to keep an eye
    // towards how these things will actually work in practice.
    //
    // What you might not notice until you start trying to program
    // with these is that they're really inflexible.
    //
    // Can we, say, compute some aggregate stats? Totals by lifecycle?
    /*



         ┌───── What would be returned?
         ▼
    Map<???, USD> totalsByLifecycle(List<???> fees) {
        //  ???                           ▲
    }                                     └── What goes here? Each lifecycle is an isolated type





    */
}
