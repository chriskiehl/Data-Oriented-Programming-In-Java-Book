package dop.chapter06;

import java.time.LocalDate;

public class Listing6_23 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.23
   * ───────────────────────────────────────────────────────
   * Same inputs; Same outputs
   * ───────────────────────────────────────────────────────
   */
  void example() {
    //  The same inputs will always give the same outputs
    gracePeriod(CustomerRating.GOOD).apply(dueDate);
  }








  LocalDate dueDate;

  GracePeriod gracePeriod(CustomerRating rating) { return null; }

  enum CustomerRating {GOOD, ACCEPTABLE, POOR}

  interface GracePeriod {
    LocalDate apply(LocalDate date);
  }

}
