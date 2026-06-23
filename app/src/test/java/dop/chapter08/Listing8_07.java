package dop.chapter08;

import java.util.Optional;
import java.util.Set;

import static dop.chapter08.Listing8_07.CountryCode.AU;
import static dop.chapter08.Listing8_07.CountryCode.BE;
import static dop.chapter08.Listing8_07.CountryCode.FR;

public class Listing8_07 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.7
     * ───────────────────────────────────────────────────────
     * Ah… much better?
     * ───────────────────────────────────────────────────────
     */
    /**
     * Rule #1
     * All accounts in EMEA excluding those in Belgium, Austria,
     * and France belong to SalesOrg=111
     */
    static Optional<SalesOrgId> ruleForOrg111(Account account) {
        Set<CountryCode> excluded = Set.of(BE, AU, FR);
        return account.region().equals(Region.EMEA)
                && !excluded.contains(account.country())
            ? Optional.of(new SalesOrgId("111"))
            : Optional.empty();
    }














    record SalesOrgId(String value){}
    record AccountId(String value) {}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}

    record Account(
            AccountId accountId,
            Region region,
            CountryCode country
    ){}
}
