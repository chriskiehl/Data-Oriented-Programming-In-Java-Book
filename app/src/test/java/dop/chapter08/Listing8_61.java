package dop.chapter08;

public class Listing8_61 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.61
     * ───────────────────────────────────────────────────────
     * New information on the Account
     * ───────────────────────────────────────────────────────
     */
    record Account(
        AccountId accountId,
        Region region,
        CountryCode country,
        Sector sector,
        Segment segment,
        SalesChannel channel,
        USD totalSpend // ◄── New!
    ){}














    record AccountId(String value) {}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
    enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
    enum SalesChannel {Direct, Partner, Reseller /*...*/}
    record Sector(String value) {}
    record USD(java.math.BigDecimal value) {}
}
