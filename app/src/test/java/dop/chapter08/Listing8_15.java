package dop.chapter08;

import static dop.chapter08.Listing8_15.Attribute.COUNTRY;
import static dop.chapter08.Listing8_15.Attribute.REGION;
import static dop.chapter08.Listing8_15.Attribute.SEGMENT;
import static dop.chapter08.Listing8_15.Rule.And;
import static dop.chapter08.Listing8_15.Rule.Equals;
import static dop.chapter08.Listing8_15.Rule.Not;
import static dop.chapter08.Listing8_15.Rule.Or;

public class Listing8_15 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.15
     * ───────────────────────────────────────────────────────
     * It’s super verbose right now, but we’ll make it better
     * ───────────────────────────────────────────────────────
     */
    void example() {
        // Creating rules currently involves a lot of boilerplate
        new Or(
            new And(new Equals(COUNTRY, "US"), new Equals(SEGMENT, "Public")),
            new Not(new Equals(REGION, "AMER")));
    }
























    







    enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL}

    sealed interface Rule {
        record Equals(Attribute field, String value) implements Rule {}
        record And(Rule a, Rule b) implements Rule {}
        record Or(Rule a, Rule b) implements Rule {}
        record Not(Rule rule) implements Rule {}
    }
}
