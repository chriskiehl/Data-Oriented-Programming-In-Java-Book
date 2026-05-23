package dop.chapter07;

import java.util.List;
import java.util.Optional;

public class Listing7_1 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.1
     * ───────────────────────────────────────────────────────
     * A starting point
     * ───────────────────────────────────────────────────────
     */
    public enum Policy {   //  ◄──────────┐
        GRACE_PERIOD,               //    │
        FLEXIBLE,                   //    │
        IMMEDIATE,                  //    │
        STRICT,                     //    │
        MANUAL_REVIEW               //    │
    }                               //    │
//  ▲                               //    │
//  └───                            //    │
                                    //    │
    public enum AuditFinding { //◄────────┘
        NO_ISSUE,
        INACCURATE,
        BILLING_ERROR,
        OUT_OF_COMPLIANCE
    }

//          ┌─── This is the core model for representing
//          ▼    an individual row in our data set
    record RawData(
        String id,
        Optional<Policy> policy,
        Optional<AuditFinding> findings,
        Optional<Boolean> isPremium
//                  ▲
//                  └──── Note that the Boolean is also wrapped in an
//                        optional. “We don’t know” is a valid state
    ){}

    enum CustomerImpact { HARMS, FAVORS }                          //  ┐
    static CustomerImpact policyImpact(Policy stage) {             //  │
      return switch (stage) {                                      //  │
        case GRACE_PERIOD, FLEXIBLE -> CustomerImpact.FAVORS;      //  │  These functions tell us whether a
        default -> CustomerImpact.HARMS;                           //  │  given policy/finding "harms" or "favors"
      };                                                           //  │  the customer
    }                                                              //  │
    static CustomerImpact findingsImpact(AuditFinding stage) {     //  │
      return switch (stage) {                                      //  │
        case NO_ISSUE, INACCURATE -> CustomerImpact.FAVORS;        //  │
        default -> CustomerImpact.HARMS;                           //  │
      };                                                           //  ┘
    }

    static List<RawData> cleanDuplicates(List<RawData> rows) {
//                             ▲
//                             └──── Here's an example starting point. We're ignoring
//                                   how we get this data so we can focus just on what
//                                   we do with it. So… what do we do with it?
        // [your implementation here]
        return null; 
    }
}
