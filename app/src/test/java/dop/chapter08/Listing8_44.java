package dop.chapter08;

public class Listing8_44 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.44
     * ───────────────────────────────────────────────────────
     * This doesn’t work
     * ───────────────────────────────────────────────────────
     */
    static <A> A get(Account account, Attr<A> attr) {
//        return switch (attr.value()) {
//            case Region -> account.region();          ┐
//            case Country -> account.country();        │
//            case Sector -> account.sector();          │◄── All of these produce type errors.
//            case Segment -> account.segment();        │
//            case Channel -> account.channel();        ┘
//        };
        return null;
    }














    enum Attribute {Region, Country, Sector, Segment, Channel}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
    enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
    enum SalesChannel {Direct, Partner, Reseller /*...*/}
    record Sector(String value) {}
    record Attr<A>(Attribute value){}
    record Account(Region region, CountryCode country, Sector sector, Segment segment, SalesChannel channel){}
}
