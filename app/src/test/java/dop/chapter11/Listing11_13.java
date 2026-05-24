package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_13 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.13
     * ───────────────────────────────────────────────────────
     * Show the relationship between inputs and assertions
     * ───────────────────────────────────────────────────────
     */
    @Test
    void noDiscountForLargeAccounts() {
        //                  ┌───────────────────┐ Representing what we know with data.
        //                  │                   │ We expect accountB to be dropped
        //                  ▼                   ▼
        Set<Account> shouldBeDropped = Set.of(accountB);
        Set<Account> shouldBeProcessed = Set.of(accountA, accountC);
        //                   ▲                    ▲          ▲
        //                   └────────────────────┘──────────┘   And accounts A and C to be processed

        Set<Account> items = union(shouldBeDropped, shouldBeKept);
        //                     ▲
        //                     └──── Now we can see that the input is made up of good
        //                           and bad values.
        Set<Result> result = this.service.applyDiscounts(items);

        // No more magic numbers needed. The assertions are derived
        // from the input data
        assertEquals(shouldBeProcessed.size(), result.size());
        //                               ▲
        //                               └─ (Note! This is a terrible assertion that we'll fix shortly!
    }   //                                   Comparing sets by their size tells us very little about their
        //                                   content)

    static <A> Set<A> union(Set<A> left, Set<A> right) {
//                   ▲
//                   └──── (this helper is just so we can merge sets as values.
//                         Java’s default addAll acts as an identity)
        HashSet<A> copy = new HashSet<>(left);
        copy.addAll(right);
        return copy;
    }














    MyService service;
    Account accountA;
    Account accountB;
    Account accountC;
    Set<Account> shouldBeKept;
    record Account(String id){}
    record Result(String id){}
    interface MyService {
        Set<Result> applyDiscounts(Set<Account> accounts);
    }
}
