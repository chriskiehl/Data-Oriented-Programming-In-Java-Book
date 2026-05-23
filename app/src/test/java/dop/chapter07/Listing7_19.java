package dop.chapter07;

public class Listing7_19 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.19
     * ───────────────────────────────────────────────────────
     * Calling a binary operation with the same arguments in different orders
     * ───────────────────────────────────────────────────────
     */
    void example() {
        RawData row1 = new RawData(/*...*/);
        RawData row2 = new RawData(/*...*/);
        add(row1, row2); // ◄── row1 then row2
        add(row2, row1); // ◄── row2 then row1
    }





















    record RawData() {}

    static RawData add(RawData x, RawData y) {
        return x;
    }
}
