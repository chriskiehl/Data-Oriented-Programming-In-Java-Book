package dop.chapter08;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing8_4 {

    record SalesOrgId(String value){}
    record AccountId(String value) {}
    enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
    enum SalesChannel {Direct, Partner, Reseller /*...*/}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
    record Sector(String value) {}

    record Account(
            AccountId accountId,
            Region region,
            CountryCode country,
            Sector sector,
            Segment segment,
            SalesChannel channel
    ){}

    interface Rule extends Function<Account, Optional<SalesOrgId>> {}

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.4
     * ───────────────────────────────────────────────────────
     * What does it mean to be correct?
     *
     * We skip the math in the book, but dive into it here for fun.
     *
     * [==========     SKIP IF NOT INTERESTED   ================]
     *
     * The challenge in the chapter is to map Accounts to
     * SalesOrgIds. This is a partial, many-to-one mapping.
     * No function is allowed to map the same input to the same
     * output. So, the implication being that if they DO overlap
     * then there is an error.
     *
     * Loosely:
     *
     * $$
     * \forall f_1, f_2 \in F, \forall a \in A, f_1(a) = f_2(a) \implies f_1 = f_2
     * $$
     *
     * This is the property we roll with (informally) in the book.
     *
     * An early reviewer caught that I incorrectly called this injectivity
     * (Whoops!). It's trying to assert a similar property, but about a set
     * of functions, rather than a set of inputs to a single function.
     *
     * A better way of thinking about it is that we're doing set partitioning
     * on B. In this view, the correctness is defined by two properties:
     *
     * Every function must produce a non-overlapping output
     *
     * $$ \bigcap_{f \in F} Image(f) = \emptyset $$
     *
     * and the disjoint union of all outputs should be equal to B
     *
     * $$ B = \bigsqcup_{f \in F} Image(f) $$
     *
     * [========== DISCRETE MATH SECTION DONE ================]
     *
     * Back to the code!
     */
    @Test
    void example() {
        class __ {
            // Left as an exercise to the reader: defining the set of
            // all rules in the system (see chapter 7!)
            static List<Rule> allKnownRules = List.of();

            static List<Account> allPossibleAccounts() {
                return List.of(); // left as an exercise to the reader
            }

            void assertNoRulesOverlap() {
                for (Account account: allPossibleAccounts()) {   // for all accounts
                    for (Rule a : allKnownRules) {               // for all a, b in Rules
                        for (Rule b : allKnownRules) {           //
                            if (a.apply(account).equals(b.apply(account))) {  // if their output is the same
                                assertEquals(a, b);                           // then they MUST be the same rule
                            }  //            ▲
                               //            └── At this point in the chapter, these technically
                        }      //                aren't comparable, but the code is meant to be
                               //                conceptual.
                    }
                }
            }
        }
    }
}
