package dop.chapter05;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static dop.chapter05.the.existing.world.Entities.Invoice;

public class Listing5_27_to_5_28 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.27
   * ───────────────────────────────────────────────────────
   * One approach: duplicating all of the common fields across each data type
   * ───────────────────────────────────────────────────────
   */
  // Is this good modeling?
  // I'd argue it's not terrible. It enables some really
  // powerful things. However, it's extremely clunky from
  // an ergonomics perspective. We've got a ton of repeated
  // fields between each data type.
  record DraftLateFee(
      String customerId,           //  ┐
      USD total,                   //  │
      LocalDate invoiceDate,       //  │◄── Everything is duplicated
      LocalDate dueDate,           //  │
      List<PastDue> includedInFee  //  ┘
  ) {}

  record BilledLatefee(
      InvoiceId id,  // ◄── Billed late fees only have one novel attribute: the invoice ID
      String customerId,
      USD total,
      LocalDate invoiceDate,
      LocalDate dueDate,
      List<PastDue> includedInFee
  ) {}

  record RejectedLatefee(
      Reason reason,     //  ◄──┐ Ditto for these. Other than this one extra piece
      String customerId, //  ◄──┐ of info, they’re exactly the same as the others.
      USD total,
      LocalDate invoiceDate,
      LocalDate dueDate,
      List<PastDue> includedInFee
  ) {}

  record InReviewLatefee(
      ApprovalId approvalId,
      String customerId,           //  ┐
      USD total,                   //  │
      LocalDate invoiceDate,       //  │◄── Everything is duplicated
      LocalDate dueDate,           //  │
      List<PastDue> includedInFee  //  ┘
  ) {}


  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.28
   * ───────────────────────────────────────────────────────
   * The representation allows us to enforce that only **Drafts**
   * can be assessed
   * ───────────────────────────────────────────────────────
   */
  // They're clunky, but their modeling gives us power.
  // We can specify exactly which methods operate on which
  // lifecycle states.
  // For instance:
  class Example {
    //
    //                          ┌── We only assess DRAFT late fees!
    //                          ▼
    ReviewedFee assessDraft(DraftLateFee fee) {
      // ...
      return null;
    }
  }
  // However, they remain too clunky to use in practice.
  // Developers recoil from this kind of duplication.






  record PastDue(Invoice invoice) {}
  record USD(BigDecimal value) {}
  record InvoiceId(String value) {}
  record Reason(String value) {}
  record ApprovalId(String value) {}
  record ReviewedFee() {}

}
