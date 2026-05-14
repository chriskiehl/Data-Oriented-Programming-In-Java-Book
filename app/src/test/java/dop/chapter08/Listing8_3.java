package dop.chapter08;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

public class Listing8_3 {

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

    // This interface is not used in the chapter, it's only meant
    // to conceptually show what a rule *means*
    interface Rule extends Function<Account, Optional<SalesOrgId>> {}

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.3
     * ───────────────────────────────────────────────────────
     * We don't dive into it in the book, but this listing is really
     * in the spirit of the prior chapter "what does it mean to
     * be correct?" question.
     *
     * Before writing any code, we imagine what the bedrock denotation
     * is -- in this case, a function from `Account -> Optional<SalesOrgId>`
     */
    @Test
    void example() {
        class __ {
            // This function is an example of what a function looks like in Java
            public static Optional<SalesOrgId> orgId1234rule(Account account) {
                return null; // not implemented in the book. Null is returned here
                             // just so the code compiles
            }
        }
    }
}
