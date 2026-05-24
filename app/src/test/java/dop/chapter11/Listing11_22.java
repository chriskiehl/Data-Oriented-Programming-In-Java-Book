package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static dop.chapter11.Listing11_22.Region.AMER;
import static dop.chapter11.USD.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_22 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.22
     * ───────────────────────────────────────────────────────
     * Driving our test with data makes it easy to exercise different scenarios
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

            Set<Account> willBeDropped = Set.of(/*[REMOVED]*/);
            //                                        ▲
            //                                        └──── Does the bug get caught it we don’t include
            //                                              this and instead only pass in data we expect
            //                                              to be processed?

            Set<Account> willBeProcessed = Set.of(atLimit, belowLimit);  //  ┐
            Set<Account> items = union(willBeDropped, willBeProcessed);  //  │
                                                                         //  │
            MyService service = new MyService(threshold);                //  │
            Set<Discount> result = service.applyDiscounts(items);        //  │ Note that we don’t have to
                                                                         //  │ mess with the rest of the test!
                                                                         //  │ The test just consumes our data
            assertEquals(result.size(), willBeProcessed.size());         //  ┘
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
