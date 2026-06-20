package dop.chapter05;

import org.junit.jupiter.api.Test;

public class Listing5_15 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.15
   * ───────────────────────────────────────────────────────
   * Using Sum Types, we can capture how Draft late fees actually
   * get used in our program. The decisions our program makes
   * are a core part of the domain.
   */
  @Test
  public void example() {
    //
    // List<Invoice>
    // -> List<PastDue>
    // -> LateFeeDraft
    // -> (BillableFee OR NotBillable OR NeedsApproval)
    //    └───────────────────────────────────────────┘
    //                      │ These capture the three distinct decisions
    //                      │ out program makes
    //
  }

}
