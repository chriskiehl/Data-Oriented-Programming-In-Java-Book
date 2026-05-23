package dop.chapter08;

import static dop.chapter08.Listing8_22.Attribute.COUNTRY;
import static dop.chapter08.Listing8_22.Rule.eq;

public class Listing8_22 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.22
     * ───────────────────────────────────────────────────────
     * Increasing the clarity of our API
     * ───────────────────────────────────────────────────────
     */
    void example() {
        // [OLD BOILERPLATE VERSION]
        // eq(COUNTRY, "US").or(eq(COUNTRY, "BE")).or(eq(COUNTRY, "FR"));

        // New!
        contains(COUNTRY, "US", "BE", "FR");
//          ▲
//          └──── Sophisticated ideas without the boilerplate
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
        default Rule or(Rule other) {
            return new Or(this, other);
        }
    }

    static Rule contains(Attribute field, String opt1, String... rest) {
        return Rule.eq(field, opt1);
    }
}
