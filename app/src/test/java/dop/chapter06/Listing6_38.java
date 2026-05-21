package dop.chapter06;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.util.Map;

public class Listing6_38 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.38
     * ───────────────────────────────────────────────────────
     * Defending against future unknowns
     * ───────────────────────────────────────────────────────
     */
    void example() {
        currentDate.isAfter(
          invoice.invoiceDate()
            .with(gracePeriodAdjustments
              .getOrDefault(customer.rating(), /*??? */ __)));
//                                         ▲
//                                         └──── What goes here? What would make a good default?
    }















    LocalDate currentDate;
    Invoice invoice;
    Customer customer;
    Map<CustomerRating, TemporalAdjuster> gracePeriodAdjustments;

    // here just so the example compiles
    TemporalAdjuster __ = (x) -> x;

    interface Invoice {
        LocalDate invoiceDate();
    }

    interface Customer {
        CustomerRating rating();
    }

    enum CustomerRating {GOOD, ACCEPTABLE, POOR}
}
