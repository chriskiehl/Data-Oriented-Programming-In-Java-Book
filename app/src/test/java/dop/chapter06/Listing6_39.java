package dop.chapter06;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Listing6_39 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.39
     * ───────────────────────────────────────────────────────
     * The implementation just follows the type signature
     * ───────────────────────────────────────────────────────
     */
    static LateFee<Draft> buildDraft(LocalDate today, EnrichedCustomer customer, List<PastDue> invoices) {
        //     ▲
        //     └────────── The implementation just does whatever the types say
        //            │
        //            ▼
        return new LateFee<>(
            new Draft(),
            customer,
            // We just have to decide this value
            /*???*/ __ /*???*/,
            today,
            // and this one.
            /*???*/ ___ /*???*/,
            invoices
        );
    }















    record Draft() {}
    record LateFee<State>(State state, EnrichedCustomer customer, USD fee, LocalDate invoiceDate, LocalDate dueDate, List<PastDue> invoices) {}
    record EnrichedCustomer(Percent feePercentage, PaymentTerms terms) {}
    record PastDue() {}
    record USD(BigDecimal value) {}
    record Percent(BigDecimal value) {}
    enum PaymentTerms {NET_30}
    static USD __;
    static LocalDate ___;
}
