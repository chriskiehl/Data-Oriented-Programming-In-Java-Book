package dop.chapter06;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.util.Map;

public class Listing6_37 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.37
     * ───────────────────────────────────────────────────────
     * Using the map of GracePeriods
     * ───────────────────────────────────────────────────────
     */
    void example() {
        currentDate.isAfter(
          invoice.invoiceDate()
            .with(gracePeriod.get(customer.rating())));
//                ▲
//                └──── pretty clean!
    }















    LocalDate currentDate;
    Invoice invoice;
    Customer customer;
    Map<CustomerRating, TemporalAdjuster> gracePeriod;

    interface Invoice {
        LocalDate invoiceDate();
    }

    interface Customer {
        CustomerRating rating();
    }

    enum CustomerRating {GOOD, ACCEPTABLE, POOR}
}
