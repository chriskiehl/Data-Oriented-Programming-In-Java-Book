package dop.chapter06;

import java.time.LocalDate;

public class Listing6_07 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.7
   * ───────────────────────────────────────────────────────
   * Is this original implementation from Chapter 05 deterministic?
   * ───────────────────────────────────────────────────────
   */
  private LocalDate figureOutDueDate() {

    LocalDate currentDate = LocalDate.now();
    //                                 ▲
    //                                 └────┌──── These two actions make the function non-deterministic.
    //                                      │     Each time we run the function we'll get a different answer.
    //                                      ▼
    PaymentTerms terms = contractsClient.getTerms(invoice.getCustomerId());
//
//      Contrast that with everything else in the function.
//      This is completely deterministic
//                             │
//                             │
//    ┌────────────────────────────────────────────────────┐
    return switch (terms) {
      case PaymentTerms.NET_30 -> currentDate.plusDays(30);
      case PaymentTerms.NET_60 -> currentDate.plusDays(60);
      // and so on
    };
  }












  Invoice invoice;
  LocalDate currentDate;
  PaymentTerms terms;
  ContractsClient contractsClient;

  enum PaymentTerms {NET_30, NET_60}

  interface Invoice {
    String getCustomerId();
  }

  interface ContractsClient {
    PaymentTerms getTerms(String customerId);
  }

}
