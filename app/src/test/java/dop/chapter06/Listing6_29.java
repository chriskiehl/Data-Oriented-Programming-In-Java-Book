package dop.chapter06;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Listing6_29 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.29
   * ───────────────────────────────────────────────────────
   * The data model
   * ───────────────────────────────────────────────────────
   */
  sealed interface Lifecycle {
    record Draft() implements Lifecycle{}
    record Billed(InvoiceId id) implements Lifecycle {}
    record Rejected(Rejection why) implements Lifecycle {}
  }
  record LateFee<State extends Lifecycle>(
      State state,
      USD total,
      LocalDate invoiceDate,
      LocalDate dueDate,
      List<PastDue> includedInFee
  ) {}

  sealed interface ReviewedFee {}
  record Billable(LateFee<Lifecycle.Draft> latefee)
      implements ReviewedFee {}
  record NeedsReview(LateFee<Lifecycle.Draft> latefee)
      implements ReviewedFee {}
  record NotBillable(LateFee<Lifecycle.Draft> latefee, String rationale)
      implements ReviewedFee {}

  record EnrichedCustomer(
//         ▲
//         └──── As a reminder, we introduced this “enriched” data type to
//               abstract away the numerous services from which we have to
//               fetch customer data
      CustomerId id,
      Address address,
      Percent feePercentage,
      PaymentTerms terms,
      CustomerRating rating,
      Optional<Approval> approval
  ) {}








  record InvoiceId(String value) {}
  record Rejection(String value) {}
  record USD(BigDecimal value) {}
  record PastDue() {}
  record CustomerId(String value) {}
  record Address(String value) {}
  record Percent(BigDecimal value) {}
  enum PaymentTerms {NET_30}
  enum CustomerRating {GOOD}
  record Approval(String value) {}

}
