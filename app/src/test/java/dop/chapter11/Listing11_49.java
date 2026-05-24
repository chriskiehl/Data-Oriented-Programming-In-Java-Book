package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import static dop.chapter11.Listing11_49.Region.AMER;
import static dop.chapter11.USD.ONE;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_49 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.49
     * ───────────────────────────────────────────────────────
     * Showing that our rules hold “for all” states
     * ───────────────────────────────────────────────────────
     */
    Random rand = new Random();

    @Test
    void noDiscountForLargeAccountsInAMER() {
        // We generate a random threshold and then compute what “above”
        // and “below” mean for the test
        USD threshold = USD.valueOf(rand.nextDouble(USD_ZERO, USD_MAX));
        USD above = threshold.plus(ONE);
        USD below = threshold.minus(ONE);


        Set<Account> accounts = concat(                                             //  ┐
            generate(() -> mkAcnt().withSpend(above)).limit(rand.nextInt(10)),      //  │◄── Then we use these to generate example
            generate(() -> mkAcnt().withSpend(threshold)).limit(rand.nextInt(10)),  //  │    data for each threshold case. Each
            generate(() -> mkAcnt().withSpend(below)).limit(rand.nextInt(10))       //  │    gets concatenated together to form
        ).collect(toSet());                                                         //  ┘    the full set of accounts used in the test


        // This predicate is to soul of the test. It’s what extracts out
        // what shouldn't be processed.
        Predicate<Account> aboveThresholdInAMER = (account) ->
            account.region().equals(AMER)
                && account.spend().equals(above);


        // Note that the rest of the test doesn’t know or care that we’re
        // generating inputs now. It looks exactly the same as it always has.
        // The change is that it’s now making stronger assertions because of
        // those generated inputs
//     ┌───────────────────────────────────────────────────────────────────┐

        Set<Account> shouldBeDropped = accounts.stream()
            .filter(aboveThresholdInAMER)
            .collect(toSet());

        Set<Account> shouldBeProcessed = diff(accounts, shouldBeDropped);
        MyService service = new MyService(threshold);
        Set<Discount> result = service.applyDiscounts(accounts);
        assertEquals(result.size(), shouldBeProcessed.size());
    }














    static double USD_ZERO = 0.0;
    static double USD_MAX = 1_000_000.0;
    enum Region {AMER, LA, EMEA}
    record Account(Region region, USD spend) {
        Account withSpend(USD spend) { return new Account(region, spend); }
    }
    record Discount(String id){}
    static Account mkAcnt() { return new Account(AMER, USD.valueOf(0.0)); }
    static <A> java.util.stream.Stream<A> concat(java.util.stream.Stream<A>... streams) {
        return java.util.Arrays.stream(streams).flatMap(java.util.function.Function.identity());
    }
    static java.util.stream.Stream<Account> generate(java.util.function.Supplier<Account> supplier) {
        return java.util.stream.Stream.generate(supplier);
    }
    static <A> Set<A> diff(Set<A> left, Set<A> right) {
        return left;
    }
    static class MyService {
        MyService(USD threshold) {}
        Set<Discount> applyDiscounts(Set<Account> items) { return Set.of(); }
    }
}
