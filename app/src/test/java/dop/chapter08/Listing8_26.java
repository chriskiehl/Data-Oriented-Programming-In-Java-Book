package dop.chapter08;

public class Listing8_26 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.26
     * ───────────────────────────────────────────────────────
     * a Fleshing out the interpreter
     * ───────────────────────────────────────────────────────
     */
    static Boolean interpret(Rule rule, Account account) {
//            ▲
//            └──── Setting the return value to Boolean.


        return switch (rule) {
            case Rule.Equals(Attribute field, String value) -> ___/*???*/.equals(value);
//                                      ???                                  ▲
//                                       │                                   │
//              We know our equality check will look something like this,  ──┘
//              but we still have to define how we get from an Attribute
//              enum back to a real value on an Account
            case Rule.Not(Rule r) -> __/*???*/;
            case Rule.And(Rule a, Rule b) -> __/*???*/;
            case Rule.Or(Rule a, Rule b) -> __/*???*/;
        };
    }














    enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL}
    record Account(){}
    static boolean __;
    static String ___;
    sealed interface Rule {
        record Equals(Attribute field, String value) implements Rule {}
        record And(Rule a, Rule b) implements Rule {}
        record Or(Rule a, Rule b) implements Rule {}
        record Not(Rule rule) implements Rule {}
    }
}
