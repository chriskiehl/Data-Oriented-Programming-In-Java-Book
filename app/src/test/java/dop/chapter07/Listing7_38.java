package dop.chapter07;

import java.util.function.Function;

public class Listing7_38 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.38
     * ───────────────────────────────────────────────────────
     * DRY == Better?
     * ───────────────────────────────────────────────────────
     */
    static <E extends Enum<E>> E add(E x, E y, Function<E, CustomerImpact> f) {
        if (f.apply(x).equals(f.apply(y))) {
            return x.name().compareTo(y.name()) >= 0 ? x : y;
        } else {
            return f.apply(x).equals(CustomerImpact.FAVORS) ? x : y;
        }
    }


    static Policy add(Policy x, Policy y) {
        return add(x, y, Starting::policyImpact);             //  ┐
    }                                                         //  │◄── Replacing the duplicated logic with
    static AuditFinding add(AuditFinding x, AuditFinding y) { //  │    our new function
        return add(x, y, Starting::findingsImpact);           //  ┘
    }
    static Boolean add(Boolean x, Boolean y) {
        return x || y;
    }














    enum CustomerImpact { HARMS, FAVORS }

    enum Policy {
        GRACE_PERIOD, FLEXIBLE, IMMEDIATE, STRICT, MANUAL_REVIEW
    }

    enum AuditFinding {
        BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
    }

    static class Starting {
        static CustomerImpact policyImpact(Policy policy) {
            return CustomerImpact.FAVORS;
        }

        static CustomerImpact findingsImpact(AuditFinding finding) {
            return CustomerImpact.FAVORS;
        }
    }
}
