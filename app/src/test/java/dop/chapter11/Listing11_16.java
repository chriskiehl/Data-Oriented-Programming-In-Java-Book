package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static dop.chapter11.Listing11_16.Region.AMER;
import static dop.chapter11.Listing11_16.Segment.ENTERPRISE;
import static dop.chapter11.USD.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_16 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.16
     * ───────────────────────────────────────────────────────
     * Use factory functions to highlight what’s important when creating data
     * ───────────────────────────────────────────────────────
     */
    Account mkAcnt(Region region, USD spend) {
//             ▲
//             └──── (The aggressively shortened “mkAcnt” is to help examples
//                   fit on a page)
        return new Account(
           new AccountId("000000001"),
//        ┌───────┐
           region,
           spend,
//        └───────┘
//            ▲
//            └──── The function is designed so that you *must* provide these
//                  values in order to construct anything
           ENTERPRISE,
           new Sector("Education"),
           Instant.now()
        );
    }

    @Test
    void noDiscountForLargeAccountsInAMER() {
        USD threshold = USD.valueOf(1_500_000.00);     //  ┐
        MyService service = new MyService(threshold);  //  ┘◄── Now we can show exactly which
                                                       //       attributes are relevant to the test

//                                                  ┌────┐ ┌─────────────────────┐
        Set<Account> shouldBeDropped = Set.of(mkAcnt(AMER,  threshold.plus(ONE)));
//                                                  └────┘ └─────────────────────┘
//                                                     ▲              ▲
//           You don’t have to know anything about the domain. ───────┘
//           The test teaches you its rules. Accounts in AMER
//           that are over the threshold will be dropped.
        Set<Account> shouldBeProcessed = Set.of(accountA, accountC);
        Set<Account> items = union(shouldBeDropped, shouldBeProcessed);

        Set<Result> result = service.applyDiscounts(items);
        assertEquals(shouldBeProcessed.size(), result.size());
    }














    Account accountA;
    Account accountC;
    enum Region {AMER}
    enum Segment {ENTERPRISE}
    record AccountId(String value){}
    record Sector(String value){}
    record Account(AccountId id, Region region, USD spend, Segment segment, Sector sector, Instant updatedOn){}
    record Result(String id){}
    static class MyService {
        MyService(USD threshold) {}
        Set<Result> applyDiscounts(Set<Account> items) { return Set.of(); }
    }
    static <A> Set<A> union(Set<A> left, Set<A> right) {
        return left;
    }
}
