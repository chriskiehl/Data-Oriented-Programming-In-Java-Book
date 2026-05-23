package dop.chapter08;

import static dop.chapter08.Listing8_23.Attribute.COUNTRY;
import static dop.chapter08.Listing8_23.Attribute.REGION;
import static dop.chapter08.Listing8_23.Attribute.SEGMENT;
import static dop.chapter08.Listing8_23.Rule.eq;
import static dop.chapter08.Listing8_23.Rule.not;

public class Listing8_23 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.23
     * ───────────────────────────────────────────────────────
     * Combinators combine with all other operations in our algebra
     * ───────────────────────────────────────────────────────
     */
    void example() {
        // Our tiny algebra is suddenly able to express complex
        // conditions tersely
        contains(COUNTRY, "US", "FR", "BE")                           
            .and(eq(SEGMENT, "Public")).or(not(eq(REGION, "LATAM")));
    }





























    







    enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL}

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
        default Rule and(Rule other) {
            return new And(this, other);
        }
        default Rule or(Rule other) {
            return new Or(this, other);
        }
    }

    static Rule contains(Attribute field, String opt1, String... rest) {
        return Rule.eq(field, opt1);
    }
}
