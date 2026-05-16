package dop.chapter05;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static dop.chapter05.the.existing.world.Entities.Invoice;

public class Listing5_37 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.37
     * ───────────────────────────────────────────────────────
     * Capturing lifecycle state as a generic
     * ───────────────────────────────────────────────────────
     */
    sealed interface Lifecycle {                                // ◄── This stays the same
      record Draft() implements Lifecycle {};
      record Billed(InvoiceId id) implements Lifecycle {};
      record Rejected(Reason reason) implements Lifecycle {};
      record InReview(ApprovalId approvalId) implements Lifecycle{};
    }


    record LateFee<State extends Lifecycle>(   // ◄── But we modify the LateFee record to track that information as a generic. We’ve lifted this information “up” to the type level
        State state,                           // ◄── This lets us say what’s inside of this object at compile time
        CustomerId customerId,
        USD total,
        LocalDate invoiceDate,
        LocalDate dueDate,
        List<Invoice> includedInFee
    ) {}

    record InvoiceId(String value) {}

    record Reason(String value) {}

    record ApprovalId(String value) {}

    record CustomerId(String value) {}

    record USD(BigDecimal value) {}
}
