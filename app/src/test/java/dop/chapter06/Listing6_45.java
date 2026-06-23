package dop.chapter06;

import java.math.BigDecimal;

public class Listing6_45 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.45
   * ───────────────────────────────────────────────────────
   * Our model from Chapter 5
   * ───────────────────────────────────────────────────────
   */
  ReviewedFee example(LateFee draft, Rules rules) {
    BigDecimal total = draft.total().value();
    if (total.compareTo(rules.minimumFeeThreshold()) < 0){
                        //      ▲
                        //      └──── Here are the border cases. Too low? can’t bill. Too high?
                        //            Well… we’ll get to that below
      return new NotBillable(draft, new Reason("..."));
    }
    else if (total.compareTo(rules.maximumFeeThreshold()) > 0) {
      // we’ll come back to this one
      return null; //(to compile)
    }
    else {
      return new Billable(draft);
    //               ▲
    //               └──── Implicitly, if we make it here then we’re in the
    //                     goldilocks zone and can freely bill this customer
    }
  }








  sealed interface ReviewedFee {}
  record Billable(LateFee draft) implements ReviewedFee {}
  record NotBillable(LateFee draft, Reason reason) implements ReviewedFee {}
  record Reason(String value) {}
  record LateFee(USD total) {}
  record USD(BigDecimal value) {}
  record Rules(BigDecimal minimumFeeThreshold, BigDecimal maximumFeeThreshold) {}

}
