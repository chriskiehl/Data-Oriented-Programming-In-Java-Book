package dop.chapter06;

import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static java.time.temporal.ChronoUnit.DAYS;

public class Listing6_35 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.35
   * ───────────────────────────────────────────────────────
   * Grace Period (we already figured this one out in Listing 6.21)
   * ───────────────────────────────────────────────────────
   */
  static TemporalAdjuster gracePeriod(CustomerRating rating) {
    return switch (rating) {
      case CustomerRating.GOOD -> date -> date.plus(60, DAYS);
      case CustomerRating.ACCEPTABLE -> date -> date.plus(30, DAYS);
      case CustomerRating.POOR -> TemporalAdjusters.lastDayOfMonth();
    };
  }








  enum CustomerRating {GOOD, ACCEPTABLE, POOR}

}
