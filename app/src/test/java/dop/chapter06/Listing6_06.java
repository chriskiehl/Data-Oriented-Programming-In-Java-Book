package dop.chapter06;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.format;

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

  @Test
  void example() {
    List<USD> xs = IntStream.range(1, 10000).boxed().map(BigDecimal::new).map(USD::new).toList();
    System.out.println(sum(xs));
    // [out]
    // Adding 0 to 9062   //  ┐
    // Adding 0 to 4531   //  │
    // Adding 1 to 9063   //  │◄── Depending on anything other than the output
    // Adding 0 to 2812   //  ┘    of the function ruins its global determinism.

    // How much does this actually matter?
    //
    // It depends!
  }







  record USD(BigDecimal value) {
    static USD zero() { return new USD(BigDecimal.ZERO); }
    USD add(USD other) {
      System.out.println(format("Adding %s to %s", this.value, other.value));
      return new USD(this.value.add(other.value));
    }
  }

}
