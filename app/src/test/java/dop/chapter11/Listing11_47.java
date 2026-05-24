package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static dop.chapter11.Listing11_47.Region.AMER;
import static dop.chapter11.USD.ONE;

public class Listing11_47 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.47
     * ───────────────────────────────────────────────────────
     * The now claims that the code under test will work with any threshold
     * ───────────────────────────────────────────────────────
     */
    Random rand = new Random();

    @Test
    void noDiscountForLargeAccountsInAMER () {
        USD threshold = USD.valueOf(                //  ┐
            rand.nextDouble(USD_MIN, USD_MAX)       //  │◄── “I’m so confident that this code works
        );                                          //  ┘    with any value that I’m not even going to
                                                    //       bother picking a specific one”

        Account aboveLimit = mkAcnt(AMER, threshold.plus(ONE));    //  ┐
        Account atLimit = mkAcnt(AMER, threshold);                 //  │◄── While the threshold is randomized,
        Account belowLimit = mkAcnt(AMER, threshold.minus(ONE));   //  ┘    the data we derive from it
                                                                   //       is deterministic.
        // (rest of test)
    }














    static double USD_MIN = 0.0;
    static double USD_MAX = 1_000_000.0;
    enum Region {AMER}
    record Account(String id){}
    static Account mkAcnt(Region region, USD spend) { return new Account(""); }
}
