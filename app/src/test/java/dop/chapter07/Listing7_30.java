package dop.chapter07;

public class Listing7_30 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.30
     * ───────────────────────────────────────────────────────
     * States can be recombined over and over into larger sets
     * ───────────────────────────────────────────────────────
     */
    void example() {
        for (RawData a : everyPossibleRow()) {
            for (RawData b : everyPossibleRow()) {
                for (RawData c : everyPossibleRow()) {
                    // assert something about a and b and c
                }
            }
        }
        // or 4 or 5 or more!
    }














    record RawData(String id) {}

    static RawData[] everyPossibleRow() {
        return new RawData[0];
    }
}
