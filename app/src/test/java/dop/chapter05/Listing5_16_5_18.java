package dop.chapter05;

import org.junit.jupiter.api.Test;

public class Listing5_16_5_18 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.16 through 5.18
   * ───────────────────────────────────────────────────────
   * Interesting things happen when we lift the decisions our
   * program makes into the type system. We're suddenly forced
   * to deal with each of those decisions *as a data type*.
   *
   * Our "story" starts to naturally follow those branching paths.
   */
  @Test
  public void example() {
    //
    // List<Invoice>
    // -> List<PastDue>
    // -> LateFeeDraft
    // -> BillableFee -> (BilledLateFee OR RejectedLateFee)
    //    OR NotBillable -> NotBillable  ◄── Keeping the type the same is how we can say "no change"
    //    OR NeedsApproval -> Approval
    //          ▲                ▲
    //          └────────────────┘
    //                  │ The nice thing about really descriptive types is that they largely
    //                  │ push themselves in the right direction.
    //                  │ What kind of output will something that takes a `NeedsApproval` as
    //                  │ input produce? An `Approval`!
  }

}
