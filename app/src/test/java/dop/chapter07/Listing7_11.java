package dop.chapter07;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

public class Listing7_11 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.11
   * ───────────────────────────────────────────────────────
   * Combining Maps and their values with binary operations
   * ───────────────────────────────────────────────────────
   */
  static <K,V> Map<K,List<V>> add(
      Map<K, List<V>> left,
      Map<K, List<V>> right,
//    ┌──────────────────────────────────┐
      BinaryOperator<List<V>> operator
//    └──────────────────────────────────┘
//                              └──── Supplying a binary operation as an argument
  ) {
    HashMap<K, List<V>> combined = new HashMap<>(left);
    right.forEach((key, value) -> combined.merge(
            key,
            value,
//                            ┌────────────────────────────────┐
            (list1, list2) -> operator.apply(list1, list2)));
//                            └────────────────────────────────┘
//                                   ▲
//                                   └──── Now the code can decide what to do
//                                         whenever collisions are encountered
    return combined;
  }

}
