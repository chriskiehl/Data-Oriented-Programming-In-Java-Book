package dop.chapter08;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Listing8_4 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.4
     * ───────────────────────────────────────────────────────
     * Helpers that extract the “Image” and ”Domain” of a Rule Function
     * ───────────────────────────────────────────────────────
     */
    static Set<Account> allPossibleAccounts() {
        Set<Account> accounts = new HashSet<>();
        List<Sector> sectors = List.of(new Sector("Finance"), new Sector("..."));   //  ┐
        for (Segment segment : Segment.values()) {                                  //  │
            for (SalesChannel channel : SalesChannel.values()) {                    //  │◄── Generates all possible states an
                for (Region region : Region.values()) {                             //  │    individual account could be in
                    for (Sector sector : sectors) {                                 //  │    (checkout chapter 7 for a refresher
                        for (CountryCode country : CountryCode.values()) {          //  │    on this idea)
                            accounts.add(new Account(                               //  │
                                    new AccountId("000000001"),                     //  │     
                                    region,                                         //  │
                                    country,                                        //  │
                                    sector,                                         //  │
                                    segment,                                        //  │
                                    channel                                         //  │
                            ));                                                     //  ┘
                        }
                    }
                }
            }
        }
        return accounts;
    }

//                           ┌──── The "image" of a function is the set of everything
//                           ▼     it outputs
    static Set<SalesOrgId> image(RuleFunction f) {
        return allPossibleAccounts()
            .stream()
            .map(f)
            .filter(Optional::isPresent)      // ◄── for our purposes, anything that's Empty
            .map(Optional::get)               //     gets dropped. We only want to know what SalesOrgIds
            .collect(Collectors.toSet());     //     a function maps inputs to 
    }
//
//                         ┌── This is the inverse of Image. It records what inputs
//                         ▼   a rule knows how to map to an output.
    static Set<Account> domain(RuleFunction f) {
        return allPossibleAccounts()
            .stream()
            // as with Image, we’re only interested in what is defined
            .filter(acount -> f.apply(acount).isPresent())
            .collect(Collectors.toSet());
    }











    interface RuleFunction extends Function<Account, Optional<SalesOrgId>> {}

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
}
