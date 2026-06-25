package dop.chapter07;

import java.util.function.BinaryOperator;

import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;

public class Listing7_49 {
  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.49
   * ───────────────────────────────────────────────────────
   * The design of our algebraic binary operators assumes data will be present
   * ───────────────────────────────────────────────────────
   */
  static BinaryOperator<Listing7_47.Policy> addPolicies =               //   ┐
      maxBy(comparing(Listing7_47.Chapter07::policyImpact)              //   │
              .thenComparing(Listing7_47.Policy::name));                //   │ We "forgot" to handle Optional!
  static BinaryOperator<Listing7_47.AuditFinding> addFindings =         //   │
      maxBy(comparing(Listing7_47.Chapter07::findingsImpact)            //   │ Now what do we do?
              .thenComparing(Listing7_47.AuditFinding::name));          //   │
  static BinaryOperator<Boolean> addPremiumStatus = Boolean::logicalOr; //   ┘








  enum Policy {
    GRACE_PERIOD, FLEXIBLE, IMMEDIATE, STRICT, MANUAL_REVIEW
  }

  enum AuditFindings {
    BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
  }

}
