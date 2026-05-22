package dop.chapter06;

import java.util.Optional;

public class Listing6_48 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.48
     * ───────────────────────────────────────────────────────
     * Handling missing Approvals functionally
     * ───────────────────────────────────────────────────────
     */
    ReviewedFee example(LateFee draft) {
        return draft.customer().approval().map(approval -> switch(approval.status()) {
          case ApprovalStatus.APPROVED -> new Billable(draft);
          case ApprovalStatus.PENDING -> new NotBillable(draft /*...*/);
          case ApprovalStatus.DENIED -> new NotBillable(draft /*...*/);
        }).orElse(new NeedsApproval(draft));
        //   ▲
        //   └─ In this version, the most interesting case is tucked away
        //      at the very bottom of the expression
    }















    sealed interface ReviewedFee {}
    record Billable(LateFee draft) implements ReviewedFee {}
    record NotBillable(LateFee draft) implements ReviewedFee {}
    record NeedsApproval(LateFee draft) implements ReviewedFee {}
    record LateFee(Customer customer) {}
    record Customer(Optional<Approval> approval) {}
    record Approval(ApprovalStatus status) {}
    enum ApprovalStatus {APPROVED, PENDING, DENIED}
}
