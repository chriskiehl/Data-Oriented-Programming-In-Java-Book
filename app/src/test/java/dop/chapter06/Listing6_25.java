package dop.chapter06;

public class Listing6_25 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.25
   * ───────────────────────────────────────────────────────
   * Code that reads just like the requirements
   * ───────────────────────────────────────────────────────
   */
  // The requirement
  // A0 - The system shall charge late fees to customers with
  // open [[past due invoices]] as of the [[Evaluation Date]] plus the
  // customer's [[Grace Period]]

  // The Code:
  static boolean isPastDue(/*...*/) {
    //     ┌── Evaluation Date!                         ┌── Grace Period!
    //     ▼                                            ▼
    // evaluationDate.isAfter(invoice.dueDate().with(gracePeriod(rating));
    //                                   ▲
    //                                   └─ Due date!

    return false;  // (so it compiles)
  }

}
