package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static java.util.stream.Stream.generate;

public class Listing11_45 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.45
     * ───────────────────────────────────────────────────────
     * randomly generating random amounts of random amounts of data
     * ───────────────────────────────────────────────────────
     */
    @Test
    void randomizingTheAmountOfRandomizedData() {
        List<List<Account>> accounts =
            generate(() -> generate(this::mkAcnt)    //  ┐
                .limit(rand.nextInt(10))             //  │◄── We’ve embedded one generator
                .toList())                           //  ┘    in another!
            .limit(rand.nextInt(10))
            .toList();
    }














    Random rand = new Random();
    record Account(String id){}
    Account mkAcnt() { return new Account(""); }
}
