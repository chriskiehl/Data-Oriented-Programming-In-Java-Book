package dop.chapter07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listing7_07 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.7
     * ───────────────────────────────────────────────────────
     * Control logic for cleaning the duplicate records
     * ───────────────────────────────────────────────────────
     */
    static List<RawData> cleanDuplicates(List<RawData> rows) {
        Map<String, List<RawData>> dupesById = new HashMap<>();   //  ─┐
        for (RawData row : rows) {                                //   │
            String key = row.id();                                //   │ Finding all the duplicates
            if (!dupesById.containsKey(key)) {                    //   │
                dupesById.put(key, new ArrayList<>());            //   │
            }                                                     //   │
            dupesById.get(key).add(row);                          //   │
        }                                                         //  ─┘

        List<RawData> cleaned = new ArrayList<>();
//                    ▲
//                    └──── Setting up our output data set

        // Now we loop over the list of values stored under each key
        for (List<RawData> duplicates : dupesById.values()) {
            RawData merged = duplicates.getFirst();
            // And then loop over each sublist and apply our binary operation
            // to each row
            for (RawData other : duplicates.subList(1, duplicates.size())) {
                merged = add(merged, other);
            }
            // Then finally add the now combined result to the output data
            cleaned.add(merged);
        }
        return cleaned;
    }















    static RawData add(RawData x, RawData y) { return x; }
    record RawData(String id) {}
}
