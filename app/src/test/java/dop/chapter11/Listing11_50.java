package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.function.Predicate;

import static dop.chapter11.Listing11_50.Region.AMER;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.generate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_50 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.50
     * ───────────────────────────────────────────────────────
     * Generating a large set of data and building our test around it
     * ───────────────────────────────────────────────────────
     */
    @Test
    void noDiscountForLargeAccountsInAMER() {
        // Rather than specifying the type of data we want, we
        // let the randomizer freely generate a large set of examples
        Set<Account> accounts = generate(this::mkAcnt)
            .limit(1000)
            .collect(toSet());

        USD threshold = accounts.iterator().next().spend();
        //                            ▲
        //                            └──── And also rather than us setting a threshold, we grab a one
        //                                  from the generated data

        Predicate<Account> aboveThresholdInAMER = (account) ->
            account.region().equals(AMER)
            && account.spend().compareTo(threshold) > 0;

        Set<Account> shouldBeDropped = accounts.stream()                  //  ┐
            .filter(aboveThresholdInAMER)                                 //  │
            .collect(toSet());                                            //  │
                                                                          //  │
        Set<Account> shouldBeProcessed = diff(accounts, shouldBeDropped); //  │◄── The rest of the
        MyService service = new MyService(threshold);                     //  │    test is business as usual
        Set<Discount> result = service.applyDiscounts(accounts);          //  │
        assertEquals(result.size(), shouldBeProcessed.size());            //  ┘
    }














    enum Region {AMER, LA, EMEA}
    record Discount(String id){}
    record Account(Region region, USD spend){}
    Account mkAcnt() { return new Account(AMER, new USD(0.0)); }
    static <A> Set<A> diff(Set<A> left, Set<A> right) { return left; }
    static class MyService {
        MyService(USD threshold) {}
        Set<Discount> applyDiscounts(Set<Account> accounts) { return Set.of(); }
    }
}
