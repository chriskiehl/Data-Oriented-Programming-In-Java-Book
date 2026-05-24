package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static dop.chapter11.Listing11_15.Region.AMER;
import static dop.chapter11.Listing11_15.Segment.ENTERPRISE;

public class Listing11_15 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.15
     * ───────────────────────────────────────────────────────
     * What’s important here?
     * ───────────────────────────────────────────────────────
     */
    @Test
    void wayTooManyDetails() {
        Account accountA = new Account(
            new AccountId("111111111"),  //  ┐
            Region.AMER,                 //  │
            USD.valueOf(1_000_00.00),    //  │
            Segment.ENTERPRISE,          //  │◄── Which of these attributes are
            new Sector("Healthcare"),    //  │    relevant to the test?
            Instant.now()                //  ┘
        );
        Account accountB = new Account(
            new AccountId("222222222"),  // This noise is usually what motivates us moving
            Region.AMER,                 // state into shared methods in the first place.
            USD.valueOf(1_600_00.00),
            Segment.ENTERPRISE,
            new Sector("Healthcare"),
            Instant.now()
        );
        // Account accountC = ...
        MyService service = new MyService(USD.valueOf(1_500_000.00));
        Set<Account> shouldBeDropped = Set.of(accountB);             //
        Set<Account> shouldBeProcessed = Set.of(accountA, accountC); //   ◄── Despite all the important data
                                                                     //       defined "in the test", it’s still
                                                                     //       no clearer why we expect some accounts
                                                                     //       to be processed versus dropped. All
                                                                     //       we’ve done is show that data is
                                                                     //       involved, not show what data is
                                                                     //       important.
        Set<Account> items = union(shouldBeDropped, shouldBeProcessed);
        // ...
    }














    Account accountC;
    enum Region {AMER}
    enum Segment {ENTERPRISE}
    record AccountId(String value){}
    record Sector(String value){}
    record Account(AccountId id, Region region, USD spend, Segment segment, Sector sector, Instant updatedOn){}
    static class MyService {
        MyService(USD threshold) {}
    }
    static <A> Set<A> union(Set<A> left, Set<A> right) {
        return left;
    }
}
