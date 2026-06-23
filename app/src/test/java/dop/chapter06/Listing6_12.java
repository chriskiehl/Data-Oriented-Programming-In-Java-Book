package dop.chapter06;

import java.time.LocalDate;

public class Listing6_12 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.12
   * ───────────────────────────────────────────────────────
   * The constant allure. If it’s in scope, someone will reach for it
   * ───────────────────────────────────────────────────────
   */
  class FeeService {
    private RatingsAPI ratingsApi;

    LocalDate figureOutDueDate(LocalDate today, PaymentTerms terms) {
//                  ▲
//                  └──── This method is acting as a deterministic function, but
//                        nothing cues people who work on the code that we want it
//                        to stay that way.
      return switch (terms) {
        // [beautiful, clean, deterministic implementation here]

        default -> LocalDate.now(); // (Not part of the listing. Just hereto compile)
      };
    }


    LocalDate figureOutDueDateV2(LocalDate today, PaymentTerms terms) {
      Rating rating = this.ratingsApi.getRating();
//                              ▲
//                              └──── this becomes a very “obvious” change to make if we pick up
//                                    a requirement that due Dates should also depend on the
//                                    customer's current rating.
      return switch (terms) {
        // use the result here.
        // ...

        default -> LocalDate.now(); // (Not part of the listing. Just hereto compile)
      };
    }
  }















  interface RatingsAPI {
    Rating getRating();
  }

  enum Rating {GOOD}

  enum PaymentTerms {NET_30}

}
