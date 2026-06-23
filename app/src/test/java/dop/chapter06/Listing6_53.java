package dop.chapter06;

import java.math.BigDecimal;
import java.util.Optional;

public class Listing6_53 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.53
   * ───────────────────────────────────────────────────────
   * Refactoring to make the 3 assessment cases explicit
   * ───────────────────────────────────────────────────────
   */
  public static ReviewedFee assessDraft(Entities.Rules rules, LateFee<Draft> draft) {
    return switch (assessTotal(rules, draft.total())) {
      case WITHIN_RANGE -> new Billable(draft);                        //  ┐
      case BELOW_MINIMUM -> new NotBillable(draft, new Reason("...")); //  │◄── The three, and only three, assessment
      case ABOVE_MAXIMUM -> draft.customer().approval().isEmpty()      //  ┘    possibilities in our domain
        ? new NeedsApproval(draft)
        : switch (draft.customer().approval().get().status()) {
            case APPROVED -> new Billable(draft);
            case PENDING -> new NotBillable(draft, new Reason("..."));
            case DENIED -> new NotBillable(draft, new Reason("..."));
        };
    };
  }








  enum Assessment {WITHIN_RANGE, BELOW_MINIMUM, ABOVE_MAXIMUM}
  static Assessment assessTotal(Entities.Rules rules, USD total) { return null; }
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
  static class Entities {
    interface Rules {}
  }

}
