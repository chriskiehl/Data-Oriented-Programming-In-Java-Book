package dop.chapter07;

import java.util.function.BinaryOperator;

import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;

public class Listing7_47 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.47
     * ───────────────────────────────────────────────────────
     * Refactoring to static definitions
     * ───────────────────────────────────────────────────────
     */
    static BinaryOperator<Policy> addPolicies =           //   ┐
        maxBy(comparing(Chapter07::policyImpact)          //   │
            .thenComparing(Policy::name));                //   │  If we were so inclined, we could implement everything
                                                          //   │  this way. It is better? Worse? I leave it up to you.
    static BinaryOperator<AuditFinding> addFindings =     //   │
        maxBy(comparing(Chapter07::findingsImpact)        //   │  It's neat that we can do it, though.
            .thenComparing(AuditFinding::name));          //   │
                                                          //   ┘
    static BinaryOperator<Boolean> addPremiumStatus = Boolean::logicalOr;
















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
