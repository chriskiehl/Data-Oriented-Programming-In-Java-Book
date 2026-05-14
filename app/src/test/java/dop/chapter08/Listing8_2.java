package dop.chapter08;

import org.junit.jupiter.api.Test;

public class Listing8_2 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.2
     * ───────────────────────────────────────────────────────
     * This listing just shows the main data types in play.
     * (Repeated from above)
     */
    @Test
    void example() {
        record SalesOrgId(String value){}
        record AccountId(String value) {}
        enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
        enum SalesChannel {Direct, Partner, Reseller /*...*/}
        enum Region { LATAM, NA, EMEA /*...*/}
        enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
        record Sector(String value) {}
    }
}
