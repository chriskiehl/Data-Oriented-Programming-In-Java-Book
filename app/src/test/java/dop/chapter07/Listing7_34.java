package dop.chapter07;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Listing7_34 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.34
     * ───────────────────────────────────────────────────────
     * Associativity is the bedrock property of Stream parallelism
     * ───────────────────────────────────────────────────────
     */
    static <K,V> Map<K,V> toMap(
            List<V> items,
            Function<V, K> classifier,
            BinaryOperator<V> binop
    ) {
        return items.stream()
            .parallel()
//              ▲
//              └──── Associativity is a requirement for correct parallel
//                    stream processing
            .map(item -> Map.of(classifier.apply(item), item))
            .reduce(Map.of(), (m1, m2) -> add(m1, m2, binop));
    }














    static <K,V> Map<K, V> add(
            Map<K, V> left,
            Map<K, V> right,
            BinaryOperator<V> binop
    ) {
        HashMap<K, V> combined = new HashMap<>(left);
        right.forEach((key, value) -> combined.merge(
            key,
            value,
            (list1, list2) -> binop.apply(list1, list2)));
        return combined;
    }
}
