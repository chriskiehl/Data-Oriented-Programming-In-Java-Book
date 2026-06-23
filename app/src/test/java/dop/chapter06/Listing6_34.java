package dop.chapter06;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static java.time.temporal.ChronoUnit.DAYS;

public class Listing6_34 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.34
   * ───────────────────────────────────────────────────────
   * Checking if an Invoice is past due
   * ───────────────────────────────────────────────────────
   */
  static boolean isPastDue(
      Invoice invoice,
      CustomerRating rating,
      LocalDate today) {
    return invoice.getInvoiceType().equals(STANDARD)
                                  //           ▲
                                  //           └──── This is part of our ongoing fuzzy barrier with the outside
                                  //                 world. We have to make sure these Invoices are the right
                                  //                 type due to database details beyond our control.
        && invoice.getStatus().equals(OPEN)
                            //         ▲
                            //         └──── Now our core constraints. Invoices must still be open (unpaid)
        && today.isAfter(invoice.getDueDate().with(gracePeriod(rating)));
//            ▲
//            └──── And the current date must be after their due date and gracePeriod
    }








  static final InvoiceType STANDARD = InvoiceType.STANDARD;
  static final InvoiceStatus OPEN = InvoiceStatus.OPEN;

  enum CustomerRating {GOOD, ACCEPTABLE, POOR}
  enum InvoiceType {STANDARD}
  enum InvoiceStatus {OPEN}

  interface Invoice {
    InvoiceType getInvoiceType();
    InvoiceStatus getStatus();
    LocalDate getDueDate();
  }

  static TemporalAdjuster gracePeriod(CustomerRating rating) {
    return switch (rating) {
      case CustomerRating.GOOD -> date -> date.plus(60, DAYS);
      case CustomerRating.ACCEPTABLE -> date -> date.plus(30, DAYS);
      case CustomerRating.POOR -> TemporalAdjusters.lastDayOfMonth();
    };
  }

}
