package dop.chapter08;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static dop.chapter08.Listing8_5.CountryCode.*;

public class Listing8_5 {

    record SalesOrgId(String value){}
    record AccountId(String value) {}
    enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
    enum SalesChannel {Direct, Partner, Reseller /*...*/}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
    record Sector(String value) {}

    record Account(
            AccountId accountId,
            Region region,
            CountryCode country,
            Sector sector,
            Segment segment,
            SalesChannel channel
    ){}

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.5
     * ───────────────────────────────────────────────────────
     * Our first rule!
     * We begin by writing some code.
     */
    @Test
    void example() {
        class __ {
            /**
             * Rule #1
             * All accounts in EMEA excluding those in Belgium, Austria,
             * and France belong to SalesOrg=111
             */
            static Optional<SalesOrgId> ruleOrg1234(Account account) {
                if (account.region().equals(Region.EMEA)) {
                    Set<CountryCode> excluded = Set.of(BE, AU, FR);
                    if (!excluded.contains(account.country())) {
                        return Optional.of(new SalesOrgId("111"));
                    } else {//                     ▲
                        //                         └───────┐
                        return Optional.empty();  //       │  The code is OK, but it has lots of
                    }//                   ▲                │  branching. Overall it's just kind of
                } else {//                └─────────────── │  ugly.
                    return Optional.empty();//             │
                }//                   ▲                    │  Maybe we could "fix" it?
            }//                       └────────────────────┘
        }
    }
}
