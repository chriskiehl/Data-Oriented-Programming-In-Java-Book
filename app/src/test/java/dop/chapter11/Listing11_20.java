package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static dop.chapter11.Listing11_20.Region.AMER;
import static dop.chapter11.USD.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_20 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.20
     * ───────────────────────────────────────────────────────
     * Computing data from data
     * ───────────────────────────────────────────────────────
     */
    @Test
    void noDiscountForLargeAccountsInAMER () {
        List<USD> thresholds = List.of(  //  ┐
            USD.valueOf(0.0),            //  │
            USD.valueOf(0.1),            //  │◄── We don’t have to pick one threshold;
            USD.valueOf(1_500.0),        //  │    we can exercise as many as we want!
            USD.valueOf(1_500_000.0)     //  ┘
        );

        thresholds.forEach(threshold -> {
            Account aboveLimit = mkAcnt(AMER, threshold.plus(ONE));     //  ┐
            Account atLimit = mkAcnt(AMER, threshold);                  //  │
            Account belowLimit = mkAcnt(AMER, threshold.minus(ONE));    //  │ And the rest of the test automatically
                                                                        //  │ adjusts to the new thresholds. It’s just
            Set<Account> willBeDropped = Set.of(aboveLimit);            //  │ data being computed from other data.
            Set<Account> willBeProcessed = Set.of(atLimit, belowLimit); //  │
            Set<Account> items = union(willBeDropped, willBeProcessed); //  ┘
//
//
//

            MyService service = new MyService(threshold);
            Set<Discount> result = service.applyDiscounts(items);

            assertEquals(result.size(), willBeProcessed.size());
        });
    }














    enum Region {AMER}
    record Account(String id){}
    record Discount(String id){}
    static Account mkAcnt(Region region, USD spend) { return new Account(""); }
    static <A> Set<A> union(Set<A> left, Set<A> right) { return left; }
    static class MyService {
        MyService(USD threshold) {}
        Set<Discount> applyDiscounts(Set<Account> items) { return Set.of(); }
    }
}
