package dop.chapter06;

import java.math.BigDecimal;
import java.util.List;

public class Listing6_41 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.41
     * ───────────────────────────────────────────────────────
     * the implementation for computing the fee
     * ───────────────────────────────────────────────────────
     */
    static USD computeFee(List<PastDue> pastDue, Percent percentage) {
        // Reads just like the requirments.
        // "The fee shall be calculated as a percentage of the customer's total past due"
        return computeTotal(pastDue).multiply(percentage.decimalValue());
    }















    static USD computeTotal(List<PastDue> pastDue) { return null; }
    record PastDue() {}
    record USD(BigDecimal value) {
        USD multiply(BigDecimal multiplier) { return this; }
    }
    record Percent(BigDecimal decimalValue) {}
}
