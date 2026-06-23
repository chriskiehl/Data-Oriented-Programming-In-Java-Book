package dop.chapter06;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dop.chapter05.the.existing.world.Entities.Address;
import dop.chapter05.the.existing.world.Entities.Invoice;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.Approval;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;

public class Listing6_32 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.32
   * ───────────────────────────────────────────────────────
   * Our model from the previous chapter
   * ───────────────────────────────────────────────────────
   */
  public static List<PastDue> collectPastDue(
      EnrichedCustomer customer, 
      LocalDate today, 
      List<Invoice> invoices) {
    // Implement me!
    return List.of();
  }








  public record PastDue(Invoice value) {}

  record EnrichedCustomer(
      CustomerId id,
      Address address,
      Percent feePercentage,
      PaymentTerms terms,
      CustomerRating rating,
      Optional<Approval> approval
    ) {}

  record CustomerId(String value) {}
  record Percent(double numerator, double denominator) { /*...*/ }

}
