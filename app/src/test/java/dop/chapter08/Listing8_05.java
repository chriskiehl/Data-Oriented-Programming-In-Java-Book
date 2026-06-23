package dop.chapter08;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing8_05 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.5
     * ───────────────────────────────────────────────────────
     * Capturing the properties in a test
     * ───────────────────────────────────────────────────────
     * We don't dive super deeply into in the book, but discrete
     * math is fun, so we'll do so here.
     *
     * The challenge in the chapter is to map Accounts to
     * SalesOrgIds. This is a partial, many-to-one mapping.
     * No function is allowed to map the same input to the same
     * output. So, the implication being that if they DO overlap
     * then there is an error.
     *
     * Many-to-one is easy enough to express formally. There must
     * exist no function/rule that outputs a set with more than
     * 1 item in it. 
     *
     * Eq.1
     * $$ \forall f \in F, |Domain(f)| = 1 $$
     *
     * "Different rules must not map to the same SalesOrgId" is just
     * another way of saying they should produce "pairwise disjoint sets."
     *
     * Eq.2
     * $$ \forall f,g \in F, f \neq g \implies Im(f) \cap Im(g) = \emptyset $$
     *
     * The most tricky and subtle thing to enforce is that no two rule
     * functions map the same *inputs* to *different* outputs. It's very
     * easy to accidentally honor (Eq.1) and (Eq.2) while silently mapping
     * the same account(s) to different SalesOrgIds. So, the final property
     * verifies that we don't do that.
     *
     * it's exactly the same as the above, just for inputs, rather than outputs.
     *
     * $$ \forall f,g \in F, f \neq g \implies Domain(f) \cap Domain(g) = \emptyset $$
     *
     */
    List<RuleFunction> allKnownRules = List.of(
            Chapter08::ruleForOrg111,
            Chapter08::ruleForOrg222
            // and so on...
    );

    @Test
    void rulesMustNotCollide() {
        for (RuleFunction a : allKnownRules) {
            for (RuleFunction b : allKnownRules) {
                if (a != b) {
//                 ┌────────────────────────────────┐
                    assertEquals(image(a).size(), 1);
                    assertEquals(image(b).size(), 1);   // One Rule maps to one Sales Org Id
//                 └────────────────────────────────┘

                    assertEquals(
//                        ┌───────────────────────────────┐
                            intersect(image(a), image(b)),  // No two rules have colliding outputs
                            Set.of()
//                        └───────────────────────────────┘
                    );
                    assertEquals(
//                        ┌────────────────────────────────┐
                            intersect(domain(a), domain(b)), // And no two rules assign the same
                            Set.of()                         // inputs to any Org Id
//                        └────────────────────────────────┘
                    );
                }
            }
        }
    }

























    interface RuleFunction extends Function<Account, Optional<SalesOrgId>> {
    }

    record SalesOrgId(String value) {
    }

    record AccountId(String value) {
    }

    record Account(AccountId accountId) {
    }

    static class Chapter08 {
        static Optional<SalesOrgId> ruleForOrg111(Account account) {
            return Optional.empty();
        }

        static Optional<SalesOrgId> ruleForOrg222(Account account) {
            return Optional.empty();
        }
    }
    // If you're looking down here -- IGNORE ME!
    // These are just minimal (and meaningless!) shims so the tests pass!
    static int orgId = 1;
    static SalesOrgId nextOrgId() {
        return new SalesOrgId(String.valueOf(orgId++));
    }
    static Set<SalesOrgId> image(RuleFunction f) {
        return Set.of(nextOrgId());
    }

    static Set<Account> domain(RuleFunction f) {
        return Set.of();
    }

    static <A> Set<A> intersect(Set<A> a, Set<A> b) {
        return Set.of();
    }
}
