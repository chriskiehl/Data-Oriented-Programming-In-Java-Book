package dop.chapter05;

import org.junit.jupiter.api.Test;

public class Listing5_11 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.11
   * ───────────────────────────────────────────────────────
   * The complexity of the original implementation hides what's
   * ultimately simple business rules.
   */
  @Test
  public void example() {
    /*
    if (latefee.compareTo(config.minimumFeeThreshold()) <= 0) {   ◄──┐
      // [logic omitted]                                             │
    } else {                                                         │
      if (latefee.compareTo(config.maximumFeeThreshold()) > 0) {  ◄──│  This mess of nested if/else conditions
        if (customer.getApprovalId().isEmpty()) {                    │  is just deciding what to do with a Draft.
          // [logic omitted]                                         │  Can we bill it?
        } else {                                                  ◄──│  Do we need an approval to do so?
          if (status.equals(ApprovalStatus.PENDING)) {            ◄──│  Is it not worth billing at all?
            // [logic omitted]                                       │
          } else if (status.equals(ApprovalStatus.DENIED)) {      ◄──┘
            // [logic omitted]
          }
        }
        // [logic omitted]
      }
      // [logic omitted]
    }
    */
  }

}
