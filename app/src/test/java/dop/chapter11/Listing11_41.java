package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.time.Instant.ofEpochSecond;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Stream.generate;

public class Listing11_41 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.41
     * ───────────────────────────────────────────────────────
     * using Stream.concat to combine multiple generators
     * ───────────────────────────────────────────────────────
     */
    @Test
    void combiningData() {
        Instant cutoff = ofEpochSecond(rand.nextLong(10000, 9999999));
        Stream<Account> staleAccounts = generate(() ->                    //  ┐
            mkAcnt().withUpdatedOn(cutoff.minus(1, DAYS)))                //  │
            .limit(50);                                                   //  │
                                                                          //  │◄── Generating interesting
        Stream<Account> freshAccounts = generate(() ->                    //  │    subsets of data in isolation
            mkAcnt().withUpdatedOn(cutoff.plus(1, DAYS)))                 //  │
            .limit(50);                                                   //  ┘

        List<Account> allAccounts =
            Stream.concat(staleAccounts, freshAccounts).toList();
//                 ▲
//                 └──── And then combining them into one large collection
//                       for us in the test
//          “for all accounts, it must hold that...”
    }














    Random rand = new Random();
    record Account(Instant updatedOn) {
        Account withUpdatedOn(Instant updatedOn) { return new Account(updatedOn); }
    }
    static Account mkAcnt() { return new Account(Instant.now()); }
}
