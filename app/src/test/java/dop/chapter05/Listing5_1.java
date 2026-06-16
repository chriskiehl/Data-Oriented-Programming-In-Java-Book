package dop.chapter05;

import java.util.Optional;

/**
 * Chapter 5 takes all the modeling tools we've explored
 * so far and applies them to building a complex feature.
 * No more simple domains. No more isolated modeling. We
 * dive into the messy world of building software. That
 * means everything that makes it hard: databases, ORMS,
 * third party services (with APIs we don't control), and
 * the absolute worst thing of all: prior decisions.
 *
 * We'll learn how to work with all of these limitations
 * and produce clean, clear, data-oriented code.
 */
public class Listing5_1 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.1
   * ───────────────────────────────────────────────────────
   * We're implementing the feature in an application that
   * already "exists." So, the first thing we do in the chapter
   * is set the stage.
   *
   * This pretend app is "modern" and "service oriented." These
   * are the external APIs we'll interact with.
   * We cheat a bit and ignore stuff like HTTP and failures.
   */
  interface RatingsAPI {    //  ◄───────────────────────────────┐
    enum CustomerRating {GOOD, ACCEPTABLE, POOR}
    CustomerRating getRating(String customerId);   //           │ These are the easiest. They’re read only.
  }

  interface ContractsAPI {  //  ◄───────────────────────────────┘
    enum PaymentTerms {
      NET_30, NET_60,
      END_OF_MONTH, DUE_ON_RECEIPT}
    PaymentTerms getPaymentTerms(String customerId);
  }

  interface ApprovalsAPI {
    enum Status {PENDING, APPROVED, DENIED}
    record Approval(String id, Status status){}
    record CreateApprovalRequest(/*...*/){}
    Approval createApproval(CreateApprovalRequest request); //  ◄──┐ Approvals are more complicated. We have to
    Optional<Approval> getApproval(String approvalId);      //  ◄──┘ manage two APIs (read and write).
  }

  interface BillingAPI {
    enum Status {ACCEPTED, REJECTED}
    record SubmitInvoiceRequest(/*...*/) {}
    record BillingResponse(
        Status status,
        String invoiceId,
        String error
    ) {}
    BillingResponse submit(SubmitInvoiceRequest request);  // ◄──┐ This is the most important and dangerous API in
    //                                                           │ our codebase. Invoices go straight to the customer
    //                                                           │ with no “undo” button
  }

}
