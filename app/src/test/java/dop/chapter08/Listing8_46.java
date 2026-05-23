package dop.chapter08;

import java.util.function.Function;

public class Listing8_46 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.46
     * ───────────────────────────────────────────────────────
     * Refactoring to simplify how we lookup values
     * ───────────────────────────────────────────────────────
     */
    void example(Attr<Region> attr, Account account) {
        attr.getter().apply(account);
//                       ▲
//                       └──── this is now all we need to convert from data to concrete code
    }














    enum Attribute {Region, Country, Sector, Segment, Channel}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
    enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
    enum SalesChannel {Direct, Partner, Reseller /*...*/}
    record Sector(String value) {}
    record Attr<A>(Attribute attribute, Function<Account, A> getter){}
    record Account(Region region, CountryCode country, Sector sector, Segment segment, SalesChannel channel){}
}
