package dop.chapter08;

import static dop.chapter08.Listing8_31.Attribute.Country;
import static dop.chapter08.Listing8_31.Attribute.Region;
import static dop.chapter08.Listing8_31.Rule.Not;
import static dop.chapter08.Listing8_31.Rule.contains;
import static dop.chapter08.Listing8_31.Rule.eq;

public class Listing8_31 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.31
     * ───────────────────────────────────────────────────────
     * Serializing to something like JSON is trivial because it’s just data!
     * ───────────────────────────────────────────────────────
     */
    void example() {
        contains(Country, US, CA).and(Not(eq(Region, EMEA)));
    }
    // out:
    /*
    {
      "type": "AND",
      "a": {
        "type": "OR",
        "a": {
          "type": "EQ",
          "field": "country",
          "value": "US"
        },
        "b": {
          "type": "EQ",
          "field": "country",
          "value": "CA"
        }
      },
      "b": {
        "type": "NOT",
        "expr": {
          "type": "EQ",
          "field": "Region",
          "value": "EMEA"
        }
      }
    }
    */














    static String US = "US";
    static String CA = "CA";
    static String EMEA = "EMEA";
    enum Attribute {Country, Region}

    sealed interface Rule {
        record Equals(Attribute field, String value) implements Rule {}
        record And(Rule a, Rule b) implements Rule {}
        record Or(Rule a, Rule b) implements Rule {}
        record Not(Rule rule) implements Rule {}

        static Equals eq(Attribute field, String value) {
            return new Equals(field, value);
        }
        static Not Not(Rule rule) {
            return new Not(rule);
        }
        static Rule contains(Attribute field, String opt1, String... rest) {
            return eq(field, opt1);
        }
        default Rule and(Rule other) {
            return new And(this, other);
        }
    }
}
