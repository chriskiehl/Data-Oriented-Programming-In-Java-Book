package dop.chapter06;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class Listing6_42 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.42
     * ───────────────────────────────────────────────────────
     * Summing the line items and discovering something weird
     * ───────────────────────────────────────────────────────
     */
    static USD computeTotal(List<PastDue> invoices) {
        // First we grab the invoices
        return invoices.stream().map(PastDue::invoice)
            // then flatten all the line items
            .flatMap(x -> x.getLineItems().stream())
            // grab their charges and then...
            .map(LineItem::charges)
            //    ┌─── Hmm... This feels weird, right?
            //    ▼
            .map(USD::new)
            .reduce(USD.zero(), USD::add);
    }















    record PastDue(Invoice invoice) {}
    interface Invoice {
        List<LineItem> getLineItems();
    }
    record LineItem(BigDecimal charges, Currency currency) {}
    record USD(BigDecimal value) {
        static USD zero() { return new USD(BigDecimal.ZERO); }
        static USD add(USD x, USD y) { return new USD(x.value().add(y.value())); }
    }
}
