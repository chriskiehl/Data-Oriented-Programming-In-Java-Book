package dop.chapter05;

import org.junit.jupiter.api.Test;

public class Listing5_08_to_5_10 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.8 through 5.10
   * ───────────────────────────────────────────────────────
   * The design work is refining the "story" that our data types communicate.
   *
   * You might imagine explaining the feature to a coworker. Actually just
   * putting the requirements to words (rather than code) tends to quickly
   * clarify when your story is "saying" the wrong thing or missing details.
   */
  @Test
  public void example() {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.8
     * ───────────────────────────────────────────────────────
     * Refining how late fees get produced in our system
     * ───────────────────────────────────────────────────────
     */
    // List<Invoice>
    // -> List<PastDue>   ◄──────┐ This is what was missing from out first
    // -> LateFeeInvoice         │ description. We don't process ANY invoices, we
    //                           │ process *Past Due* invoices.
    //
    // Is this the whole story? Would the imaginary coworker we're explaining
    // all of this to have a good mental model at this point?
    // Probably not! There's still much left unsaid.
    //
    // We keep refining

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.9
     * ───────────────────────────────────────────────────────
     * Introducing the draft lifecycle
     * ───────────────────────────────────────────────────────
     */
    //
    // List<Invoice>
    // -> List<PastDue>
    // -> DraftLateFee    ◄──────┐ Another clarification. Our service is just a
    // -> LateFeeInvoice         │ middleman. We don't make the late fees directly.
    //                           │ We make a description of a latefee we want to have later.
    //
    // If you mentally swap those arrows (->) for something like "and then we use
    // that to make..." you the story this data is telling becomes explicit

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.10
     * ───────────────────────────────────────────────────────
     * In plain English
     * ───────────────────────────────────────────────────────
     */
    //  (we start with) List<Invoice>
    //  (and then we use that to make) List<PastDue>
    //  (and then we use that to make a) LateFeeDraft
    //  (and then we use that to make a) LateFeeInvoice
    //
  }

}
