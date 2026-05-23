package dop.chapter08;

import java.util.Optional;

import static dop.chapter08.Listing8_30.Attribute.COUNTRY;
import static dop.chapter08.Listing8_30.Attribute.REGION;
import static dop.chapter08.Listing8_30.Rule.contains;
import static dop.chapter08.Listing8_30.Rule.eq;
import static dop.chapter08.Listing8_30.Rule.not;

public class Listing8_30 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.30
     * ───────────────────────────────────────────────────────
     * Refactoring to use our new tools
     * ───────────────────────────────────────────────────────
     */
    /**
     * Rule #1
     * All accounts in EMEA excluding those in Belgium, Austria,
     * and France belong to SalesOrg=111
     */
    static Optional<SalesOrgId> ruleForOrg111(Account account) {
        Rule rule = eq(REGION, "EMEA")                      // ┐
            .and(not(contains(COUNTRY, "US", "BE", "FR"))); // ┘◄── Code that reads exactly
                                                            //      like its requirements
        return interpret(rule, account)
            ? Optional.of(new SalesOrgId("111"))
            : Optional.empty();
    }














    enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL}
    record SalesOrgId(String value){}
    record Account(){}

    sealed interface Rule {
        record Equals(Attribute field, String value) implements Rule {}
        record And(Rule a, Rule b) implements Rule {}
        record Or(Rule a, Rule b) implements Rule {}
        record Not(Rule rule) implements Rule {}

        static Equals eq(Attribute field, String value) {
            return new Equals(field, value);
        }
        static Not not(Rule rule) {
            return new Not(rule);
        }
        static Rule contains(Attribute field, String opt1, String... rest) {
            return eq(field, opt1);
        }
        default Rule and(Rule other) {
            return new And(this, other);
        }
    }

    static boolean interpret(Rule rule, Account account) {
        return false;
    }
}
