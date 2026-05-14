package dop.chapter08;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static dop.chapter08.Listing8_6_to_8_7.CountryCode.*;

public class Listing8_6_to_8_7 {

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
     * Listing 8.6 through 8.7
     * ───────────────────────────────────────────────────────
     * Ah... All better?
     * We did the "pro" developer thing of reducing the individual
     * checks down to one clean boolean expression, but... we
     * lost something along the way.
     */
    @Test
    void example() {
        class __ {
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
            // this feels like an OK pattern. Let's write another.
            //
            // Here's our second rule:
            /**
             * Rule #2
             * All non-reseller accounts in Belgium, Austria, and France
             * belong to SalesOrg=222
             */
            static Optional<SalesOrgId> ruleForOrg222(Account account) {
                // This code is "elegant" to look at, but it's an operational
                // and maintenance pain. Collapsing everything down to a single
                // boolean destroys all the useful information we learn.
                // With this setup, simple questions like "Why did(n't) you
                // send Account X to SalesOrg Y?" will leave us stumped.
                Set<CountryCode> included = Set.of(BE, AU, FR);
                return account.region().equals(Region.EMEA)
                        && included.contains(account.country())
                        && account.channel().equals(SalesChannel.Reseller)
                        ? Optional.of(new SalesOrgId("222"))
                        : Optional.empty();
            }
        }
    }
}
