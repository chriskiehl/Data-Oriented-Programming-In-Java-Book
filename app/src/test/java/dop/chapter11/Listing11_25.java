package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static dop.chapter11.Listing11_25.Region.AMER;
import static dop.chapter11.USD.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_25 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.25
     * ───────────────────────────────────────────────────────
     * Using a powerSet to exercise all possible Accoun[EH10.1]t combinations
     * ───────────────────────────────────────────────────────
     */
    @Test
    void noDiscountForLargeAccountsInAMER4_11_5() {
        List<USD> thresholds = List.of(
            USD.valueOf(0.0),
            USD.valueOf(0.1),
            USD.valueOf(1_500.0),
            USD.valueOf(1_500_000.0)
        );

        thresholds.forEach(threshold -> {
            Account aboveLimit = mkAcnt(AMER, threshold.plus(ONE));
            Account atLimit = mkAcnt(AMER, threshold);
            Account belowLimit = mkAcnt(AMER, threshold.minus(ONE));

            Set<Account> states = Set.of(aboveLimit, atLimit, belowLimit);
            // Now we loop over every possible subset of our accounts
            for (Set<Account> accounts : powerSet(states)) {
                Set<Account> shouldBeProcessed = without(accounts, aboveLimit);
//                           ▲
//                           └──── And we derive what shouldBeProcessed by
//                                 removing accounts above the threshold (if present)
                MyService service = new MyService(threshold);
                Set<Discount> result = service.applyDiscounts(accounts);
                assertEquals(result.size(), shouldBeProcessed.size());
                //              ▲
                //              └──── We’re still making the same squishy assertion, but now
                //                    from so many different angles that we can be confident
                //                    we’re validating something about our software
            }
        });
    }
    // [out]
    // Expected :2
    // Actual   :1
    //           ▲
    //           └──── And indeed, if we run this, we stumble on the imagined bug!














    enum Region {AMER}
    record Account(String id){}
    record Discount(String id){}
    static Account mkAcnt(Region region, USD spend) { return new Account(""); }
    static <A> Set<Set<A>> powerSet(Set<A> items) { return Set.of(items); }
    static <A> Set<A> without(Set<A> left, A item) { return left; }
    static class MyService {
        MyService(USD threshold) {}
        Set<Discount> applyDiscounts(Set<Account> items) { return Set.of(); }
    }
}
