package dop.chapter07;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

public class Listing7_16 {
  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.16
   * ───────────────────────────────────────────────────────
   * We loop over the items and apply a binary operation
   * ───────────────────────────────────────────────────────
   */
  static <K,T> Map<K, List<T>> groupBy(List<T> items, Function<T, K> classifier) {
    //  Inside of groupBy we loop over the data
    //  and apply binary operations
    return items.stream()
//                                          ┌────────────┐
       .map(x -> Map.of(classifier.apply(x), List.of(x)))
//                                          └────────────┘
//                                               └──── Right now, those binary operations are “about”
//                                                     combining lists, but they don’t have to be!
       .reduce(Map.of(), (map1, map2) -> add(
            map1, map2,
            Chapter07::add));
//               ▲
//               └──── The main logic is looping over the elements and applying a
//                     binary operation. It doesn’t care what that operation does
  }








  static <K,V> Map<K,List<V>> add(Map<K, List<V>> left, Map<K, List<V>> right, BinaryOperator<List<V>> operator) {
    return left;
  }

  static class Chapter07 {
    static <T> List<T> add(List<T> list1, List<T> list2) {
      return Stream.concat(list1.stream(), list2.stream()).toList();
    }
  }

}
