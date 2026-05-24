package dop.chapter11;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Listing11_9 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.9
     * ───────────────────────────────────────────────────────
     * Generating a bunch of account states
     * ───────────────────────────────────────────────────────
     */
    static List<Account> createAccounts() {
        List<Account> accounts = new ArrayList<>();
        int num = 1;
        List<String> sectors = List.of("Healthcare", "Finance", "...");  //  ┐
        for (Region region : Region.values()) {                          //  │ ◄── As we’ve done in previous chapters, we
            for (Segment segment : Segment.values()) {                   //  │     iterate through the Account’s data types
                for (String sector : sectors) {                          //  ┘     to generate a representative set of states
                    accounts.add(new Account(
                        new AccountId(format("%09d", num++)), // ◄─┐
                        region,                               //   │◄─ For types that could be anything, we
                        new USD(BigDecimal.valueOf(num)),     // ◄─┘   derive unique values from the loop counter.
                        segment,
                        new Sector(sector),
                        Instant.now() // ◄─── Or set it to some throw-away value

                    ));
                }
            }
        }
        return accounts;
    }














    enum Region {AMER, LA, EMEA /*etc...*/;}
    enum Segment {ENTERPRISE, STRATEGIC, EXISTING, /*etc...*/ }
    record AccountId(String value){}
    record Sector(String value){}
    record Account(AccountId id, Region region, USD spend, Segment segment, Sector sector, Instant updatedOn){}
}
