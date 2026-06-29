package dop.chapter07;

import java.util.Comparator;
import java.util.function.BinaryOperator;

import static java.util.Comparator.comparing;

public class Listing7_46 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.46
   * ───────────────────────────────────────────────────────
   * BinaryOperator’s MaxBy
   * ───────────────────────────────────────────────────────
   */
  public static <T> BinaryOperator<T> maxBy(Comparator< T> comparator) {
//                                    ▲
//                                    └──── MaxBy is built into Java’s
//                                          BinaryOperator interface
//        ┌──────┐  ┌──────────────────────────────────────┐
    return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
//        └──────┘  └──────────────────────────────────────┘
//         ▲                   ▲
//         └──── It turns comparators into Binary operations
  }

  static Policy add(Policy x, Policy y) {
    Comparator<Policy> comparator =
        comparing(Chapter07::policyImpact)
            .thenComparing(Policy::name);
    return maxBy(comparator).apply(x, y);
//         ▲
//         └──── Refactoring to use maxBy. This doesn’t change any
//               behavior, only make it explicit
  }
  static AuditFinding add(AuditFinding x, AuditFinding y) {
      Comparator<AuditFinding> comparator =
        comparing(Chapter07::findingsImpact)
            .thenComparing(AuditFinding::name);
    return maxBy(comparator).apply(x, y);
//         ▲
//         └──── Refactoring to use maxBy. This doesn’t change any
//               behavior, only make it explicit
  }








  enum CustomerImpact { HARMS, FAVORS }

  enum Policy {
    GRACE_PERIOD, FLEXIBLE, IMMEDIATE, STRICT, MANUAL_REVIEW
  }

  enum AuditFinding {
    BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
  }

  static class Chapter07 {
    static CustomerImpact policyImpact(Policy policy) {
      return CustomerImpact.FAVORS;
    }

    static CustomerImpact findingsImpact(AuditFinding finding) {
      return CustomerImpact.FAVORS;
    }
  }

}
