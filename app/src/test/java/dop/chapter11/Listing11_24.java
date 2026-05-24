package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class Listing11_24 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.24
     * ───────────────────────────────────────────────────────
     * Power Set: the set of all possible subsets
     * ───────────────────────────────────────────────────────
     */
    static <A> Set<Set<A>> powerSet(Set<A> items) {
        //                    ▲
        //                    └──── This computes the set of all possible subsets of the input
        Set<Set<A>> allSubsets = new HashSet<>();
        allSubsets.add(new HashSet<>());

        for (A item : items) {
            Set<Set<A>> newSubsets = new HashSet<>();
            for (Set<A> subset : allSubsets) {
                Set<A> newSubset = new HashSet<>(subset);
                newSubset.add(item);
                newSubsets.add(newSubset);
            }
            allSubsets.addAll(newSubsets);
        }
        return allSubsets;
    }

    // This is a little helper for removing items from a set
    // as data, rather than an identity
    static <A> Set<A> without(Set<A> left, A item) {
        HashSet<A> copy = new HashSet<>(left);
        copy.remove(item);
        return copy;
    }

    @Test
    void demo() {
        System.out.println(powerSet(Set.of("A")));              //  ┐
        System.out.println(powerSet(Set.of("A", "B")));         //  │
        System.out.println(powerSet(Set.of("A", "B", "C")));    //  │
        System.out.println(without(Set.of("A", "B"), "C"));     //  │
    }                                                           //  │◄── Here’s powerSet in action. We hand
    // [out]                                                    //  │    it a set, and it outputs all of its
    // [[], [A]]                                                //  │    subsets.
    // [[], [A], [B], [A, B]]                                   //  │
    // [[], [A], [B], [A, B], [C], [A, C], [B, C], [A, B, C]]   //  │
    // [A,B]                                                    //  ┘
}
