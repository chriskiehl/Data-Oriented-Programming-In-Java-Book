package dop.chapter06;

import java.time.LocalDate;

public class Listing6_22 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.22
   * ───────────────────────────────────────────────────────
   * expanding the surface area of our function
   * ───────────────────────────────────────────────────────
   */
  Days gracePeriod(CustomerRating rating, LocalDate dueDate) {
//                                             ▲
//                                             └──── Supplying the “missing” context our function needed in order
//                                                   to handle its final case

    return null; // (so it compiles)
  }








  enum CustomerRating {GOOD, ACCEPTABLE, POOR}

  record Days(int value) {}

}
