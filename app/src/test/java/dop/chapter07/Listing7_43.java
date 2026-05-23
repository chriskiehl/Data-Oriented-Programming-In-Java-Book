package dop.chapter07;

public class Listing7_43 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.43
     * ───────────────────────────────────────────────────────
     * Whoops! Compares by ordinal rather than name
     * ───────────────────────────────────────────────────────
     */
    // BUG
    static AuditFinding add(AuditFinding x, AuditFinding y) {
        // (below is the original implementation before we
        //  started offloading to comparator)
        if (findingsImpact(x).equals(findingsImpact(y))) {
            return x.compareTo(y) >= 0 ? x : y;
//                 ▲           ▲
//                 │           │
//                 └──── Super easy mistake – we compared by ordinal when we
//                       meant to compare by name
        } else {
            if (findingsImpact(x).equals(CustomerImpact.FAVORS)) {
                return x;
            } else {
                return y;
            }
        }
    }














    enum CustomerImpact { HARMS, FAVORS }

    enum AuditFinding {
        BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
    }

    static CustomerImpact findingsImpact(AuditFinding finding) {
        return CustomerImpact.FAVORS;
    }
}
