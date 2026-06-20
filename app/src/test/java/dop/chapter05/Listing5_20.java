package dop.chapter05;

import java.util.List;

import org.junit.jupiter.api.Test;

import dop.chapter05.the.existing.world.Entities.Invoice;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.Approval;

public class Listing5_20 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.20
   * ───────────────────────────────────────────────────────
   * We use this design to guide our implementation.
   * Each arrow can be turned directly into a method.
   */
  @Test
  public void example() {
    //
    //      ┌──── (this class is here just so we can show the method definitions)
    //      ▼
    class ___ {
      List<PastDue> collectPastDue(List<Invoice> invoices) { return null; } // ◄──┐ (All return null just so they
      LatefeeDraft buildTheDraft(List<PastDue> invoices) { return null; }   //    │ will compile)
      ReviewedDraft assessTheDraft(LatefeeDraft invoice) { return null; }
      BillingResult submitBill(LatefeeDraft draft) { return null; }
      Approval startApproval(NeedsReview needsReview) { return null; }
    }
  }



  // Note: We don't define these in the listing. They're
  // just here as minimal shims to enable compilation.
  record PastDue(){}
  record LatefeeDraft(){}
  record ReviewedDraft(){}
  record BillingResult(){}
  record NeedsReview(){}

}
