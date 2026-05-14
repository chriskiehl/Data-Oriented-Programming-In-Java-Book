package dop.chapter08;

import dop.chapter06.the.implementation.Types.USD;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter08.Listing8_41.Attribute.*;
import static dop.chapter08.Listing8_41.CountryCode.*;
import static dop.chapter08.Listing8_41.Region.EMEA;
import static dop.chapter08.Listing8_41.Region.LATAM;
import static dop.chapter08.Listing8_41.Rule.*;
import static dop.chapter08.Listing8_41.SalesChannel.Direct;
import static dop.chapter08.Listing8_41.Segment.Public;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class Listing8_41 {

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

    enum Attribute {
        REGION,
        COUNTRY,
        SECTOR,
        SEGMENT,
        CHANNEL
    }
    sealed interface Rule {
        record Equals(Attribute field, String value) implements Rule {}
        record And(Rule a, Rule b) implements Rule {}
        record Or(Rule a, Rule b) implements Rule {}
        record Not(Rule a) implements Rule {}

        static Rule eq(Attribute field, String value) {
            return new Equals(field, value);}

        static Rule eq(Attribute field, Enum<?> value) {
            return new Equals(field, value.toString());}

        static Rule not(Rule rule) {
            return new Not(rule);
        }
        static Rule any(Rule a, Rule... rest) {
            return Arrays.stream(rest).reduce(a, Or::new);
        }
        static Rule all(Rule a, Rule... rest) {
            return Arrays.stream(rest).reduce(a, And::new);
        }

        default Rule and(Rule other) {
            return new And(this, other);
        }
        default Rule or(Rule other) {
            return new Or(this, other);
        }
    }

    static Rule contains(Attribute field, String opt1, String... rest) {
        return Arrays.stream(rest)
                .map(value -> eq(field, value))
                .reduce(eq(field, opt1), Rule::or);
    }


    static String get(Account account, Attribute attr) {
        // (8.26) The exhaustiveness of the switch guarantees type safety!
        // Java won't compile if we miss a case.
        return switch (attr) {
            // In the current variation, we're interpreting everything
            // as plain strings (we'll fix that shortly!), we "bottom out"
            // every attribute on its string value
            case Attribute.REGION -> account.region().name();
            case Attribute.COUNTRY -> account.country().name();
            case Attribute.SECTOR -> account.sector().value();
            case Attribute.SEGMENT -> account.segment().name();
            case Attribute.CHANNEL -> account.channel().name();
        };
    }
    static String id(Rule rule) {
        return "node_" + Integer.toHexString(rule.toString().hashCode());
    }

    static String node(Rule rule) {
        return switch (rule) {
            case Equals(var field, var value) -> format("%s [label=\"%s=%s\"]", id(rule), field, value);
            default -> format("%s [label=\"%s\"]", id(rule), rule.getClass().getSimpleName());
        };
    }

    static String edge(Rule from, Rule to) {
        return format("%s -> %s", id(from), id(to));
    }

    static Stream<String> collectNodesAndEdges(Rule rule) {
        return switch (rule) {
            case Equals e -> Stream.of(node(e));
            case Not not -> concat(
                    Stream.of(node(not)),
                    Stream.of(edge(not, not.a)),
                    collectNodesAndEdges(not.a()));
            case Or or -> concat(
                    Stream.of(node(or)),
                    Stream.of(edge(or, or.a)),
                    Stream.of(edge(or, or.b)),
                    collectNodesAndEdges(or.a()),
                    collectNodesAndEdges(or.b()));
            case And and -> concat(
                    Stream.of(node(and)),
                    Stream.of(edge(and, and.a)),
                    Stream.of(edge(and, and.b)),
                    collectNodesAndEdges(and.a()),
                    collectNodesAndEdges(and.b()));
        };
    }

    @SafeVarargs
    static <A> Stream<A> concat(Stream<A>...ys) {
        return Arrays.stream(ys).reduce(Stream.of(), Stream::concat);
    }

    static String graphVis(Rule rule) {
        return String.format("""
                digraph Rule {
                    rankdir=TD;
                    %s
                }""", String.join("\n\t", collectNodesAndEdges(rule).toList()));
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.41
     * ───────────────────────────────────────────────────────
     * Wrappers to the rescue!
     */
    @Test
    void example() {
        record Attr<A>(Attribute value){}
        //          ▲
        //          └─ Valid Java!
        //
        // It's not explored in the book, but these are commonly called "Phantom Types".
        // They get the name because they're not "in" the record's body. They exist "around"
        // it as extra type information. This is an extremely useful design technique for
        // increasing type safety in a program. It feels weird at first, but it's super useful.
    }
}
