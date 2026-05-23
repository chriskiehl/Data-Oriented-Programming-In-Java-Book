package dop.chapter08;

public class Listing8_62 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.62
     * ───────────────────────────────────────────────────────
     * Adding a new option to our algebra
     * ───────────────────────────────────────────────────────
     */
    sealed interface Rule {
        record Equals(/*...*/) implements Rule {}

//              Adding the ability to describe comparisons between
//              attributes.  ─────────────┐
//                                        ▼
//                                   ┌─────────────┐
        record GreaterThan<A extends Comparable<A>>(Attr<A> attr, A value) implements Rule{}
//                                   └─────────────┘
//                                ┌─────────────┐
        record LessThan<A extends Comparable<A>>(Attr<A> attr, A value) implements Rule{}
//                                └─────────────┘



    }














    record Attr<A>(Attribute attribute){}
    enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL}
}
