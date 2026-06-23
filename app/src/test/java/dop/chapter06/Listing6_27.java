package dop.chapter06;

import java.time.LocalDate;

public class Listing6_27 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.27
   * ───────────────────────────────────────────────────────
   * The PastDue data type (and its dependency on an identity object)
   * ───────────────────────────────────────────────────────
   */
  record PastDue(Invoice invoice) {}








  interface Invoice {
    LocalDate dueDate();
  }

}
