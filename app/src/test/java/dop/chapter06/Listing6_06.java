package dop.chapter06;

import java.math.BigDecimal;
import java.util.List;

public class Listing6_06 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.6
   * ───────────────────────────────────────────────────────
   * The observation of this function is non-deterministic
   * ───────────────────────────────────────────────────────
   */
  public USD sum(List<USD> amounts) {
    return amounts.stream().parallel()   //  ┐
        .reduce(USD.zero(), USD::add);   //  │◄── The same input will emit logs in a different order
                                         //  ┘    each time it's run
  }
  // [out]
  // Adding 0 to 9062   //  ┐
  // Adding 0 to 4531   //  │
  // Adding 1 to 9063   //  │◄── Depending on anything other than the output
  // Adding 0 to 2812   //  ┘    of the function ruins its global determinism.

  // How much does this actually matter?
  //
  // It depends!








  record USD(BigDecimal value) {
    static USD zero() { return new USD(BigDecimal.ZERO); }
    static USD add(USD x, USD y) {
      return new USD(x.value().add(y.value()));
    }
  }

}
