package dop.chapter05;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Listing5_32 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.32
   * ───────────────────────────────────────────────────────
   * A little ad-hoc pattern matching
   * ───────────────────────────────────────────────────────
   */
  Map<LateFee, USD> totalsByLifecycle(List<LateFee> fees) {
//  fees.stream()
//      .map(fee -> switch(fee) {
//        case Draft d -> d.details();       //  ┐
//        case Billed b -> b.details();      //  │
//        case Rejected r -> r.details();    //  │◄── Ugh...
//        case InReview u -> u.details();    //  ┘
//      })
//  // ...
    return null; // (so the example compiles)
  }






  sealed interface LateFee {}
  record Draft(Details details) implements LateFee {}
  record Billed(Details details) implements LateFee {}
  record Rejected(Details details) implements LateFee {}
  record InReview(Details details) implements LateFee {}
  record Details(USD total) {}
  record USD(BigDecimal value) {}

}
