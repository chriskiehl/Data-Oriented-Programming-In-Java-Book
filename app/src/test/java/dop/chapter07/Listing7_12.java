package dop.chapter07;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

public class Listing7_12 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.12
     * ───────────────────────────────────────────────────────
     * Expressing groupBy in terms of binary operations on Maps
     * ───────────────────────────────────────────────────────
     */
    static <K,T> Map<K, List<T>> groupBy(
            List<T> items,
            Function<T, K> classifier
    ) {
        Map<K, List<T>> result = new HashMap<>();
        for (T item : items) {
            K key = classifier.apply(item);
            Map<K, List<T>> anotherMap = Map.of(key, List.of(item));
//                           ▲
//                           └──── First we create a new map with the updated data

//                      
            result = add(result, anotherMap, (list1,list2) -> add(list1, list2));
//                    ▲
//                    └──── Then produce another map that’s the combination of
//                          what we have so far plus the new value
        }
        return result;
    }















    static <K,V> Map<K,List<V>> add(Map<K, List<V>> left, Map<K, List<V>> right, BinaryOperator<List<V>> operator) {
        return left;
    }

    static <T> List<T> add(List<T> list1, List<T> list2) {
        return Stream.concat(list1.stream(), list2.stream()).toList();
    }
}
