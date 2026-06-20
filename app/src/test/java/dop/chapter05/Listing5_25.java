package dop.chapter05;

import java.time.LocalDate;

import static dop.chapter05.the.existing.world.Entities.Invoice;

public class Listing5_25 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.25
   * ───────────────────────────────────────────────────────
   * Tracking more data just to enforce semantic integrity
   * ───────────────────────────────────────────────────────
   */
  record PastDue (
      Invoice invoice,
      LocalDate lateAsOf  //  ◄────────────────────────────┐
  ) {                     //                               │ If we make the data type carry additional
    PastDue {             //                               │ information,we can use that to enforce semantic
      if (invoice.getDueDate().isBefore(lateAsOf)) {  // ◄─┘ integrity during construction.
        // and is a standard invoice
        // and is open
        // and ...
      }
    }
  }

}
