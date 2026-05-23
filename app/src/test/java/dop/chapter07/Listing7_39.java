package dop.chapter07;

import java.util.Comparator;

import static java.util.Comparator.comparing;

public class Listing7_39 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.39
     * ───────────────────────────────────────────────────────
     * Refactoring to express our logic in terms of ordering
     * ───────────────────────────────────────────────────────
     */
    static Policy add(Policy x, Policy y) {
        Comparator<Policy> comparator =                        //  ┐
            comparing(Chapter7::policyImpact)                  //  │◄── Expressing our problem in
                .thenComparing(Policy::name);                  //  ┘    terms of Comparator
        return comparator.compare(x, y) > 0 ? y : x;
    }
    static AuditFinding add(AuditFinding x, AuditFinding y) {
        Comparator<AuditFinding> comparator =                  //  ┐
            comparing(Chapter7::findingsImpact)                //  │◄── Our refactor has about the
                .thenComparing(AuditFinding::name);            //  ┘    same number of lines of code,
                                                               //       but our implementation is reading much more
                                                               //       like our requirements. “Sort by favorability,
                                                               //       and then name”
        return comparator.compare(x, y) > 0 ? y : x;
    }
    static Boolean add(Boolean x, Boolean y) {
        // Boolean.compare(x, y) > 0 ? x : y;
//      ▲
//      └──── While we could express the “more favorable” on Booleans in
//            terms of a comparator, it’s hard to beat the familiarity
//            of logical OR.
        return x || y;
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
