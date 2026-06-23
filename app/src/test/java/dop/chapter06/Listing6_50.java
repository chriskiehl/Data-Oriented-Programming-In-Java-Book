package dop.chapter06;

import java.math.BigDecimal;
import java.util.Optional;

public class Listing6_50 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.50
   * ───────────────────────────────────────────────────────
   * The finished method
   * ───────────────────────────────────────────────────────
   */
  public static ReviewedFee assessDraft(
      Rules rules,
      LateFee<Draft> draft
  ) {
    BigDecimal total = draft.total().value();
    if (total.compareTo(rules.minimumFeeThreshold()) < 0){
      return new NotBillable(draft, new Reason("..."));
    }
    else if (total.compareTo(rules.maximumFeeThreshold()) > 0) {
      return draft.customer().approval().isEmpty()
        ? new NeedsApproval(draft)
        : switch (draft.customer().approval().get().status()) {
            case ApprovalStatus.APPROVED -> new Billable(draft);
            case ApprovalStatus.PENDING -> new NotBillable(draft, new Reason("..."));
            case ApprovalStatus.DENIED -> new NotBillable(draft, new Reason("..."));
      };
    } else {
        return new Billable(draft);
    }
  }








  sealed interface ReviewedFee {}
  record Billable(LateFee<Draft> draft) implements ReviewedFee {}
  record NeedsApproval(LateFee<Draft> draft) implements ReviewedFee {}
  record NotBillable(LateFee<Draft> draft, Reason reason) implements ReviewedFee {}
  record Draft() {}
  record Reason(String value) {}
  record LateFee<State>(USD total, Customer customer) {}
  record USD(BigDecimal value) {}
  record Customer(Optional<Approval> approval) {}
  record Approval(ApprovalStatus status) {}
  enum ApprovalStatus {APPROVED, PENDING, DENIED}
  record Rules(BigDecimal minimumFeeThreshold, BigDecimal maximumFeeThreshold) {}

}
