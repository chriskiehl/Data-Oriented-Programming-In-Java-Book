package dop.chapter06;

import java.math.BigDecimal;

public class Listing6_52 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.52
   * ───────────────────────────────────────────────────────
   * Giving “not those other things” its own name
   * ───────────────────────────────────────────────────────
   */
  private enum Assessment {ABOVE_MAXIMUM, BELOW_MINIMUM, WITHIN_RANGE}
//               ▲
//               └──── Introducing an enum to capture that an Assessment is
//                     exactly three things

  static Assessment assessTotal(USD total, Rules rules) {
//                    ▲
//                    └──── And a new function that just handles the logic of
//                          assessing the total and returning its findings
    if (total.value().compareTo(rules.getMinimumFeeThreshold()) < 0){
      return Assessment.BELOW_MINIMUM;
    } else if (total.value().compareTo(rules.getMaximumFeeThreshold()) > 0) {
      return Assessment.ABOVE_MAXIMUM;
    } else {
      return Assessment.WITHIN_RANGE;
    }
  }








  record USD(BigDecimal value) {}
  interface Rules {
    BigDecimal getMinimumFeeThreshold();
    BigDecimal getMaximumFeeThreshold();
  }

}
