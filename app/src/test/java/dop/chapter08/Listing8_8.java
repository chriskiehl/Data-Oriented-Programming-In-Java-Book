package dop.chapter08;

import java.util.Optional;
import java.util.Set;

import static dop.chapter08.Listing8_8.CountryCode.AU;
import static dop.chapter08.Listing8_8.CountryCode.BE;
import static dop.chapter08.Listing8_8.CountryCode.FR;

public class Listing8_8 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.8
     * ───────────────────────────────────────────────────────
     * Writing another rule
     * ───────────────────────────────────────────────────────
     */
    /**
     * Rule #2
     * All non-reseller accounts in Belgium, Austria, and France
     * belong to SalesOrg=222
     */
    static Optional<SalesOrgId> ruleForOrg222(Account account) {
        Set<CountryCode> included = Set.of(BE, AU, FR);
        return account.region().equals(Region.EMEA)
               && included.contains(account.country())
               && account.channel().equals(SalesChannel.Reseller)
            ? Optional.of(new SalesOrgId("222"))
            : Optional.empty();
    }














    record SalesOrgId(String value){}
    record BillingCode(String value){}
    record AccountId(String value) {}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
    enum SalesChannel {Direct, Partner, Reseller /*...*/}

    record Account(
            AccountId accountId,
            Region region,
            CountryCode country,
            SalesChannel channel
    ){}
}
