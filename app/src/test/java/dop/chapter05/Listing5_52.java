package dop.chapter05;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dop.chapter05.Listing5_52.Lifecycle.InReview;
import dop.chapter05.Listing5_52.ReviewedFee.Billable;
import dop.chapter05.Listing5_52.ReviewedFee.NeedsReview;
import dop.chapter05.the.existing.world.Entities.Address;
import dop.chapter05.the.existing.world.Entities.Invoice;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.Approval;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;

public class Listing5_52 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.52
   * ───────────────────────────────────────────────────────
   * The finished model
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
  ) {}

  sealed interface ReviewedFee {
    record Billable(LateFee<Lifecycle.Draft> latefee)
            implements ReviewedFee {}
    record NeedsReview(LateFee<Lifecycle.Draft> latefee)
            implements ReviewedFee {}
    record NotBillable(LateFee<Lifecycle.Draft> latefee, Reason reason)
            implements ReviewedFee {}
  }

  record EnrichedCustomer(
    CustomerId id,
    Address address,
    Percent feePercentage,
    PaymentTerms terms,
    CustomerRating rating,
    Optional<Approval> approval
  ) {}

  interface TheMethodsSignatures {
    List<PastDue> collectPastDue(List<Invoice> invoices);
    LateFee<Lifecycle.Draft> buildDraft(List<PastDue> invoices);
    ReviewedFee assessDraft(LateFee<Lifecycle.Draft> invoice);
    LateFee<? extends Lifecycle> submitBill(Billable draft);
    LateFee<InReview> startApproval(NeedsReview needsReview);
  }

  record USD(BigDecimal value) {}
  record CustomerId(String value) {}
  record ApprovalId(String value) {}
  record Reason(String value) {}
  record Percent(double numerator, double denominator) { /*...*/ }

}
