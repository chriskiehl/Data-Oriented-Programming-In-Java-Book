package dop.chapter07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Listing7_9 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.9
     * ───────────────────────────────────────────────────────
     * Refactoring groupBy to combine lists with algebraic Binary Operations
     * ───────────────────────────────────────────────────────
     */
    <K,T> Map<K, List<T>> groupBy(List<T> items, Function<T, K> classifier) {
      Map<K, List<T>> groups = new HashMap<>();
      for (T item : items) {
        K key = classifier.apply(item);
        List<T> list1 = groups.getOrDefault(key, new ArrayList<>());
//                                  ▲
//                                  └──── Rather than putting an empty list into the map and mutating
//                                        it in place, we read the value out as data
        List<T> list2 = List.of(item);
//                                ▲
//                                └──── Our “algebra” is about Lists, so we wrap the incoming item
//                                      in a List
        groups.put(key, add(list1, list2));
//                       ▲    ▲      ▲
//                       └──── We combine the two lists together and store the
//                             new list in the map.
      }
      return groups;
    }















    static <T> List<T> add(List<T> list1, List<T> list2) { return list1; }
}
