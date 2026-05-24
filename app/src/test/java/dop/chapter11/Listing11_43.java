package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static dop.chapter11.Listing11_43.Region.EMEA;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Stream.generate;

public class Listing11_43 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.43
     * ───────────────────────────────────────────────────────
     * Querying interesting subsets of data from a larger collection
     * ───────────────────────────────────────────────────────
     */

    @Test
    void selectingSubsetsOfInterest() {
        // We start with a big collection of data
        List<Account> accounts = generate(this::mkAcnt).limit(100).toList();
        // And then query out subsets we need for the test
        List<Account> emeaAccounts = accounts.stream()
            .filter(x -> x.region().equals(EMEA)).toList();

        Map<Sector, List<Account>> bySector = emeaAccounts  //
            .stream()                                       // It’s all just data. We can
            .collect(groupingBy(Account::sector));          // break it down however we like
    }














    enum Region {AMER, LA, EMEA}
    record Sector(String value){}
    record Account(Region region, Sector sector){}
    Account mkAcnt() { return new Account(EMEA, new Sector("")); }
}
