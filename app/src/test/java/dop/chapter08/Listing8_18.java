package dop.chapter08;

import static dop.chapter08.Listing8_18.Attribute.REGION;
import static dop.chapter08.Listing8_18.Attribute.SEGMENT;
import static dop.chapter08.Listing8_18.Region.EMEA;
import static dop.chapter08.Listing8_18.Region.LATAM;
import static dop.chapter08.Listing8_18.Segment.Public;

public class Listing8_18 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.18
     * ───────────────────────────────────────────────────────
     * Check this out!
     * ───────────────────────────────────────────────────────
     */
    void example(Account account) {
        if (account.region().equals(EMEA)                    //  ┐
                && account.segment().equals(Public)) {       //  │
            /*...*/                                          //  │
        }                                                    //  │◄── Compound, multi-branch if/else
        else if (!account.region().equals(LATAM)) {          //  │    statements in code
            /*...*/                                          //  │
        } else {                                             //  │
            // no match                                      //  │
        }                                                    //  ┘

//    Can now be expressed tersely in our algebra
//                    │
//                    ▼
//    ┌──────────────────────────────────────────────────────────────┐
        Rule rule = (eq(REGION, "EMEA").and(eq(SEGMENT, "Public")))
            .or(not(eq(REGION, "LATAM")));
//    └──────────────────────────────────────────────────────────────┘
    }














    enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum Segment {Enterprise, Strategic, Existing, Public /*/*...*/ }

    sealed interface Rule {
        default Rule and(Rule other) {
            return new And(this, other);
        }
        default Rule or(Rule other) {
            return new Or(this, other);
        }
    }

    record Equals(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule rule) implements Rule {}

    record Account(Region region, Segment segment){}

    static Equals eq(Attribute attr, String val) {
        return new Equals(attr, val);
    }
    static Not not(Rule rule) {
        return new Not(rule);
    }
}
