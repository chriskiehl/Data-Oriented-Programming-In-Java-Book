package dop.chapter06;

import java.util.Optional;

public class Listing6_49 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.49
   * ───────────────────────────────────────────────────────
   * Handling missing approvals imperatively
   * ───────────────────────────────────────────────────────
   */
  ReviewedFee example(LateFee draft) {
    return draft.customer().approval().isEmpty()                      // ┐
            ? new NeedsApproval(draft)                                // │◄─ the imperative version puts the
            : switch (draft.customer().approval().get().status()) {   // ┘   interesting case at the top
        case ApprovalStatus.APPROVED -> new Billable(draft);
        case ApprovalStatus.PENDING -> new NotBillable(draft /*...*/);
        case ApprovalStatus.DENIED -> new NotBillable(draft /*...*/);
    };
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
