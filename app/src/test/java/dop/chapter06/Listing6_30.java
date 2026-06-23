package dop.chapter06;

public class Listing6_30 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.30
   * ───────────────────────────────────────────────────────
   * What each function depends on
   * ───────────────────────────────────────────────────────
   */
  /*
                              NOTES.md
  -----------------------------------------------------------------
  collectPastDue depends on:
    (input) EnrichedCustomer
    (input) Current Date
    (input) List<Invoice>

  buildDraft depends on:
    (input) EnrichedCustomer
    (input) List<PastDue>
    (input) CurrentDate

  assessDraft depends on:
    (input) LateFee<Draft>
    (input) Rules


  submitBill depends on:
    (input) Billable
    (external API) BillingService

  startApproval depends on:
    (input) NeedsReview
    (external API) ApprovalsService

  loadInvoicingData depends on:
    (environment) Current Date
    (internal API) CustomerFacade
    (internal API) Rules


  processLatefees depends on:
    (Pretty much everything)
  */

}
