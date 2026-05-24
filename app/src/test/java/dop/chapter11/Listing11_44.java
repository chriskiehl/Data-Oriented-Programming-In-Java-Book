package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static java.util.stream.Stream.generate;

public class Listing11_44 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.44
     * ───────────────────────────────────────────────────────
     * randomly generating random amounts of random data
     * ───────────────────────────────────────────────────────
     */
    @Test
    void randomizingTheAmountOfData() {
        List<Account> accounts = generate(() -> mkAcnt())
//                ┌───────────────────┐
            .limit(rand.nextInt(0, 10))
//                └───────────────────┘
//                      ▲
//                      └──── Generates between 0 and 10 accounts
            .toList();
    }














    Random rand = new Random();
    record Account(String id){}
    Account mkAcnt() { return new Account(""); }
}
