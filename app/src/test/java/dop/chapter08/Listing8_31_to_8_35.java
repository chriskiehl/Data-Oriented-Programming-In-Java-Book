package dop.chapter08;

import dop.chapter06.the.implementation.Types.USD;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter08.Listing8_31_to_8_35.Attribute.*;
import static dop.chapter08.Listing8_31_to_8_35.CountryCode.*;
import static dop.chapter08.Listing8_31_to_8_35.Region.EMEA;
import static dop.chapter08.Listing8_31_to_8_35.Region.LATAM;
import static dop.chapter08.Listing8_31_to_8_35.Rule.*;
import static dop.chapter08.Listing8_31_to_8_35.SalesChannel.Direct;
import static dop.chapter08.Listing8_31_to_8_35.Segment.Public;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class Listing8_31_to_8_35 {

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
     * Listing 8.31 through 8.35
     * ───────────────────────────────────────────────────────
     * Now we begin the second variation.
     * Booleans are cool, but cause a blindness in code. The
     * reduce everything we learn down to binary yes / no.
     *
     * Let's make a better Boolean.
     */
    @Test
    void example() {
        class __ {
            record Result(
//                 ┌───  Our core data type is still a boolean underneath the covers
//                 ▼
                boolean matched,
                String expected, // ◄──┐ But we upgrade it to carry around
                String found     //    │ why it’s in its current state
            ){}

//                   ┌───  Our new interpretation of the data returns a Result type, rather than a plain Boolean
//                   ▼
            static Result interpret(Rule rule, Account account) {
                return switch (rule) {
                    case Equals(Attribute field, String value) -> {
//                                               ┌── The main logic is still exactly the same.
//                                               ▼   We compare two strings.
                        String found = get(account, field);
                        boolean result = found.equals(value);
                        yield new Result(
                                result,                         //     │ But now we keep track of what those
                                format("%s=%s", field, value),  // ◄───┘ comparisons were and whether they matched
                                format("%s=%s", field, found)); // ◄───┘ what we expected
                    }
                    case Not(Rule a) -> {
                        Result res = interpret(a, account);
                        yield new Result(                  //     │ We're still just a boolean. Not simply
                                !res.matched(),            // ◄───┘ inverts the result it finds
                                format("not(%s)", res.expected),
                                res.found());
                    }
                    // AND and OR work exactly the same. Evaluate each expression and record the findings
                    case And(Rule rule1, Rule rule2) -> {
                        Result a = interpret(rule1, account);
                        Result b = interpret(rule2, account);
                        yield new Result(a.matched() && b.matched(),
                                format("(%s AND %s)", a.expected, b.expected),
                                format("(%s AND %s)", a.found, b.found));
                    }
                    case Or(Rule rule1, Rule rule2) -> {
                        Result a = interpret(rule1, account);
                        Result b = interpret(rule2, account);
                        yield new Result(a.matched() && b.matched(),
                                format("(%s OR %s)", a.expected, b.expected),
                                format("(%s OR %s)", a.found, b.found));
                    }
                };
            }

            /**
             * Listing 8.35
             * Full information on why we matched or didn't
             */
            void DEMO() {
                Account account = new Account(
                        new AccountId("1234"),
                        Region.EMEA,
                        CountryCode.BE,
                        new Sector("Retail"),
                        Segment.Strategic,
                        SalesChannel.Reseller
                );

                Rule rule = (not(eq(COUNTRY, "US")).and(eq(REGION, "AMER")))
                        .or(contains(SEGMENT, "Enterprise", "Strategic"));
                // No more Boolean Blindness!
                // Our interpreter produces a report of WHY it made particular decision.
                interpret(rule, account);
                // [out]
                // Result[
                //     matched=false,
                //     expected=((not(country=US) AND Region=AMER)
                //                 OR (Segment=Enterprise OR Segment=Strategic)),
                //     found=((country=BE AND Region=EMEA)
                //                 OR (Segment=Enterprise OR Segment=Strategic))]

            }
        }

    }
}
