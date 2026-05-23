package dop.chapter07;

import java.util.Comparator;

import static java.util.Comparator.comparing;

public class Listing7_45 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.45
     * ───────────────────────────────────────────────────────
     * What makes our functions monotonic
     * ───────────────────────────────────────────────────────
     */
    static Policy add(Policy x, Policy y) {
        Comparator<Policy> comparator =
            comparing(Chapter7::policyImpact)
                .thenComparing(Policy::name);
        return comparator.compare(x, y) > 0 ? y : x;
//                                            ▲
//                                            └──── Only taking the largest value
    }
    static AuditFinding add(AuditFinding x, AuditFinding y) {
        Comparator<AuditFinding> comparator =
            comparing(Chapter7::findingsImpact)
                .thenComparing(AuditFinding::name);
        return comparator.compare(x, y) > 0 ? y : x;
//                                            ▲
//                                            └──── Only taking the largest value
    }























    enum CustomerImpact { HARMS, FAVORS }

    enum Policy {
        GRACE_PERIOD, FLEXIBLE, IMMEDIATE, STRICT, MANUAL_REVIEW
    }

    enum AuditFinding {
        BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
    }

    static class Chapter7 {
        static CustomerImpact policyImpact(Policy policy) {
            return CustomerImpact.FAVORS;
        }

        static CustomerImpact findingsImpact(AuditFinding finding) {
            return CustomerImpact.FAVORS;
        }
    }
}
