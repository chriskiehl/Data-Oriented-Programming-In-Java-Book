package dop.chapter06;

import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class Listing6_40 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.40
   * ───────────────────────────────────────────────────────
   * computing the due date
   * ───────────────────────────────────────────────────────
   */
  static LocalDate dueDate(LocalDate today, PaymentTerms terms) {
    return switch (terms) {
      case PaymentTerms.NET_30 -> today.plusDays(30);
      case PaymentTerms.NET_60 -> today.plusDays(60);
      case PaymentTerms.DUE_ON_RECEIPT -> today;
      case PaymentTerms.END_OF_MONTH -> today.with(lastDayOfMonth());
    };
  }








  enum PaymentTerms {NET_30, NET_60, DUE_ON_RECEIPT, END_OF_MONTH}

}
