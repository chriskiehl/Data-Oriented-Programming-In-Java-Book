package dop.chapter07;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listing7_10 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.10
     * ───────────────────────────────────────────────────────
     * A few of the ways we could implement Map “addition”
     * ───────────────────────────────────────────────────────
     */
    class Example1 {

        // This is an example of a “left biased” implementation
        static <K,V> Map<K,List<V>> add(
                Map<K, List<V>> left,
                Map<K, List<V>> right) {
            HashMap<K, List<V>> combined = new HashMap<>(right);
            combined.putAll(left);
//                            ▲
//                            └──── It overwrites any conflicting values in “right” with
//                                  values from “left”
            return combined;
        }
    }

    class Example2 {
        // Here’s a “right biased” flavor
        static <K,V> Map<K,List<V>> add(
                Map<K, List<V>> left,
                Map<K, List<V>> right) {
            HashMap<K, List<V>> combined = new HashMap<>(left);
            combined.putAll(right);
//                            ▲
//                            └──── It overwrites any values in the left map with values
//                                  from right
            return combined;
        }
    }
}
