package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static dop.chapter11.Listing11_37.Region.AMER;
import static dop.chapter11.Listing11_37.Segment.ENTERPRISE;
import static dop.chapter11.USD.ONE;

public class Listing11_37 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.37
     * ───────────────────────────────────────────────────────
     * Using Withers to customize test data
     * ───────────────────────────────────────────────────────
     */
    record Account(AccountId id,
                   Region region,
                   USD spend,
                   Segment segment,
                   Sector sector,
                   Instant updatedOn
    ) {
        // The "wither" pattern.
        // These will one day be baked directly into java!
        Account withSegment(Segment segment) {
//                                               ┌───────┐
            return new Account(id, region, spend, segment, sector, updatedOn);
//                                               └───────┘
    }
        Account withRegion(Region region) {
//                                ┌──────┐
            return new Account(id, region, spend, segment,sector, updatedOn);
//                                └──────┘
    }
        Account withSpend(USD spend) {
//                                        ┌─────┐
            return new Account(id, region, spend, segment,sector, updatedOn);
//                                        └─────┘
      }
    }


    @Test
    void myTestThatNeedsEnterpriseAccounts() {
        Account account = mkAcnt().withSegment(ENTERPRISE);
        //              ▲
        //              └──── As before, we start with randomized records and
        //                    then tailor them to our test
        // rest of test
    }

    @Test
    void noDiscountForLargeAccountsInAMER() {
        // ...
        USD threshold = USD.valueOf(1200.00);
        Account aboveLimit = mkAcnt()          //  ┐
            .withRegion(region)                //  │◄── Now we can start with completely
            .withSpend(threshold.plus(ONE));      //  │    randomized records and then tailor
            //…                                //  ┘    them to our test
    }














    Region region;
    record AccountId(){}
    enum Region {AMER}
    enum Segment {ENTERPRISE}
    record Sector(){}
    Account __;
    Account mkAcnt() { return __; }
    record Threshold(double value) {
        double plus(double n) { return value + n; }
    }
}
