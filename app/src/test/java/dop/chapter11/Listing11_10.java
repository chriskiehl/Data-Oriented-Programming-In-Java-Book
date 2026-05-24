package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.format;

public class Listing11_10 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.10
     * ───────────────────────────────────────────────────────
     * Generating CSV data from Accounts
     * ───────────────────────────────────────────────────────
     */
    String toCSV(int rowNumber, Account account) {
//         ▲
//         └──── These helpers turn Accounts into CSV data (using a library
//               would be a better option here, but this works in a pinch)
        return format(
            "%s,%s,%s,%s,%s,%s,%s,...",
            rowNumber,
            account.id().value(),
            account.region(),
            account.spend().value(),
            account.segment(),
            account.sector().value(),
            account.updatedOn()
        );
    }
    String toCSV(List<Account> accounts) {
//         ▲
//         └──── These helpers turn Accounts into CSV data (using a library
//               would be a better option here, but this works in a pinch)
        return String.join("\n", IntStream.range(0, accounts.size())
            .boxed().map(i -> this.toCSV(i+1, accounts.get(i)))
            .toList());
    }

    @Test
    void demo() {
        // Now rather than us coming up with examples, we have the ability
        // to generate arbitrary CSV files.
        toCSV(createAccounts().subList(0, 5));
    }
    // [out]
    // 1,149404365,LA,ENTERPRISE,1990-01-29T10:27:12Z,...
    // 2,360670251,LA,Strategic,2046-10-11T03:22:09Z,...
    // 3,499772083,AMER,Strategic,1976-05-08T17:13:10Z,...
    // 4,153989572,EMEA,Existing,2005-12-30T23:45:31Z,...
    // 5,325253899,LA,Existing,2069-04-13T08:01:34Z,...














    enum Region {AMER, LA, EMEA /*etc...*/;}
    enum Segment {ENTERPRISE, STRATEGIC, EXISTING, /*etc...*/ }
    record AccountId(String value){}
    record Sector(String value){}
    record Account(AccountId id, Region region, USD spend, Segment segment, Sector sector, Instant updatedOn){}

    static List<Account> createAccounts() {
        return List.of();
    }
}
