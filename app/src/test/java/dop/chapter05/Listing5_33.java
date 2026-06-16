package dop.chapter05;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Listing5_33 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.33
   * ───────────────────────────────────────────────────────
   * patching around our own modeling limitations
   * ───────────────────────────────────────────────────────
   */
  interface HasDetails {   // ◄── Ham-fisting in an interface that mirrors a record’s accessor
    Details details();
  }
  sealed interface LateFee extends HasDetails {  // ◄── Then having our Latefee interface extend it
    record Draft (Details details) implements LateFee {}
    record Billed (Details details /*...*/) implements LateFee {}
    record Rejected(Details details /*...*/) implements LateFee {}
    record InReview(Details details /*...*/) implements LateFee {}
  }

  Map<LateFee, USD> totalsByLifecycle(List<LateFee> fees) {
//  fees.stream()
//      .map(fee -> fee.details().total()); // ◄── Now we can access the details attribute without pattern matching.
//      ...
//  Hooray…?

    return null;
  }






  record Details(USD total) {}
  record USD(BigDecimal value) {}

}
