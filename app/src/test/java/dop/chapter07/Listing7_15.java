package dop.chapter07;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class Listing7_15 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.15
     * ───────────────────────────────────────────────────────
     * Refactoring to use the streams APIs
     * ───────────────────────────────────────────────────────
     */
    record NonEmptyList<A>(List<A> items) {
        //       ▲
        //       └──── The helper record lets us prove to Java’s type system that we have
        //             something to reduce over. Without it, we would have to return
        //             Optional<RawData>
      NonEmptyList {
        if (items.isEmpty()) {
          throw new IllegalArgumentException("...");
        }
      }
    }


    static RawData merge(NonEmptyList<RawData> dupes) {
      RawData initial = dupes.items().getFirst();
//            ▲
//            └──── The extra NonEmptyList wrapper lets us safely reference
//                  items and reduce without Java asking us what happens if
//                  the list is empty
      return dupes.items().stream().skip(1)
        .reduce(initial, Chapter07::add);
    }

    static List<RawData> cleanDuplicates(List<RawData> rows) {
        Map<String, List<RawData>> dupesById = rows.stream()
          .collect(groupingBy(RawData::id));
//                 ▲
//                 └──── Grouping using the built in groupingBy rather than
//                       our version

        return dupesById.values().stream()
           .map(NonEmptyList::new)
           .map(dupes -> merge(dupes))
           .toList();
//         ▲
//         └──── We grab the duplicates then map over them to reduce down to
//               a final answer
    }















    static class Chapter07 {
        static RawData add(RawData x, RawData y) { return x; }
    }
    record RawData(String id) {}
}
