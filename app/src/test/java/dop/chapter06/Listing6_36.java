package dop.chapter06;

import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

public class Listing6_36 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.36
     * ───────────────────────────────────────────────────────
     * Modeling Grace Period as a map of data
     * ───────────────────────────────────────────────────────
     */
    static Map<CustomerRating, TemporalAdjuster> gracePeriod
//                                                ▲
//                                                └──── Now the business logic is just another piece of data!
      = Map.of(
        CustomerRating.GOOD, date -> date.plus(60, DAYS),
        CustomerRating.ACCEPTABLE, date -> date.plus(30, DAYS),
        CustomerRating.POOR, TemporalAdjusters.lastDayOfMonth()
    );















    enum CustomerRating {GOOD, ACCEPTABLE, POOR}
}
