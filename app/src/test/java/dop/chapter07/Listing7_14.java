package dop.chapter07;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Listing7_14 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.14
     * ───────────────────────────────────────────────────────
     * The same as what we started with, but using our custom groupBy
     * ───────────────────────────────────────────────────────
     */
    static List<RawData> cleanDuplicates(List<RawData> rows) {
        Map<String, List<RawData>> dupesById = groupBy(rows, RawData::id);
//                                                ▲
//                                                └──── Using the groupBy method we
//                                                      defined above.

        List<RawData> cleaned = new ArrayList<>();
        for (List<RawData> duplicates : dupesById.values()) {
            RawData merged = duplicates.getFirst();
            for (RawData other : duplicates.subList(1, duplicates.size())) {
                 merged = add(merged, other);
            }
            cleaned.add(merged);
        }
        return cleaned;
    }















    static <K,T> Map<K, List<T>> groupBy(List<T> items, java.util.function.Function<T, K> classifier) { return null; }
    static RawData add(RawData x, RawData y) { return x; }
    record RawData(String id) {}
}
