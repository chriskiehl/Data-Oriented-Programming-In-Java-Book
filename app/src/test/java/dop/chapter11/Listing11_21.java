package dop.chapter11;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Listing11_21 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.21
     * ───────────────────────────────────────────────────────
     * Whoops – comparison operator is in the wrong order
     * ───────────────────────────────────────────────────────
     */
    Set<Discount> applyDiscounts(Set<Account> accounts) {
        // complicated discount logic
        return accounts.stream()
        .filter((x) -> /* complicated logic here */ __)
        .filter(account -> account.spend().compareTo(threshold) >= 0)
        //                                    ▲
        //                                    └──── Few things are easier than mixing up a Comparator inside a
        //                                          filter expression
        .map(x -> /* complicated Discount computing logic here */ ___)
        .collect(toSet());

    }















    USD threshold;
    record Account(USD spend){}
    record Discount(String id){}
    boolean __ = false;
    Discount ___;
}
