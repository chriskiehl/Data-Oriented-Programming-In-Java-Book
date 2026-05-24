package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static dop.chapter11.Listing11_46.Region.AMER;
import static dop.chapter11.USD.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_46 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.46
     * ───────────────────────────────────────────────────────
     * it takes a lot of work to exercise multiple states by hand
     * ───────────────────────────────────────────────────────
     */
    @Test
    void noDiscountForLargeAccountsInAMER () {
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

            Set<Account> accountStates = Set.of(aboveLimit, atLimit, belowLimit);
            for (Set<Account> accounts : powerSet(accountStates)) {
                Set<Account> shouldBeProcessed = without(accounts, aboveLimit);
                MyService service = new MyService(threshold);
                Set<Discount> result = service.applyDiscounts(accounts);
                assertEquals(result.size(), shouldBeProcessed.size());
            }
        });
    }














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
