package dop.chapter08;

import java.util.function.Function;

import static java.lang.String.format;

public class Listing8_50 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.50
     * ───────────────────────────────────────────────────────
     * The full type-safe data model and interpreter
     * ───────────────────────────────────────────────────────
     */
    enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL}
    record Attr<A>(Attribute attribute, Function<Account, A> getter){}
    record Result(Boolean matched, String expected, String found){}

    sealed interface Rule {
        record Equals<A>(Attr<A> field, A value) implements Rule {}
        record Or(Rule a, Rule b) implements Rule {}
        record And(Rule a, Rule b) implements Rule {}
        record Not(Rule rule) implements Rule {}
        default Rule or(Rule b) {
            return new Or(this, b);
        }
        default Rule and(Rule b) {
            return new And(this, b);
        }
    }

    static Attr<SalesChannel> channel =
        new Attr<>(Attribute.CHANNEL, Account::channel);
    static Attr<Sector> sector =
        new Attr<>(Attribute.SECTOR, Account::sector);
    static Attr<CountryCode> country =
        new Attr<>(Attribute.COUNTRY, Account::country);
    static Attr<Region> region =
        new Attr<>(Attribute.REGION, Account::region);
    static Attr<Segment> segment =
        new Attr<>(Attribute.SEGMENT, Account::segment);

    static Result interpret(Account account, Rule rule) {
        return switch (rule) {
            case Rule.Equals(var attr, var value) -> {
                var found = attr.getter().apply(account);
                boolean result = found.equals(value);
                yield new Result(result,
                    format("%s=%s", attr.attribute(), value),
                    format("%s=%s", attr.attribute(), found));
                }
            case Rule.Not(Rule r) -> {
                Result res = interpret(account, r);
                yield new Result(!res.matched(),
                    format("not(%s)", res.expected),
                    res.found());
                }
            case Rule.Or(Rule rule1, Rule rule2) -> {
                Result a = interpret(account, rule1);
                Result b = interpret(account, rule2);
                yield new Result(a.matched() || b.matched(),
                    format("(%s OR %s)", a.expected, b.expected),
                    format("(%s OR %s)", a.found, b.found));
                }
            case Rule.And(Rule rule1, Rule rule2) -> {
                Result a = interpret(account, rule1);
                Result b = interpret(account, rule2);
                yield new Result(a.matched() && b.matched(),
                    format("(%s AND %s)", a.expected, b.expected),
                    format("(%s AND %s)", a.found, b.found));
                }
            };
        };














    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
    enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
    enum SalesChannel {Direct, Partner, Reseller /*...*/}
    record Sector(String value) {}
    record Account(Region region, CountryCode country, Sector sector, Segment segment, SalesChannel channel){}
}
