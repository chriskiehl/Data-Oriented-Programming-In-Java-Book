package dop.chapter06;

public class Listing6_21 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.21
   * ───────────────────────────────────────────────────────
   * Does throwing more data at the problem help?
   * ───────────────────────────────────────────────────────
   */
  Days gracePeriod(CustomerRating rating) {
    return switch(rating) {
      case CustomerRating.GOOD -> new Days(60);
      case CustomerRating.ACCEPTABLE -> new Days(30);
      case CustomerRating.POOR -> /* ??? */ null; /* ??? */
//                                      ▲
//                                      └──── Hmm. What goes here? This is very
//                                            different from the other cases
    };
  }








  enum CustomerRating {GOOD, ACCEPTABLE, POOR}

  record Days(int value) {}

}
