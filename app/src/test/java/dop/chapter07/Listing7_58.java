package dop.chapter07;

import java.util.Optional;
import java.util.function.BinaryOperator;

public class Listing7_58 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.58
     * ───────────────────────────────────────────────────────
     * Converting BinaryOperators between worlds
     * ───────────────────────────────────────────────────────
     */
    void example() {
        // Lifting arbitrary binaryOperations into the Optional universe
        BinaryOperator<Optional<Integer>> maybeAdd = lift(Integer::sum);      
        BinaryOperator<Optional<String>> maybeConcat = lift(String::concat);
    }

































    static <A> BinaryOperator<Optional<A>> lift(BinaryOperator<A> f) {
        return (opt1, opt2) -> Optional.empty();
    }
}
