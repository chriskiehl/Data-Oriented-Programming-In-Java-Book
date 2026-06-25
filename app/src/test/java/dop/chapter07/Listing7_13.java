package dop.chapter07;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

public class Listing7_13 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.13
   * ───────────────────────────────────────────────────────
   * Replacing manual looping with map and reduce
   * ───────────────────────────────────────────────────────
   */
  static <K,T> Map<K, List<T>> groupBy(
      List<T> items,
      Function<T, K> classifier
  ) {
    return items.stream()
        // First we turn everything into a Map
        .map(item -> Map.of(classifier.apply(item), List.of(item)))
        //  Then we loop over it to combine it with our binary operation
        .reduce(Map.of(), (a,b) -> add(a, b, (list1, list2) -> add(list1, list2)));
//                                                                  ▲
//                                                                  └──── And collisions, if any, are handled with yet another
//                                                                        binary operation
  }








  static <K,V> Map<K,List<V>> add(Map<K, List<V>> left, Map<K, List<V>> right, BinaryOperator<List<V>> operator) {
    return left;
  }

  static <T> List<T> add(List<T> list1, List<T> list2) {
    return Stream.concat(list1.stream(), list2.stream()).toList();
  }

}
