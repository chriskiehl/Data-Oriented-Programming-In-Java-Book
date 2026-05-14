package dop.chapter08;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static dop.chapter08.Listing8_8.CountryCode.*;

@Log4j2
public class Listing8_8 {

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
     * Listing 8.8
     * ───────────────────────────────────────────────────────
     * A return to the original approach, but perhaps with an
     * overcorrection.
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
                    log.info("matched region=EMEA");   // ◄───────────────┐  These logs give a ton of operational
                    Set<CountryCode> excluded = Set.of(BE, AU, FR);//     │  monitoring, but at the expense of
                    if (!excluded.contains(account.country())) {//        │  making the code a sea of noise.
                        log.info("matched: {} not in {}", account.country(), excluded);
                        return Optional.of(new SalesOrgId("111"));
                    } else {
                        log.info("Expected country not in {}, found country={}",
                                excluded,
                                account.country());
                        return Optional.empty();
                    }
                } else {
                    log.info("expected region=EMEA, found region={}",
                            account.region());
                    return Optional.empty();
                }
            }
        }
    }
}
