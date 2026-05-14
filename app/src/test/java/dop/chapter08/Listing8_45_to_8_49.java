package dop.chapter08;

import dop.chapter06.the.implementation.Types.USD;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter08.Listing8_45_to_8_49.Attribute.*;
import static dop.chapter08.Listing8_45_to_8_49.CountryCode.*;
import static dop.chapter08.Listing8_45_to_8_49.Region.EMEA;
import static dop.chapter08.Listing8_45_to_8_49.Region.LATAM;
import static dop.chapter08.Listing8_45_to_8_49.SalesChannel.Direct;
import static dop.chapter08.Listing8_45_to_8_49.Segment.Public;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class Listing8_45_to_8_49 {

    // These are reused throughout the examples, so they're defined
    // up here at the top for ease. They're meant to be Generic Accounting
    // Domain-y.
    record SalesOrgId(String value){}
    record AccountId(String value) {}
    enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
    enum SalesChannel {Direct, Partner, Reseller /*...*/}
    enum Region { LATAM, NA, EMEA /*...*/}
    enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
    record Sector(String value) {}

    record Account(
            AccountId accountId,
            Region region,
            CountryCode country,
            Sector sector,
            Segment segment,
            SalesChannel channel
    ){}

    // An arbitrary account.
    // None of the values mean anything.
    // It's just here as a placeholder so
    // the code compiles
    static Account account = new Account(
            new AccountId("12345"),
            EMEA,
            FR,
            new Sector("Retail"),
            Public,
            Direct
    );

    enum Attribute {Region, Country, Sector, Segment, Channel}
    record Attr<A>(Attribute attribute, Function<Account, A> getter){}
    record Result(Boolean matched, String expected, String found){}

        sealed interface Rule_ {
        record Equals<A>(Attr<A> field, A value) implements Rule_ {}
        record Or(Rule_ a, Rule_ b) implements Rule_ {}
        record And(Rule_ a, Rule_ b) implements Rule_ {}
        record Not(Rule_ rule) implements Rule_ {}
        default Rule_ or(Rule_ b) {
            return new Or(this, b);
        }
        default Rule_ and(Rule_ b) {
            return new And(this, b);
        }
        }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.45 through 8.49
     * ───────────────────────────────────────────────────────
     * The whole thing!
     */
    @Test
    void example() {
        // Here's everything we've built in its type safe glory.
        // 50-60 lines of code bought us a ton of functionality.
        class __ {
            static Attr<SalesChannel> channel = new Attr<>(Attribute.Channel, Account::channel);
            static Attr<Sector> sector = new Attr<>(Attribute.Sector, Account::sector);
            static Attr<CountryCode> country = new Attr<>(Attribute.Country, Account::country);
            static Attr<Region> region = new Attr<>(Attribute.Region, Account::region);
            static Attr<Segment> segment = new Attr<>(Attribute.Segment, Account::segment);

            static Result interpret(Account account, Rule_ rule) {
                return switch (rule) {
                    case Rule_.Equals(var attr, var value) -> {
                        var found = attr.getter().apply(account);
                        boolean result = found.equals(value);
                        yield new Result(result,
                                format("%s=%s", attr.attribute(), value),
                                format("%s=%s", attr.attribute(), found));
                    }
                    case Rule_.Not(Rule_ x) -> {
                        Result res = interpret(account, x);
                        yield new Result(!res.matched(),
                                format("not(%s)", res.expected),
                                res.found());
                    }
                    case Rule_.Or(Rule_ rule1, Rule_ rule2) -> {
                        Result a = interpret(account, rule1);
                        Result b = interpret(account, rule2);
                        yield new Result(a.matched() || b.matched(),
                                format("(%s OR %s)", a.expected, b.expected),
                                format("(%s OR %s)", a.found, b.found));
                    }
                    case Rule_.And(Rule_ rule1, Rule_ rule2) -> {
                        Result a = interpret(account, rule1);
                        Result b = interpret(account, rule2);
                        yield new Result(a.matched() || b.matched(),
                                format("(%s AND %s)", a.expected, b.expected),
                                format("(%s AND %s)", a.found, b.found));
                    }
                    // NOT REQUIRED IN THE REAL CODE
                    // This `default` branch is here only because we're redefining
                    // these data types inside a class, inside a method (inside
                    // another class). You can't implement sealed interfaces with
                    // local classes. Thus: this branch just to make things compile.
                    default -> null;
                };
            };
        }
        // But there's still more!
        // The true power of data is that it's scrutable.
        // We can do things with it that we can't with code.
    }
}
