package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.List;

import static dop.chapter11.Listing11_40.Region.EMEA;
import static java.util.stream.Stream.generate;

public class Listing11_40 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.40
     * ───────────────────────────────────────────────────────
     * Generating collections of specialized randomized data
     * ───────────────────────────────────────────────────────
     */
    @Test
    void specializingTheGeneratedData() {
        List<Account> emeaAccounts =                      //  ┐
            generate(() -> mkAcnt().withRegion(EMEA))     //  │
            .limit(100)                                   //  │◄── 100 random accounts all in
            .toList();                                    //  ┘    the EMEA region
        List<Account> accountStates =                                     //  ┐
            generate(() -> mkAcnt().withId(new AccountId("000000001")))   //  │
            .limit(1000)                                                  //  │◄── Generating large collections
            .toList();                                                    //  ┘    lets us approximate “for all”
        // and then feed them into your test
    }














    enum Region {AMER, LA, EMEA}
    record AccountId(String value){}
    record Account(AccountId id, Region region) {
        Account withRegion(Region region) { return new Account(id, region); }
        Account withId(AccountId id) { return new Account(id, region); }
    }
    static Account mkAcnt() {
        return new Account(new AccountId(""), EMEA);
    }
}
