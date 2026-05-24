package dop.chapter11;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static dop.chapter11.Listing11_14.Region.US;
import static dop.chapter11.Listing11_14.Segment.ENTERPRISE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_14 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.14
     * ───────────────────────────────────────────────────────
     * The relevant state is fragmented across the test suite
     * ───────────────────────────────────────────────────────
     */
    class MyTestClass {
        private MyService service;

        @BeforeEach
        void setup() {
//                                      ┌──────────────────────────┐
            this.service = new MyService(USD.valueOf(1_500_000.00));
//                                      └──────────────────────────┘
        }                                        //  ▲
                                                 //  │
                                                 //  │
        static Account accountA /*...*/;         //  │
        static Account accountC /*...*/;         //  │ These values are the most important, and yet
        static Account accountB = new Account(   //  │ they're hidden away in setup methods and hard
             new AccountId("000000001"),         //  │ coded data that lives *outside* of our test.
             US,                                 //  │
             //        ┌────────────┐            //  │
             USD.valueOf(1_600_00.00), // ◄──────────┘
             //        └────────────┘
             ENTERPRISE,
             new Sector("Finance")
             //...
          );

        @Test
        void noDiscountForLargeAccounts() {
//         ┌───────────────────────────────────────────────────────────┐
            Set<Account> willBeDropped = Set.of(accountB);             // The only way to understand *why* we expect
            Set<Account> willBeProcessed = Set.of(accountA, accountC); // these to be dropped versus processed is to
//         └───────────────────────────────────────────────────────────┘  look outside the current test function.
            Set<Account> items = union(willBeDropped, willBeProcessed);
            Set<Result> result = this.service.applyDiscounts(items);
            assertEquals(willBeProcessed.size(), result.size());
        }
    }














    enum Region {US}
    enum Segment {ENTERPRISE}
    record Sector(String value){}
    record AccountId(String value){}
    record Account(AccountId id, Region region, USD spend, Segment segment, Sector sector){}
    record Result(String id){}
    static class MyService {
        MyService(USD threshold) {}
        Set<Result> applyDiscounts(Set<Account> items) { return Set.of(); }
    }
    static <A> Set<A> union(Set<A> left, Set<A> right) {
        HashSet<A> copy = new HashSet<>(left);
        copy.addAll(right);
        return copy;
    }
}
