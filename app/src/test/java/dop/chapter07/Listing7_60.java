package dop.chapter07;

import java.util.Optional;
import java.util.function.BinaryOperator;

import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;

public class Listing7_60 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.60
   * ───────────────────────────────────────────────────────
   * Lifting our Algebra into the Optional universe
   * ───────────────────────────────────────────────────────
   */
  static <A> BinaryOperator<Optional<A>> withOptional(
      BinaryOperator<A> operator) {
    return (opt1, opt2) ->
        lift(operator).apply(opt1, opt2)
//        ▲
//        └──── First we lift up to the world of Optional. This will choose
//              a winning value if they’re both defined.
//                    ┌─────────────────────────────┐
            .or(() -> opt1.isPresent() ? opt1 : opt2);
//                    └─────────────────────────────┘
//                                └──── If they’re not both present, maxBy will
//                                      take whichever is more favorable
  }

  static BinaryOperator<Optional<Policy>> addPolicies =         //  ┐
      withOptional(maxBy(comparing(Chapter07::policyImpact)     //  │
          .thenComparing(Policy::name)));                       //  │
                                                                //  │
  static BinaryOperator<Optional<AuditFinding>> addFindings =   //  │◄── Transforming our algebra into
      withOptional(maxBy(comparing(Chapter07::findingsImpact)   //  │    one that’s Optional aware
          .thenComparing(AuditFinding::name)));                 //  │
                                                                //  │
  static BinaryOperator<Optional<Boolean>> addStatuses =        //  │
      withOptional(maxBy(Boolean::compareTo));                  //  ┘








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

  static <A> BinaryOperator<Optional<A>> lift(BinaryOperator<A> f) {
    return (opt1, opt2) -> Optional.empty();
  }

}
