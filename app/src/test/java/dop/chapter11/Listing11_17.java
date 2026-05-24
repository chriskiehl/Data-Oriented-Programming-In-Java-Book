package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static dop.chapter11.Listing11_17.Region.AMER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_17 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.17
     * ───────────────────────────────────────────────────────
     * Good tests give you everything you need to understand them
     * ───────────────────────────────────────────────────────
     */
    @Test
    void noDiscountForLargeAccountsInAMER() {
        USD threshold = USD.valueOf(1_500_000.00);

        Account aboveLimit = mkAcnt(AMER, threshold.plus(ONE));   //  ┐
        Account atLimit = mkAcnt(AMER, threshold);            //  │◄── Now the three possible cases are
        Account belowLimit = mkAcnt(AMER, threshold.minus(ONE));   //  ┘    directly in the test, right next
                                                              //       to each other for us to study

        Set<Account> shouldBeDropped = Set.of(aboveLimit);              //  ┐
        Set<Account> shouldBeProcessed = Set.of(atLimit, belowLimit);   //  │◄── The main cases in our test
        Set<Account> items = union(shouldBeDropped, shouldBeProcessed); //  ┘    become self-describing. We
                                                                        //       expect this to be dropped
                                                                        //       because it’s above the limit.

        MyService service = new MyService(threshold);
        Set<Result> result = service.applyDiscounts(items);
        assertEquals(result.size(), shouldBeProcessed.size());
    }













    static USD ONE = new USD(BigDecimal.ONE);
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
