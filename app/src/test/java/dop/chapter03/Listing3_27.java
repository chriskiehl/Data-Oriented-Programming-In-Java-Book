package dop.chapter03;

public class Listing3_27 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 3.27
     * ───────────────────────────────────────────────────────
     * Is this code correct?
     * ───────────────────────────────────────────────────────
     */
    double computeFee(double total, double feePercent) {  // ◄── Will this always do the right thing?
        return total * feePercent;                        // ◄── What kind of tests would you write?
    }
}
