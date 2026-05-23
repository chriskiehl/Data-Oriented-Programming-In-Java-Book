package dop.chapter08;

import java.util.function.Function;

public class Listing8_45 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.45
     * ───────────────────────────────────────────────────────
     * Adding evidence that we know how to access our data
     * ───────────────────────────────────────────────────────
     */
//                                     ┌───────────────────────────┐
    record Attr<A>(Attribute attribute, Function<Account, A> getter){}
//                                     └───────────────────────────┘
//                                                 ▲
//                                                 └──── The only way to create an attribute now is to supply evidence
//                                                       that we know how to get from an Account to the type we’re defining


//                                                           ┌───────────────────┐
    Attr<SalesChannel> channel = new Attr<>(Attribute.Channel, Account::channel);
    Attr<Region> region = new Attr<>(Attribute.Region,         Account::region);
//                                                           └───────────────────┘
//                                                                   ▲
//                                                                   └ Method reference makes it easy to
//                                                                     supply evidence functions








    enum Attribute {Region, Country, Sector, Segment, Channel}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
    enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
    enum SalesChannel {Direct, Partner, Reseller /*...*/}
    record Sector(String value) {}
    record Account(Region region, CountryCode country, Sector sector, Segment segment, SalesChannel channel){}
}
