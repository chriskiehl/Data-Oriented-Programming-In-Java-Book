package dop.chapter05;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Listing5_31 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.31
     * ───────────────────────────────────────────────────────
     * Sealing doesn't fix the awkward programming problems
     * ───────────────────────────────────────────────────────
     */
    sealed interface LateFee {   // ◄── Sealing our types from Figure 5.12
        record Draft(Details details) implements LateFee {}   // ◄───── We’re shortening the names since the “LateFee”
        record Billed(Details details /*...*/) implements LateFee {} // part is now explicit in the interface
        record Rejected(Details details /*...*/) implements LateFee {}
        record InReview(Details details /*...*/) implements LateFee {}
    }

    Map<LateFee, USD> totalsByLifecycle(List<LateFee> fees) {
//        return fees.stream().map(fee -> {  /* fee.details() ??? */
//            //                                     ▲
//            //                                     └── You might expect this to work since they're
//            //                                         all the same data type, but to Java, they're
//            //                                         "just" an interface. It has no idea what's inside.
//            return null;
//        })
        return null; // (to have the example compile)
    }









    record Details(USD total) {}
    record USD(BigDecimal value) {}
}
