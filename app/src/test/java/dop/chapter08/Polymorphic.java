package dop.chapter08;

//import dop.chapter06.the.implementation.Types.USD;
//import dop.chapter08.Polymorphic.Rule.And;
//import dop.chapter08.Polymorphic.Rule.Eq;
//import dop.chapter08.Polymorphic.Rule.Gt;
//import dop.chapter08.Polymorphic.Rule.Or;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.Set;
//import java.util.function.Function;
//
//import static java.lang.String.format;


public class Polymorphic {
//
//    record Account(
//            SalesChannel channel,
//            Sector sector,
//            Segment segment,
//            Region region,
//            CountryCode country,
//            Double totalSpend
//    ){}
//
//
//
//    enum SalesChannel {Internal,External}
//    enum Segment {Enterprise, Strategic, Existing }
//    enum CountryCode {AC, AD, AE, /*...*/ }
//    enum Region {AMER,EMEA,LATAM,NA}
//    record Sector(String value){}
//
//
//    enum Attribute {Region, Country, Sector, Segment, Channel, TotalSpend}
//    record Attr<A>(Attribute attribute, Function<Account, A> getter){}
//    record Result(
//        Boolean matched,
//        String expected,
//        String found
//    ){}
//
//    sealed interface Rule {
//        record Eq<A>(Attr<A> field, A value) implements Rule {}
//        record Gt<A extends Comparable<A>>(Attr<A> attr, A value) implements Rule{}
//        record Or(Rule a, Rule b) implements Rule {}
//        record And(Rule a, Rule b) implements Rule {}
//        record Not(Rule rule) implements Rule {}
//
//        default Rule or(Rule b) {
//            return new Or(this, b);
//        }
//        default Rule and(Rule b) {
//            return new And(this, b);
//        }
//    }
//
//    static <A> Rule eq(Attr<A> field, A value) {
//        return new Rule.Eq<>(field, value);
//    }
//
//    static <A> A get(Account account, Attr<A> attr) {
//        return attr.getter().apply(account);
//    }
//
//    static Attr<SalesChannel> channel = new Attr<>(Attribute.Channel, Account::channel);
////    static Attr<Sector> sector = new Attr<>(Attribute.Sector, Account::sector);
//    static Attr<CountryCode> country = new Attr<>(Attribute.Country, Account::country);
//    static Attr<Region> region = new Attr<>(Attribute.Region, Account::region);
//    static Attr<Segment> segment = new Attr<>(Attribute.Segment, Account::segment);
//    static Attr<Double> totalSpend = new Attr<>(Attribute.TotalSpend, Account::totalSpend);
//
//    static <A extends Comparable<A>> int compareTo(Gt<A> gt, Account thing) {
//        A found = gt.attr().getter().apply(thing);
//        return found.compareTo(gt.value());
//    }
//
//
//    static Rule any(Rule a, Rule... rest) {
//        return Arrays.stream(rest).reduce(a, Or::new);
//    }
//    static Rule all(Rule a, Rule... rest) {
//        return Arrays.stream(rest).reduce(a, And::new);
//    }
//
//    static Rule contains(Attribute attribute, String opt1, String... rest) {
//        return any(eq(attribute, opt1), Arrays.stream(rest)
//                .map(s -> eq(attribute, s))
//                .toArray(Rule[]::new));
//    }
//
//
//
//
//
//    static Result interpret(Account account, Rule rule) {
//        return switch (rule) {
//            case Eq(var attr, var value) -> {
//                var found = get(account, attr);
//                boolean result = found.equals(value);
//                yield new Result(result,
//                        format("%s=%s", attr.attribute(), value),
//                        format("%s=%s", attr.attribute(), found));
//            }
//            case Gt<?> gt -> {
//                Comparable<?> found = get(account, gt.attr());
//                Comparable<?> expected = gt.value();
//                boolean result = compareTo(gt, account) > 0;
//                yield new Result(result,
//                        format("%s>%s", gt.attr().attribute(), expected),
//                        format("%s=%s", gt.attr().attribute(), found));
//            }
//            case Rule.Not(Rule x) -> {
//                Result res = interpret(account, x);
//                yield new Result(!res.matched(), format("not(%s)", res.expected), res.found());
//            }
//            case Or(Rule rule1, Rule rule2) -> {
//                Result a = interpret(account, rule1);
//                Result b = interpret(account, rule2);
//                yield new Result(a.matched() || b.matched(),
//                    format("(%s OR %s)", a.expected, b.expected),
//                    format("(%s OR %s)", a.found, b.found));
//            }
//            case And(Rule rule1, Rule rule2) -> {
//                Result a = interpret(account, rule1);
//                Result b = interpret(account, rule2);
//                yield new Result(a.matched() || b.matched(),
//                        format("(%s AND %s)", a.expected, b.expected),
//                        format("(%s AND %s)", a.found, b.found));
//            }
//        };
//    };
//
//    /**
//     *
//     */
//    @Test
//    void explore() {
//        Account account = new Account(SalesChannel.External, new Sector("Bar"), Segment.Enterprise, Region.AMER, CountryCode.AE, 10.0);
//        System.out.println("Hello.");
//        System.out.println(interpret(account,
//                new Eq<>(country, CountryCode.AC)
//                    .or(new Gt<>(totalSpend, 9.00))
//                    .or(new Eq<>(country, CountryCode.AD))
//                    .or(new Eq<>(region, Region.EMEA))
//        ));
////        System.out.println(eval(new Account(External, Bar), new Term2.Eq2<>($channel2, External)));
////        System.out.println(foo(new Term.Eq3<>(new $Sector(), Sector.Bar)));
//
//    }
}
