package dop.chapter08;

public class Listing8_27 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.27
     * ───────────────────────────────────────────────────────
     * Translating attribute names back to lookups against a record
     * ───────────────────────────────────────────────────────
     */
    static String get(Account account, Attribute attr) {  // Translating from data to code is straight forward
        return switch (attr) {
//                       ▲
//                       └──── And it’s type safe! Java will tell us if we miss any cases
            case Attribute.REGION -> account.region().name();    //  ┐
            case Attribute.COUNTRY -> account.country().name();  //  │
            case Attribute.SECTOR -> account.sector().value();   //  │◄── For this first variation, we’re
            case Attribute.SEGMENT -> account.segment().name();  //  ┘    interpreting our data in the “domain”
                                                                 //       of plain strings, so enums are
                                                                 //       stringified via name()
            case Attribute.CHANNEL -> account.channel().name();
//                                                       ▲
//                                                       └──── And we reach inside SalesChannel to grab its String value
        };
    }














    enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
    enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
    enum SalesChannel {Direct, Partner, Reseller /*...*/}
    record Sector(String value) {}
    record Account(Region region, CountryCode country, Sector sector, Segment segment, SalesChannel channel){}
}
