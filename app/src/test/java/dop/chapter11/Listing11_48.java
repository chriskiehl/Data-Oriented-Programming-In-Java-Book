package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static dop.chapter11.Listing11_48.Region.AMER;
import static dop.chapter11.USD.ONE;

public class Listing11_48 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.48
     * ───────────────────────────────────────────────────────
     * We’re checking a narrow set of states out of all possible states
     * ───────────────────────────────────────────────────────
     */
    Random rand = new Random();

    @Test
    void noDiscountForLargeAccountsInAMER () {
        USD threshold = USD.valueOf(rand.nextDouble(0.0, Math.pow(10, 10)));
//                                 ┌────┐
        Account aboveLimit = mkAcnt(AMER, threshold.plus(ONE));
        Account atLimit    = mkAcnt(AMER, threshold);
        Account belowLimit = mkAcnt(AMER, threshold.minus(ONE));
//                                 └────┘
//                                   What if it’s not AMER?

        // (rest of test)
    }














    enum Region {AMER}
    record Account(String id){}
    static Account mkAcnt(Region region, USD spend) { return new Account(""); }
}
