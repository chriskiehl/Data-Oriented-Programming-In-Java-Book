package dop.chapter08;

import static java.lang.String.format;

public class Listing8_58 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.58
     * ───────────────────────────────────────────────────────
     * A few helper functions to transform Rules into GraphViz lingo
     * ───────────────────────────────────────────────────────
     */
    static String id(Rule rule) {
        return "node_" + Integer.toHexString(rule.toString().hashCode());
    }
                                                            //  ▲
                                                            //  └──── The generates a unique(ish) ID. A proper SHA would avoid collisions,
                                                            //        but this is good enough for our purposes.

    static String edge(Rule from, Rule to) {
        return format("%s -> %s", id(from), id(to));
    }
                                                            //  ▲
                                                            //  └──── Edges are simple. An ascii arrow (->) between the two ids

    static String node(Rule rule) {
        return switch (rule) {
            // To make the visualization pretty,
            // we give the nodes different styling rules depending
            // on if they’re a leaf node or a branch.
            case Rule.Equals(var field, var value)
                -> format("%s [label=\"%s=%s\"]", id(rule), field, value);
            default
                -> format("%s [label=\"%s\"]",
                    id(rule),
                    rule.getClass().getSimpleName());
        };
    }














    enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL}

    sealed interface Rule {
        record Equals(Attribute field, String value) implements Rule {}
        record And(Rule a, Rule b) implements Rule {}
        record Or(Rule a, Rule b) implements Rule {}
        record Not(Rule rule) implements Rule {}
    }
}
