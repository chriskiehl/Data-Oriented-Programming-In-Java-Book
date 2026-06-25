package dop.chapter07;

import java.util.List;

public class Listing7_18 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.18
   * ───────────────────────────────────────────────────────
   * We loop over the items and, that’s right, apply a binary operation!
   * ───────────────────────────────────────────────────────
   */
  static List<RawData> cleanDuplicates(List<RawData> rows) {
    return List.copyOf(toMap(rows, RawData::id, Chapter07::add).values());
  }








  record RawData(String id) {}

  static class Chapter07 {
    static RawData add(RawData x, RawData y) {
      return x;
    }
  }

  static <K,V> java.util.Map<K,V> toMap(
      List<V> items,
      java.util.function.Function<V, K> classifier,
      java.util.function.BinaryOperator<V> binop
  ) {
    return java.util.Map.of();
  }

}
