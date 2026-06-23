package dop.chapter06;

import java.time.LocalDate;

public class Listing6_09 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.9
   * ───────────────────────────────────────────────────────
   * Converting to a deterministic function
   * ───────────────────────────────────────────────────────
   */
  private LocalDate figureOutDueDate(
    LocalDate currentDate,
    PaymentTerms terms
//              ▲
//              └────┐ We remove the dependencies that were robbing us of
//                   │ determinism and take the data we need as input.
) {
//    LocalDate today = LocalDate.now();
//    PaymentTerms terms = contractsClient.getTerms(...);
//                 ▲
//                 └──── These choices are gone now. Nothing reaches out into the world.

//
//    Everything below here is deterministic. Same inputs.
//    Same outputs. Every time. Forever.
    return switch (terms) {
        case PaymentTerms.NET_30 -> currentDate.plusDays(30);
        case PaymentTerms.NET_60 -> currentDate.plusDays(60);
    };
  }









  enum PaymentTerms {NET_30, NET_60}

}
