package dop.chapter06;

import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static java.time.temporal.ChronoUnit.DAYS;

public class Listing6_24 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.24
   * ───────────────────────────────────────────────────────
   * Using Java’s built-in function that maps time → time
   * ───────────────────────────────────────────────────────
   */
  static TemporalAdjuster gracePeriod(CustomerRating rating) {
    return switch(rating) {
      case CustomerRating.GOOD -> date -> date.plus(60, DAYS);
      case CustomerRating.ACCEPTABLE -> date -> date.plus(30, DAYS);
      case CustomerRating.POOR -> TemporalAdjusters.lastDayOfMonth();
    };
  }








  enum CustomerRating {GOOD, ACCEPTABLE, POOR}

}
