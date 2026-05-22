package dop.chapter06;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Listing6_44 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.44
     * ───────────────────────────────────────────────────────
     * The finished implementation for buildDraft
     * ───────────────────────────────────────────────────────
     */
    static LateFee<Draft> buildDraft(
            LocalDate today,
            EnrichedCustomer customer,
            List<PastDue> invoices) {
        return new LateFee<>(
            new Draft(),
            customer,
            computeFee(invoices, customer.feePercentage()),
            today,
            dueDate(today, customer.terms()),
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
    static USD computeFee(List<PastDue> invoices, Percent percentage) { return null; }
    static LocalDate dueDate(LocalDate today, PaymentTerms terms) { return null; }
}
