package dop.chapter11;

import java.math.BigDecimal;
import java.util.Optional;

public class Listing11_54 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.54
     * ───────────────────────────────────────────────────────
     * A data type that can not have all of its states enumerated
     * ───────────────────────────────────────────────────────
     */
    public enum Policy {
        GRACE_PERIOD, FLEXIBLE, IMMEDIATE, STRICT, MANUAL_REVIEW
    }
    public enum AuditFinding {
        NO_ISSUE, INACCURATE, BILLING_ERROR, OUT_OF_COMPLIANCE
    }
    public record RawData(
        Optional<Policy> policy,           //  ┐
        Optional<AuditFinding> findings,   //  │◄── Every state in these can be
        Optional<Boolean> premiumStatus,   //  ┘    trivially enumerated
        USD total,                         //  ┐
        String tag                         //  ┘◄── But these cannot be. We can only
    ){}                                    //       approximate by generating enough samples

    static RawData add(RawData a, RawData b) {     //  ┐
        // complicated merging logic from          //  │
        // chapter 7 elided                        //  │◄── As a refresher, we were building
        return __;                                 //  │    toward an implementation of this method
    }                                              //  │    which “adds” two rows of data together
                                                   //  ┘    while satisfying algebraic properties






    


















    static RawData __;


}
