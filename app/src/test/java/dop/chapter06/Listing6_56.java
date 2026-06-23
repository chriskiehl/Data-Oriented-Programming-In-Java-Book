package dop.chapter06;

public class Listing6_56 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.56
   * ───────────────────────────────────────────────────────
   * Implementing the billing and approval side-effects
   * ───────────────────────────────────────────────────────
   */
  record LateFee<State extends Lifecycle>(State state, Customer customer /* ... */) {
    // "Withers" help us make "updates" to immutable data structures.
    // These will eventually be built into the language, but for
    // now we can make them by hand or leverage annotation processors
    public LateFee<Billed> markBilled(InvoiceId id) {
      return new LateFee<>(new Billed(id), customer /*...*/);
    }

    public LateFee<Rejected> markNotBilled(Reason reason) {
      return new LateFee<>(new Rejected(reason), customer  /*...*/);
    }

    public LateFee<UnderReview> markAsBeingReviewed(String approvalId) {
      return new LateFee<>(new UnderReview(approvalId), customer /*...*/);
    }
  }

  LateFee<? extends Lifecycle> submitBill(Billable billable) {
    BillingResponse response = this.billingApi.submit(/*...*/);
    LateFee<Draft> draft = billable.latefee();
    return switch (response.status()) {
      case ACCEPTED ->
        draft.markBilled(new InvoiceId(response.invoiceId()));
      case REJECTED ->
        draft.markNotBilled(new Reason(response.error()));
    };
  }

  LateFee<UnderReview> startApproval(NeedsApproval needsApproval) {
//             ▲                              ▲
//             └──────────────────────────────┘
//                            ▲
//                            └─ Another good example of a method which is
//                               completely controlled by its data types
    Approval approval = this.approvalsApi.createApproval(/*...*/);
    return needsApproval.latefee().markAsBeingReviewed(approval.id());
  }








  sealed interface Lifecycle {}
  record Draft() implements Lifecycle {}
  record Billed(InvoiceId id) implements Lifecycle {}
  record Rejected(Reason reason) implements Lifecycle {}
  record UnderReview(String approvalId) implements Lifecycle {}
  record InvoiceId(String value) {}
  record Reason(String value) {}
  record Customer(){}
  record Billable(LateFee<Draft> latefee) {}
  record NeedsApproval(LateFee<Draft> latefee) {}
  enum BillingStatus {ACCEPTED, REJECTED}
  record BillingResponse(BillingStatus status, String invoiceId, String error) {}
  record Approval(String id) {}
  BillingAPI billingApi;
  ApprovalsAPI approvalsApi;
  interface BillingAPI {
    BillingResponse submit();
  }
  interface ApprovalsAPI {
    Approval createApproval();
  }

}
