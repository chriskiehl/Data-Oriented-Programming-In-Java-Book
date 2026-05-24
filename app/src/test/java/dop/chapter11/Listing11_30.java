package dop.chapter11;

import java.util.Optional;

public class Listing11_30 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.30
     * ───────────────────────────────────────────────────────
     * Capturing the meaning of the test in data
     * ───────────────────────────────────────────────────────
     */
    // Tests give us another avenue for communicating what we
    // know about a system. This is especially useful when working
    // legacy services. The code might be a nightmare, but we can
    // encode what we know about it anytime we touch its tests.
    //
    // The example in the book looks "through" the if/else branches
    // in the code into what they denote about the *system*. The
    // system has three novel states.
    enum MirrorIs { EMPTY, UP_TO_DATE, STALE}
    // And each of those states is associated with a set of data.
    record State(MirrorIs state,
                 Export primaryHas,
                 Optional<Export> mirrorHas) {}














    record Export(String id){}
}
