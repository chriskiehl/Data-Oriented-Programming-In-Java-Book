package dop.chapter05;

import dop.chapter05.Listing5_41.Lifecycle.Draft;
import dop.chapter05.Listing5_41.Lifecycle.InReview;
import dop.chapter05.Listing5_41.ReviewedFee.Billable;
import dop.chapter05.Listing5_41.ReviewedFee.NeedsReview;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static dop.chapter05.the.existing.world.Entities.Invoice;

public class Listing5_41 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.41
     * ───────────────────────────────────────────────────────
     * the current data model for our feature
     * ───────────────────────────────────────────────────────
     */
    public record PastDue(Invoice value) {}

    public sealed interface Lifecycle {
        record Draft() implements Lifecycle {}
        record Billed(String invoiceId) implements Lifecycle {}
        record Rejected(Reason reason) implements Lifecycle {}
        record InReview(ApprovalId approvalId) implements Lifecycle {}
    }

    public record LateFee<State extends Lifecycle>(
        String customerId,
        USD total,
        State state,
        LocalDate invoiceDate,
        LocalDate dueDate,
        List<Invoice> includedInFee
    ){}

    sealed interface ReviewedFee {
        record Billable(LateFee<Draft> latefee)
            implements ReviewedFee {}
        record NeedsReview(LateFee<Draft > latefee)
            implements ReviewedFee {}
        record NotBillable(LateFee<Draft> latefee, Reason reason)
            implements ReviewedFee {}
    }


    interface TheMethodsSignatures {
        List<PastDue> collectPastDue(List<Invoice> invoices);
        LateFee<Draft> buildDraft(List<PastDue> invoices);
        ReviewedFee assesDraft(LateFee<Draft> invoice);
        LateFee<? extends Lifecycle> submitBill(Billable draft);
        LateFee<InReview> startApproval(NeedsReview needsReview);
    }





    record USD(BigDecimal value) {}
    record Reason(String value) {}
    record ApprovalId(String value) {}
}
