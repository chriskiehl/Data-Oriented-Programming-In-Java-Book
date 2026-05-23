package dop.chapter07;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Listing7_17 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.17
     * ───────────────────────────────────────────────────────
     * We loop over the items and… apply a binary operation!
     * ───────────────────────────────────────────────────────
     */
    static <K,V> Map<K,V> toMap(         //  ┐
            List<V> items,               //  │◄── Supplying a BinaryOperator allows us to build
            Function<V, K> classifier,   //  │    Maps of any type
            BinaryOperator<V> binop      //  ┘
    ) {
        return items.stream()
            .map(item -> Map.of(classifier.apply(item), item))
//                                                        ▲
//                                                        └──── No more wrapping things in List.
//                                                   ┌──────┐
            .reduce(Map.of(), (m1, m2) -> add(m1, m2, binop));
//                                                   └──────┘
//                                                      ▲
//                                                      └──── Conflicts are resolved via the binary operation
    }

    static <K,V> Map<K, V> add(                           //  ┐
            Map<K, V> left,                               //  │
            Map<K, V> right,                              //  │◄── The binary operation that operates
            BinaryOperator<V> binop                       //  │    on maps can be made similarly generic.
        ) {                                               //  │
        HashMap<K, V> combined = new HashMap<>(left);     //  │
        right.forEach((key, value) -> combined.merge(     //  │
            key,                                          //  │
            value,                                        //  │
            (list1, list2) -> binop.apply(list1, list2))); // ┘
        return combined;
    }
}
