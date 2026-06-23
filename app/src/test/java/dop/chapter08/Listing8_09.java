package dop.chapter08;

import java.util.Optional;
import java.util.Set;

import static dop.chapter08.Listing8_09.CountryCode.AU;
import static dop.chapter08.Listing8_09.CountryCode.BE;
import static dop.chapter08.Listing8_09.CountryCode.FR;

public class Listing8_09 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.9
     * ───────────────────────────────────────────────────────
     * Operational insight, but terrible code
     * ───────────────────────────────────────────────────────
     */
    /**
     * Rule #1
     * All accounts in EMEA excluding those in Belgium, Austria,
     * and France belong to SalesOrg=111
     */
    static Optional<SalesOrgId> ruleOrg1234(Account account) {
        if (account.region().equals(Region.EMEA)) {
            log.debug("matched region=EMEA");
            Set<CountryCode> excluded = Set.of(BE, AU, FR);
            if (!excluded.contains(account.country())) {
                log.debug("matched: {} not in {}", account.country(), excluded);
                return Optional.of(new SalesOrgId("111"));
            } else {
                log.debug("Expected country not in {}, found country={}",
                    excluded,
                    account.country());
                return Optional.empty();
            }
        } else {
            log.debug("expected region=EMEA, found region={}",
                account.region());
            return Optional.empty();
        }
    }














    static Logger log = new Logger();

    record SalesOrgId(String value){}
    record AccountId(String value) {}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}

    record Account(
            AccountId accountId,
            Region region,
            CountryCode country
    ){}

    static class Logger {
        void debug(String message, Object... args) {}
    }
}
