package dop.chapter05;

public class Listing5_39 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.39
   * ───────────────────────────────────────────────────────
   * Restoring expressivity in our data flow
   * ───────────────────────────────────────────────────────
   */
  /*
  List<Invoice>
  -> List<PastDue>
  -> LateFee<Draft>                                       //  ┐
  -> BillableFee -> LateFee<Billed> OR LateFee<Rejected>  //  │◄── We can watch LateFee’s lifecycle information
  -> NeedsApproval -> LateFee<InReview>                   //  ┘    flow through the story
  */

}
