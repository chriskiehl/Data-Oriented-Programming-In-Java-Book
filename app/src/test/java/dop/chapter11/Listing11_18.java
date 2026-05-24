package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static dop.chapter11.Listing11_18.Region.AMER;
import static dop.chapter11.USD.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_18 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.18
     * ───────────────────────────────────────────────────────
     * Good tests give you everything you need to understand them
     * ───────────────────────────────────────────────────────
     */
    @Test
    void noDiscountForLargeAccountsInAMER() {
        USD threshold = USD.valueOf(1_500_000.00);
        //                                ▲
        //                                └──── Everything is computed from this initial value

        Account aboveLimit = mkAcnt(AMER, threshold.plus(ONE));    //  ┐
        Account atLimit = mkAcnt(AMER, threshold);                 //  │◄── The state of the accounts is
        Account belowLimit = mkAcnt(AMER, threshold.minus(ONE));   //  ┘    derived from the threshold

        Set<Account> willBeDropped = Set.of(aboveLimit);              //  ┐
        Set<Account> willBeProcessed = Set.of(atLimit, belowLimit);   //  │◄── And these sets are just
        Set<Account> items = union(willBeDropped, shouldBeProcessed); //  ┘    aggregations of the data
                                                                      //       computed above

        MyService service = new MyService(threshold);            //  ┐
        Set<Result> result = service.applyDiscounts(items);      //  │◄── Everything else just consumes
        assertEquals(result.size(), shouldBeProcessed.size());   //  ┘    the data
    }














    Set<Account> shouldBeProcessed;
    enum Region {AMER}
    record Account(String id){}
    record Result(String id){}
    static Account mkAcnt(Region region, USD spend) { return new Account(""); }
    static <A> Set<A> union(Set<A> left, Set<A> right) { return left; }
    static class MyService {
        MyService(USD threshold) {}
        Set<Result> applyDiscounts(Set<Account> items) { return Set.of(); }
    }
}
