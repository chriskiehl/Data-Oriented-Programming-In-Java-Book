package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static dop.chapter11.Listing11_33.Region.AMER;
import static dop.chapter11.Listing11_33.Segment.ENTERPRISE;

public class Listing11_33 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.33
     * ───────────────────────────────────────────────────────
     * We back to the original problem. We depend on things not in the test
     * ───────────────────────────────────────────────────────
     */
    Account mkAcnt() {
        return mkAcnt(Region.AMER, USD.valueOf(1_500_000));
        //                 ▲              ▲
        //                 └──── We "don't need" these values in our test, so we set them to
        //                       something that seems "reasonable."
    }

    Account mkAcnt(Region region, USD spend) {
        return new Account(
           new AccountId("000000001"),
           region,
           spend,
//        ┌────────────────────┐
           Segment.ENTERPRISE,
//        └────────────────────┘
//                 ▲
//                 └──── Hidden inside of this function is the hard coded value that
//                       we want to reuse
           new Sector("Education"),
           Instant.now()
        );
    }


    @Test
    void myTestThatNeedsAnEnterpriseAccount () {
        Account enterpriseAccount = mkAcnt();
//              ▲
//              └──── And the problem repeats. Our test becomes coupled to
//                    values someone else wrote in another part of the code.
//                    What we see in the test has nothing to do with the
//                    actual test!
        // rest of test...
    }














    enum Region {AMER}
    enum Segment {ENTERPRISE}
    record AccountId(String value){}
    record Sector(String value){}
    record Account(AccountId id, Region region, USD spend, Segment segment, Sector sector, Instant updatedOn){}
}
