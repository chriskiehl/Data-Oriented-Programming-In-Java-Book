package dop.chapter07;

import java.util.function.BinaryOperator;

public class Listing7_62 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.62
     * ───────────────────────────────────────────────────────
     * Should we make these?
     * ───────────────────────────────────────────────────────
     */
    interface Associative<A> extends BinaryOperator<A> {}
    interface Reflexive<A> extends BinaryOperator<A> {}
    interface Monotonic<A> extends BinaryOperator<A> {}
    // and so on...
}
