package dop.chapter08;


import dop.chapter06.the.implementation.Types.USD;
import dop.chapter08.Listings.Rule.Or;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter08.Listings.Attribute.*;
import static dop.chapter08.Listings.CountryCode.*;
import static dop.chapter08.Listings.Region.EMEA;
import static dop.chapter08.Listings.Region.LATAM;
import static dop.chapter08.Listings.Rule.*;
import static dop.chapter08.Listings.SalesChannel.Direct;
import static dop.chapter08.Listings.Segment.Public;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Chapter 8 explores the duality between code and data.
 * We can represent one using the other, as well as move
 * freely between the two.
 *
 * We study this by building an interpreter. Or maybe it's
 * a DSL? It doesn't matter. Let's just say we're writing
 * some code.
 */
@Log4j2
public class Listings {


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

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.1
     * ───────────────────────────────────────────────────────
     * This is the simplified view of an Account that we'll use
     * throughout the chapter.
     */
    @Test
    void listing8_1() {

        record Account(
            AccountId accountId,
            Region region,
            CountryCode country,
            Sector sector,
            Segment segment,
            SalesChannel channel
        ){}
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.2
     * ───────────────────────────────────────────────────────
     * This listing just shows the main data types in play.
     * (Repeated from above)
     */
    @Test
    void listing8_2() {
        record SalesOrgId(String value){}
        record AccountId(String value) {}
        enum Segment {Enterprise, Strategic, Existing, Public /*...*/ }
        enum SalesChannel {Direct, Partner, Reseller /*...*/}
        enum Region { LATAM, NA, EMEA /*...*/}
        enum CountryCode {AC, AD, AE, AU, BE, FR, US, /*...*/}
        record Sector(String value) {}
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.3
     * ───────────────────────────────────────────────────────
     * We don't dive into it in the book, but this listing is really
     * in the spirit of the prior chapter "what does it mean to
     * be correct?" question.
     *
     * Before writing any code, we imagine what the bedrock denotation
     * is -- in this case, a function from `Account -> Optional<SalesOrgId>`
     */
    @Test
    void listing8_3() {
        class __ {
            // This interface is not used in the chapter, it's only meant
            // to conceptually show what a rule *means*
            interface Rule extends Function<Account, Optional<SalesOrgId>> {}

            // This function is an example of what a function looks like in Java
            public static Optional<SalesOrgId> orgId1234rule(Account account) {
                return null; // not implemented in the book. Null is returned here
                             // just so the code compiles
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.4
     * ───────────────────────────────────────────────────────
     * What does it mean to be correct?
     *
     * This is another topic we don't dive too far into in the
     * book, but we can dive into the math here because it's fun.
     *
     * [==========YOU CAN SKIP IF NOT INTERESTED================]
     *
     * The challenge in the chapter is to map Accounts to
     * SalesOrgIds. This is a partial, many-to-one mapping.
     * No function is allowed to map the same input to the same
     * output. So, the implication being that if they DO overlap
     * then there is an error.
     *
     * Loosely:
     *
     * $$
     * \forall f_1, f_2 \in F, \forall a \in A, f_1(a) = f_2(a) \implies f_1 = f_2
     * $$
     *
     * This is the property we roll with (informally) in the book.
     *
     * An early reviewer caught that I incorrectly called this injectivity
     * (Whoops!). It's trying to assert a similar property, but about a set
     * of functions, rather than a set of inputs to a single function.
     *
     * A better way of thinking about it is that we're doing set partitioning
     * on B. In this view, the correctness is defined by two properties:
     *
     * Every function must produce a non-overlapping output
     *
     * $$ \bigcap_{f \in F} Image(f) = \emptyset $$
     *
     * and the disjoint union of all outputs should be equal to B
     *
     * $$ B = \bigsqcup_{f \in F} Image(f) $$
     *
     * [========== DISCRETE MATH SECTION DONE ================]
     *
     * Back to the code!
     */
    @Test
    void listing8_4() {
        class __ {
            // Left as an exercise to the reader: defining the set of
            // all rules in the system (see chapter 7!)
            static List<Rule> allKnownRules = List.of();

            static List<Account> allPossibleAccounts() {
                return List.of(); // left as an exercise to the reader
            }
            interface Rule extends Function<Account, Optional<SalesOrgId>> {}

            void assertNoRulesOverlap() {
                for (Account account: allPossibleAccounts()) {   // for all accounts
                    for (Rule a : allKnownRules) {               // for all a, b in Rules
                        for (Rule b : allKnownRules) {           //
                            if (a.apply(account).equals(b.apply(account))) {  // if their output is the same
                                assertEquals(a, b);                           // then they MUST be the same rule
                            }  //            ▲
                               //            └── At this point in the chapter, these technically
                        }      //                aren't comparable, but the code is meant to be
                               //                conceptual.
                    }
                }
            }

        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.5
     * ───────────────────────────────────────────────────────
     * Our first rule!
     * We begin by writing some code.
     */
    @Test
    void listing8_5() {
        class __ {
            /**
             * Rule #1
             * All accounts in EMEA excluding those in Belgium, Austria,
             * and France belong to SalesOrg=111
             */
            static Optional<SalesOrgId> ruleOrg1234(Account account) {
                if (account.region().equals(Region.EMEA)) {
                    Set<CountryCode> excluded = Set.of(BE, AU, FR);
                    if (!excluded.contains(account.country())) {
                        return Optional.of(new SalesOrgId("111"));
                    } else {//                     ▲
                        //                         └───────┐
                        return Optional.empty();  //       │  The code is OK, but it has lots of
                    }//                   ▲                │  branching. Overall it's just kind of
                } else {//                └─────────────── │  ugly.
                    return Optional.empty();//             │
                }//                   ▲                    │  Maybe we could "fix" it?
            }//                       └────────────────────┘
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.6 through 8.7
     * ───────────────────────────────────────────────────────
     * Ah... All better?
     * We did the "pro" developer thing of reducing the individual
     * checks down to one clean boolean expression, but... we
     * lost something along the way.
     */
    @Test
    void listing8_6_to_8_7() {
        class __ {
            /**
             * Rule #1
             * All accounts in EMEA excluding those in Belgium, Austria,
             * and France belong to SalesOrg=111
             */
            static Optional<SalesOrgId> ruleForOrg111(Account account) {
                Set<CountryCode> excluded = Set.of(BE, AU, FR);
                return account.region().equals(Region.EMEA)
                        && !excluded.contains(account.country())
                        ? Optional.of(new SalesOrgId("111"))
                        : Optional.empty();
            }
            // this feels like an OK pattern. Let's write another.
            //
            // Here's our second rule:
            /**
             * Rule #2
             * All non-reseller accounts in Belgium, Austria, and France
             * belong to SalesOrg=222
             */
            static Optional<SalesOrgId> ruleForOrg222(Account account) {
                // This code is "elegant" to look at, but it's an operational
                // and maintenance pain. Collapsing everything down to a single
                // boolean destroys all the useful information we learn.
                // With this setup, simple questions like "Why did(n't) you
                // send Account X to SalesOrg Y?" will leave us stumped.
                Set<CountryCode> included = Set.of(BE, AU, FR);
                return account.region().equals(Region.EMEA)
                        && included.contains(account.country())
                        && account.channel().equals(SalesChannel.Reseller)
                        ? Optional.of(new SalesOrgId("222"))
                        : Optional.empty();
            }
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.8
     * ───────────────────────────────────────────────────────
     * A return to the original approach, but perhaps with an
     * overcorrection.
     */
    @Test
    void listing8_8() {
        class __ {
            /**
             * Rule #1
             * All accounts in EMEA excluding those in Belgium, Austria,
             * and France belong to SalesOrg=111
             */
            static Optional<SalesOrgId> ruleOrg1234(Account account) {
                if (account.region().equals(Region.EMEA)) {
                    log.info("matched region=EMEA");   // ◄───────────────┐  These logs give a ton of operational
                    Set<CountryCode> excluded = Set.of(BE, AU, FR);//     │  monitoring, but at the expense of
                    if (!excluded.contains(account.country())) {//        │  making the code a sea of noise.
                        log.info("matched: {} not in {}", account.country(), excluded);
                        return Optional.of(new SalesOrgId("111"));
                    } else {
                        log.info("Expected country not in {}, found country={}",
                                excluded,
                                account.country());
                        return Optional.empty();
                    }
                } else {
                    log.info("expected region=EMEA, found region={}",
                            account.region());
                    return Optional.empty();
                }
            }
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.9
     * ───────────────────────────────────────────────────────
     * In this book, we've used data to model all kinds of things.
     * Domain information (`NonNegativeInt`, `Degree`), things we "know"
     * like an invoice being `PastDue`. Now we're going to use data
     * to model a computation we want to evaluate *later*.
     */
    @Test
    void listing8_9() {
        // We can model actions like this
        // ```
        // account.country().equals("US")
        // ```
        // with a simple data type.
        record Equals(String field, String value) {}

        Equals rule1 = new Equals("country", "US");
        Equals rule2 = new Equals("country", "BE");
        Equals rule3 = new Equals("country", "FR");
        //                   ▲
        //                   └── These all represent an equality check
        //                       that we want to evaluate later
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.10
     * ───────────────────────────────────────────────────────
     * The current design makes typos our biggest foe
     */
    @Test
    void listing8_10() {
        //               ┌───── This being a string allows us to express silly things
        //               ▼
        record Equals(String field, String value) {}

        //                           ┌───── Good luck noticing this one! (A county is not a CountRy)
        //                           ▼
        Equals rule1 = new Equals("county", "US");
        Equals rule2 = new Equals("i-am-not-valid", "BE");
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.11
     * ───────────────────────────────────────────────────────
     * We'll use an enum to model our attributes and get rid of that
     * pesky string. We'll eventually get rid of the other one, too.
     * But this is good enough to start.
     *
     * Data is great because you don't have to get it 100% right on
     * your first stab. We can start with a lousy algebra and refine
     * it over time.
     */
    @Test
    void listing8_11() {
        //      ┌───── These represent each attribute on our Account model
        //      ▼
        enum Attribute {
            REGION,
            COUNTRY,
            SECTOR,
            SEGMENT,
            CHANNEL
        }
        //               ┌───── NEW!
        //               ▼
        record Equals(Attribute field, String value) {}
        // No more painful typos!
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.12
     * ───────────────────────────────────────────────────────
     * An algebra appears!
     * Now we're getting into something interesting.
     *
     */
    @Test
    void listing8_12() {
        class __ {
            //  ┌─── (modifier is commented out due to limitations on nesting)
            //  ▼
            /*sealed*/ interface Rule {
                record Equals(Attribute field, String value) implements Rule {}
                record And(Rule a, Rule b) implements Rule {}
                record Or(Rule a, Rule b) implements Rule {}
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.13
     * ───────────────────────────────────────────────────────
     * Adding new "functionality" just means adding new data types.
     */
    @Test
    void listing8_13() {
        class __ {
            //  ┌─── (modifier is commented out due to limitations on nesting)
            //  ▼
            /*sealed*/ interface Rule {
                record Equals(Attribute field, String value) implements Rule {}
                record And(Rule a, Rule b) implements Rule {}
                record Or(Rule a, Rule b) implements Rule {}
                //      ┌─── New!
                //      ▼
                record Not(Rule a) implements Rule {}
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.14
     * ───────────────────────────────────────────────────────
     * This algebra is currently super verbose, but we'll fix
     * that in a bit.
     */
    @Test
    void listing8_14() {
        interface Rule {
            record Equals(Attribute field, String value) implements Rule {}
            record And(Rule a, Rule b) implements Rule {}
            record Or(Rule a, Rule b) implements Rule {}
            record Not(Rule a) implements Rule {}
        }
        //         ┌─── This isn't exactly elegant, but it can be make
        //         ▼    so with just a few changes
        new Or(
            new And(new Equals(COUNTRY, "US"), new Equals(SEGMENT, "Strategic")),
            new Not(new Equals(REGION, "AMER")));
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.15 through 8.18
     * ───────────────────────────────────────────────────────
     * This algebra is currently super verbose, but we can fix
     * that with just a few tweaks
     */
    @Test
    void listing8_15_to_8_18() {
        interface Rule_ {
            record Equals(Attribute field, String value) implements Rule_ {}
            record And(Rule_ a, Rule_ b) implements Rule_ {}
            record Or(Rule_ a, Rule_ b) implements Rule_ {}
            record Not(Rule_ a) implements Rule_ {}

            // NEW! 8.15
            static Equals eq(Attribute attr, String val) {  //   ◄───┐
                return new Equals(attr, val);//                      │ These static constructors remove the
            }//                                                      │ visual noise caused by typing "new"
            // NEW! 8.15                                             │ everywhere
            static Not not(Rule_ rule) { //   ◄──────────────────────┘
                return new Rule_.Not(rule);
            }

            // Listing 8.16
            // Adding default methods makes it possible to
            // build large actions fluently
            default Rule_ and(Rule_ other) {
                return new And(this, other);
            }
            default Rule_ or(Rule_ other) {
                return new Or(this, other);
            }
        }
        // Now we've got something really powerful. (8.17)
        Rule rule = (eq(REGION, "EMEA").and(eq(SEGMENT, "Enterprise")))
                        .or(not(eq(REGION, "LATAM")));

        // But can it handle everything? What about more
        // complex code like this? (8.18)
        Set<CountryCode> allowed = Set.of(US, FR, BE);
        if (allowed.contains(account.country())) {
            // ...       ▲
            //           └─ Can we make our algebra support this?
        }//
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.19
     * ───────────────────────────────────────────────────────
     * We don't have to model everything directing "in" our algebra.
     * We can build more convenient and powerful interfaces.
     */
    @Test
    void listing8_19() {
        interface Rule {
            record Equals(Attribute field, String value) implements Rule {}
            record And(Rule a, Rule b) implements Rule {}
            record Or(Rule a, Rule b) implements Rule {}
            record Not(Rule a) implements Rule {}

            // NEW!
            static Rule any(Rule a, Rule... rest) {
                return Arrays.stream(rest).reduce(a, Or::new);//  ◄── New rules can be made by
            }//                                                       combining existing ones
            // NEW!
            static Rule all(Rule a, Rule... rest) {
                return Arrays.stream(rest).reduce(a, And::new);
            }
        }


        any(eq(COUNTRY, "US"), eq(COUNTRY, "FR"), eq(COUNTRY, "BE")); // ◄───┐ Despite the different APIs, these
        eq(COUNTRY, "NA").or(eq(COUNTRY, "FR")).or(eq(COUNTRY, "BE"));// ◄───┘ two are equivalent


        all(eq(COUNTRY, "US"), eq(SEGMENT, "Enterprise"));// ◄───┐ Same here.
        eq(COUNTRY, "US").and(eq(SEGMENT, "Enterprise")); // ◄───┘

    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.20 through 8.22
     * ───────────────────────────────────────────────────────
     * Adding more convenient ways of interacting with the algebra.
     */
    @Test
    void listing8_20_to_8_22() {
        class __ {
            // We can tune the interfaces into our algebra however we want
            //
            // We expose a more convenient interface built on Strings
            static Rule contains(Attribute field, String opt1, String... rest) {
                return Arrays.stream(rest)//
                        .map(value -> eq(field, value))//           │ Inside, we convert the inputs
                        .reduce(eq(field, opt1), Rule::or);//  ◄────┘ back into our Algebra
            }

            void example() {
                // This API allows us to express complicated ideas without
                // unecessary boilerplate.

                eq(COUNTRY, "US").or(eq(COUNTRY, "FR")).or(eq(COUNTRY, "BE"));
                //                ▲
                //                ┌ These two forms produce equivalent data
                //                ▼
                contains(COUNTRY, "US", "FR", "BE");

                // What's really magical is that everything we've added naturally
                // composes with everything else _because_ they're all part of the
                // same algebra. We get composability for free.
                contains(COUNTRY, "US", "FR", "BE").and(eq(SEGMENT, "Public")).or(not(eq(REGION, "LATAM")));
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.23
     * ───────────────────────────────────────────────────────
     * The code so far!
     * Here's the core of the algebra in all of its glory.
     * The helper and convenience methods are elided.
     */
    @Test
    void listing8_23() {
        class __ {
            interface Rule {
                record Equals(Attribute field, String value) implements Rule {}
                record And(Rule a, Rule b) implements Rule {}
                record Or(Rule a, Rule b) implements Rule {}
                record Not(Rule a) implements Rule {}

                static Rule eq(Attribute field, String value) {
                    return new Equals(field, value);}
                static Rule not(Rule rule) {
                    return new Not(rule);
                }
                default Rule and(Rule other) {
                    return new And(this, other);
                }
                default Rule or(Rule other) {
                    return new Or(this, other);
                }
            }
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.24
     * ───────────────────────────────────────────────────────
     * We successfully transformed code into data. Now we transform
     * data into code.
     *
     * This is first of many variations we'll explore.
     */
    @Test
    void listing8_24() {
        /*
        (Commented out so the code will compile)

                    ┌───── The first challenge is deciding what our rules should denote
                    ▼
            static ??? interpret(Rule rule, Account account) {
              return switch (rule) {
                case Eq(Attribute field, String value) -> ???   // Denotations will guide our implementation
                case Not(Rule rule) -> ???
                case And(Rule a, Rule b) -> ???
                case Or(Rule a, Rule b) -> ???
              };
            }
        */
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.26
     * ───────────────────────────────────────────────────────
     * As a first stab, we can convert our data back into the
     * world of boolean logic.
     */
    @Test
    void listing8_25() {
        /*

                  ┌─ Setting the return value to Boolean
              ┌───────┐
        static Boolean interpret(Rule rule, Account account) {
            return switch (rule) {
                case Equals(Attribute field, String value) -> ???.equals(value);
                case Not(Rule rule) -> ???                   └─────────────────┘
                case And(Rule a, Rule b) -> ???                       ▲
                case Or(Rule a, Rule b) -> ???                        └─ We know the equality check should look
            };                                                           something like this, but how do we get
        }                                                                from an Attribute Enum back to a value?
         */

    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.26 through 8.29
     * ───────────────────────────────────────────────────────
     * We touch on this in detail in the book, but part of what
     * makes all of this work is that we're effectively asking
     * our "what does it mean?" question in reverse.
     *
     * And the magic is that we get to decide! We can interpret
     * our data any way we want.
     */
    @Test
    void listing8_26_to_29() {
        class __ {
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

            // Now we can fill in the rest of our interpreter.
            static Boolean interpret(Rule rule, Account account) {
                return switch (rule) {
                    // (8.27) equality in our data (currently) denotes string equality
                    case Equals(Attribute field, String value) -> get(account, field).equals(value);
                    // This code mostly writes itself once you decide on what
                    // the data types denote. We're in the domain of booleans,
                    // so `Not` denotes Logical Not
                    case Not(Rule a) -> !interpret(a, account);
                    // (8.28) And -> denotes logical and `&&`
                    case And(Rule a, Rule b) -> interpret(a, account) && interpret(b, account);
                    // (8.28) and Or -> denotes `||`
                    case Or(Rule a, Rule b) -> interpret(a, account) || interpret(b, account);
                };
            }

            // Listing 8.29
            // Here are our new tools in action!
            static Optional<SalesOrgId> ruleForOrg111(Account account) {
                // The reward of our effort is code that reads like its requirements.
                Rule rule = eq(REGION, "EMEA").and(not(contains(COUNTRY, "US", "BE", "FR")));
                return interpret(rule, account)
                        ? Optional.of(new SalesOrgId("111"))
                        : Optional.empty();
            }

            // (Note: this is just because the listing above depends on it)
            static Rule contains(Attribute field, String opt1, String... rest) {
                return Arrays.stream(rest)
                        .map(value -> eq(field, value))
                        .reduce(eq(field, opt1), Rule::or);
            }

        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.30
     * ───────────────────────────────────────────────────────
     * The best part of everything we've done is that it's just data!
     * We can serialize it, load it, save it, transform it -- anything
     * we want!
     */
    @Test
    void listing8_30() {
        String jsonExample = """
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
            }""";
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
    void listing8_31_to_8_35() {
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


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.36 through 8.39
     * ───────────────────────────────────────────────────────
     * Now it gets really cool. We can make this type safe.
     */
    @Test
    void listing8_36_to_8_39() {
        // Our current design allows us to pass illegal values
        eq(COUNTRY, "Nacho Country");
        eq(REGION, "US"); // This is not a region!



        /**
         * ───────────────────────────────────────────────────────
         * Listing 8.37
         * ───────────────────────────────────────────────────────
         * // We can Parameterize Equality with a generic type variable!
         */
        class __ {
            record Equals(Attribute attribute, String value){}
            //               ▲                    ▲
            //               └────────────────────┘
            //                      └─ Any attribute can pair with any string
        }

        /**
         * ───────────────────────────────────────────────────────
         * Figure
         * ───────────────────────────────────────────────────────
         * We can Parameterize Equality with a generic type variable!
         */
        class ___ {
            record Equals<A>(Attribute attribute, A value) {
            }
            //            ▲                       ▲
            //            └───────────────────────┘
            //                    └─ Now linked by the type system

            /**
             * ───────────────────────────────────────────────────────
             * Listing 8.38
             * ───────────────────────────────────────────────────────
             * Real types!
             */
            void listing_8_38() {
                // Listing 8.38
                // No more strings!
                // Now we can use real types!
                new Equals<>(COUNTRY, CountryCode.US);
                new Equals<>(REGION, Region.EMEA);

            }
            //
            // Except...
            //
            /**
             * ───────────────────────────────────────────────────────
             * Listing 8.39
             * ───────────────────────────────────────────────────────
             * The type safety didn't help us!
             */
            void listing_8_39() {
                // We can still pass illegal values!
                new Equals<>(COUNTRY, Region.EMEA);
                new Equals<>(REGION, CountryCode.US);
            }
            //
            // What we need is a way to make these two arguments *depend* on each other...
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.40
     * ───────────────────────────────────────────────────────
     * We want to parameterize our enum by the same type variable
     * as our value, but this cannot be done in Java
     */
    @Test
    void listing8_40() {
        // Not Allowed
        // enum Attribute<A> { ... }
        //                ▲
    }   //                └─ Invalid Java



    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.41
     * ───────────────────────────────────────────────────────
     * Wrappers to the rescue!
     */
    @Test
    void listing8_41() {
        record Attr<A>(Attribute value){}
        //          ▲
        //          └─ Valid Java!
        //
        // It's not explored in the book, but these are commonly called "Phantom Types".
        // They get the name because they're not "in" the record's body. They exist "around"
        // it as extra type information. This is an extremely useful design technique for
        // increasing type safety in a program. It feels weird at first, but it's super useful.
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.42
     * ───────────────────────────────────────────────────────
     * Static type safety! Hooray!
     */
    @Test
    void listing8_42() {
        // Here's the coolest type-level trick in the whole book:
        // By parameterizing Attr and Equals on the same type var, we
        // can force that they all speak the same language.
        record Attr<A>(Attribute value){}
        record Equals<A>(Attr<A> attribute, A value) {}

        // These act as type "witnesses" (for lack of a better word).
        // They're pre-baked tokens that let Java know what type is
        // being held by Attr
        Attr<SalesChannel> channel = new Attr<>(CHANNEL);
        Attr<Region> region = new Attr<>(REGION);
        // etc...

        // And now we're totally type safe!
        // No more mismatched inputs!
        new Equals<>(channel, SalesChannel.Reseller);
        new Equals<>(region, Region.EMEA);

        // Try uncommenting this. Java won't compile.
        // new Equals<>(channel, Region.EMEA) // Nope!

        // This is awesome, but there's a small problem...
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.43
     * ───────────────────────────────────────────────────────
     * Static type safety! Hooray!
     */
    @Test
    void listing8_43() {
        class __ {
            record Attr<A>(Attribute value){}
            record Equals<A>(Attr<A> attribute, A value) {}

            // Uncomment me to see the compiler errors.
//            static <A> A get(Account account, Attr<A> attr) {
//                return switch (attr.attribute()) {
//                    case Region -> account.region();
//                    case Country -> account.country();
//                    case Sector -> account.sector();
//                    case Segment -> account.segment();
//                    case Channel -> account.channel();
//                };
//            }

            // The problem is that we don't have enough type information
            // available to convince Java that we can safely access those attributes
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.44
     * ───────────────────────────────────────────────────────
     * Giving Java evidence that we know how to access attributes.
     */
    @Test
    void listing8_44() {
        //                 Now the only way to create an attr is by
        //                 supplying evidence that we know how
        //                              ──┐
        //                                ▼
        record Attr<A>(Attribute value, Function<Account, A> getter){}
        // [Side note]
        // This is not explored in the book, but this is the essence of something
        // called the curry howard correspondence. You can view types as claims
        // or propositions. Here we're *claiming* that there exists a function
        // that can go from Attribute to <A>. In this view, programs are proofs.
        // The construction of Attr is _proof_ that this claim is true -- we wouldn't
        // have been able to construct it otherwise!
        //
        // Dependently typed languages like Idris and Agda take this idea to the
        // extreme. They're mind bending and worth checking out.
        // [End Side Note]
        //
        record Equals<A>(Attr<A> attribute, A value) {}

        // These act as type "witnesses" (for lack of a better word).
        // They're pre-baked tokens that let Java know what type is
        // being held by Attr
        Attr<SalesChannel> channel = new Attr<>(CHANNEL, Account::channel);
        Attr<Region> region = new Attr<>(REGION, Account::region);
        // etc...

        // And now we're totally type safe!
        // No more mismatched inputs!
        new Equals<>(channel, SalesChannel.Reseller);
        new Equals<>(region, Region.EMEA);

        // Try uncommenting this. Java won't compile.
        // new Equals<>(channel, Region.EMEA) // Nope!

        // This is awesome, but there's a small problem...
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.45 through 8.49
     * ───────────────────────────────────────────────────────
     * The whole thing!
     */
    @Test
    void listing8_45_to_8_49() {
        // Here's everything we've built in its type safe glory.
        // 50-60 lines of code bought us a ton of functionality.
        class __ {
            enum Attribute {Region, Country, Sector, Segment, Channel}
            record Attr<A>(Attribute attribute, Function<Account, A> getter){}
            record Result(Boolean matched, String expected, String found){}

            /*sealed*/ interface Rule_ {
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


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.50
     * ───────────────────────────────────────────────────────
     * We've secretly been building interpreters since the
     * first chapter! Mwahaha!
     */
    @Test
    void listing8_50() {
        // Remember this?
        // This is data we want to interpret later!
        /*sealed*/ interface RetryDecision {
            record RetryImmediately(/*...*/) implements RetryDecision {}
            record ReattemptLater(/*...*/) implements RetryDecision {}
            record Abandon(/*...*/) implements RetryDecision {}
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.51
     * ───────────────────────────────────────────────────────
     */
    @Test
    void listing8_51() {
        // Rules expressed in Code
        Set<CountryCode> included = Set.of(BE, AU, FR);
        boolean result = included.contains(account.country())
                && (account.segment().equals(Public)
                || !account.region().equals(LATAM));
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.52
     * ───────────────────────────────────────────────────────
     */
    @Test
    void listing8_52() {
        class __ {
            // Note: to get the example in this listing to compile, we have to
            // set up a lot of the data types. The actual listing is below and
            // only two lines long ^_^
            enum Attribute {Region, Country, Sector, Segment, Channel}
            record Attr<A>(Attribute attribute, Function<Account, A> getter){}
            record Result(Boolean matched, String expected, String found){}

            //                       ┌─── Note the trailing underscore (_). This isn't done in the book.
            //                       │    It's done here so we can differentiate the Rule in _this_ example
            //                       ▼    from the statically imported Rule we use in most of the other examples
            /*sealed*/ interface Rule_ {
                record Equals<A>(Attr<A> field, A value) implements Rule_ {}
                record Or(Rule_ a, Rule_ b) implements Rule_ {}
                record And(Rule_ a, Rule_ b) implements Rule_ {}
                record Not(Rule_ rule) implements Rule_ {}

                static <A> Rule_ eq(Attr<A> field, A value) {
                    return new Rule_.Equals<>(field, value);
                }

                static <A> Rule_ not(Rule_ a) {
                    return new Rule_.Not(a);
                }
                default Rule_ or(Rule_ b) {
                    return new Or(this, b);
                }
                default Rule_ and(Rule_ b) {
                    return new And(this, b);
                }
            }

            static Attr<SalesChannel> channel = new Attr<>(Attribute.Channel, Account::channel);
            static Attr<Sector> sector = new Attr<>(Attribute.Sector, Account::sector);
            static Attr<CountryCode> country = new Attr<>(Attribute.Country, Account::country);
            static Attr<Region> region = new Attr<>(Attribute.Region, Account::region);
            static Attr<Segment> segment = new Attr<>(Attribute.Segment, Account::segment);

            @SafeVarargs
            static <A> Rule_ contains(Attr<A> field, A opt1, A... rest) {
                // Note: this is implemented differently from above since
                // we cannot seal the inner Rule_ type used in this Listing
                Rule_ out = new Rule_.Equals<>(field, opt1);
                for (A value : rest) {
                    out = out.or(new Rule_.Equals<>(field, value));
                }
                return out;
            }

            // END SETUP
            // Here's the actual listing in the book:
            /**
             * ───────────────────────────────────────────────────────
             * Listing 8.52
             * ───────────────────────────────────────────────────────
             * Rules as data
             */
            void example() {
                contains(country, US, BE, FR)
                    .and(Rule_.eq(segment, Public).or(Rule_.not(Rule_.eq(region, LATAM))));
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.53
     * ───────────────────────────────────────────────────────
     * Generating documentation automatically
     */
    @Test
    void listing8_53() {
        class __ {
            // The really cool thing about data is that we can just keep
            // adding new ways of interpreting it. Rather than evaluating
            // it to get an answer, we can generate metadata about our data.
            // In this case: generating documentation.
            static String document(Rule rule) {
                return switch (rule) {
                    case Rule.Equals(var field, var value) -> format("%s = %s", field, value);
                    case Rule.Not(Rule r) -> format("not (%s)", document(r));
                    case Rule.Or(Rule a, Rule b) -> format("(%s or %s)", document(a), document(b));
                    case Rule.And(Rule a, Rule b) -> format("(%s and %s)", document(a), document(b));
                };
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.54
     * ───────────────────────────────────────────────────────
     * Listing 8.54 works, but it's output will be tough for
     * our end users to read. Is a sea of nested parentheses
     */
    @Test
    void listing8_54() {
        // Example output:
        //
        //  (((COUNTRY = US or COUNTRY = BE) or COUNTRY = FR)
        //   and (SEGMENT = public or not (REGION = LATAM)))
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.55
     * ───────────────────────────────────────────────────────
     * Instead of outputting text, we could output the tree that
     * our rules form. You can see this tree if you call toString()
     * on a rule and line up its output.
     */
    @Test
    void listing8_55() {
        //  ┌───── Here's the root of the tree
        //  ▼
        // And[
        //    a=Or[
        //      a=Or[a=Equals[field=COUNTRY, value=US],     ◄── Here's a leaf node
        //           b=Equals[field=COUNTRY, value=BE]],
        //      b=Equals[field=COUNTRY, value=FR]],
        //    b=Or[
        //      a=Equals[field=SEGMENT, value=public],
        //      b=Not[a=Equals[field=REGION, value=LATAM]]
        //  ]
        //]
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.56
     * ───────────────────────────────────────────────────────
     * Here's a quick tutorial on GraphVis' file format
     */
    @Test
    void listing8_56() {
        //    ┌───── This is the Node's ID
        //    ▼
        // node_123 [label="Hello"]
        // node_456 [label="World"]
        //              ▲
        //              └── This controls what displays
        //
        // Edges
        //node_123 -> node_456
        //         ▲
        //         └── This ascii arrow says there's an edge from Node_123 to Node_456
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.57
     * ───────────────────────────────────────────────────────
     * Let's write a few helpers to transform our Rules into GraphVis
     */
    @Test
    void listing8_57() {
        class __ {
            //
            // This generates a unique(ish) ID. A proper SHA would avoid
            // collisions, but this is good enough for our purposes.
            static String id(Rule rule) {
                return "node_" + Integer.toHexString(rule.toString().hashCode());
            }

            // Edges are simple. An ascii arrow (->) between the two ids
            static String edge(Rule from, Rule to) {
                return format("%s -> %s", id(from), id(to));
            }


            // To make the visualization pretty, we give the nodes different
            // styling rules depending on if they’re a leaf node or a branch.
            static String node(Rule rule) {
                return switch (rule) {
                    case Equals(var field, var value)
                        -> format("%s [label=\"%s=%s\"]", id(rule), field, value);
                    default
                        -> format("%s [label=\"%s\"]", id(rule), rule.getClass().getSimpleName());
                };
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.58
     * ───────────────────────────────────────────────────────
     * You could tackle generating the GraphVis in a number of ways
     * depending on how formal you want to be. In the book, we just
     * generate it in one go as we traverse the Rule tree.
     */
    @Test
    void listing8_58() {
        class __ {
            // Quick helper for combining an arbitrary number of streams
            @SafeVarargs
            static <A> Stream<A> concat(Stream<A>...ys) {
                return Arrays.stream(ys).reduce(Stream.of(), Stream::concat);
            }

            // This follows the same basic shape as our other interpreters.
            // We traverse the data and apply some action. In this case, a
            // transform from Rule -> GraphVis
            static Stream<String> nodesAndEdges(Rule rule) {
                return switch (rule) {
                    case Equals e -> Stream.of(node(e));
                    case Not not -> concat(
                            Stream.of(node(not)),
                            Stream.of(edge(not, not.a)),
                            nodesAndEdges(not.a()));
                    case Or or -> concat(
                            Stream.of(node(or)),
                            Stream.of(edge(or, or.a)),
                            Stream.of(edge(or, or.b)),
                            nodesAndEdges(or.a()),
                            nodesAndEdges(or.b()));
                    case And and -> concat(
                            Stream.of(node(and)),
                            Stream.of(edge(and, and.a)),
                            Stream.of(edge(and, and.b)),
                            nodesAndEdges(and.a()),
                            nodesAndEdges(and.b()));
                };
            }

            // Finally we stitch everything together into the GraphVis format
            static String toGraphVis(Rule rule) {
                return String.format("""
                  digraph Rule {
                      rankdir=TD;
                      %s
                  }""", String.join("\n\t", nodesAndEdges(rule).toList()));
            }
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.59
     * ───────────────────────────────────────────────────────
     * Two very different views of the same data
     */
    @Test
    void listing8_59() {
        /*
        // Data in code
        contains(COUNTRY, US, BE, FR)
            .and(eq(SEGMENT, "public").or(not(eq(REGION, LATAM))))

        // Data in GraphVis
        digraph Rule {
            rankdir=TD;
            node_9d54a989 [label="And"]
            node_9d54a989 -> node_cf7ac4e3
            node_9d54a989 -> node_141bf586
            node_cf7ac4e3 [label="Or"]
            node_cf7ac4e3 -> node_a4bf6d1d
            node_cf7ac4e3 -> node_85ef509a
            node_a4bf6d1d [label="Or"]
            node_a4bf6d1d -> node_85ef8908
            node_a4bf6d1d -> node_85ef4003
            node_85ef8908 [label="COUNTRY=US"]
            node_85ef4003 [label="COUNTRY=BE"]
            node_85ef509a [label="COUNTRY=FR"]
            node_141bf586 [label="Or"]
            node_141bf586 -> node_930ad8e0
            node_141bf586 -> node_a5e4027c
            node_930ad8e0 [label="SEGMENT=public"]
            node_a5e4027c [label="Not"]
            node_a5e4027c -> node_b83f3b85
            node_b83f3b85 [label="REGION=LATAM"]
        }
         */
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.60
     * ───────────────────────────────────────────────────────
     * We can keep going! Let's extend our algebra to support
     * comparing two values.
     */
    @Test
    void listing8_60() {
        record Account(
                AccountId accountId,
                Region region,
                CountryCode country,
                Sector sector,
                Segment segment,
                SalesChannel channel,
                USD totalSpend // NEW!
        ){}
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.61
     * ───────────────────────────────────────────────────────
     */
    @Test
    void listing8_61() {
        record Account(
                AccountId accountId,
                Region region,
                CountryCode country,
                Sector sector,
                Segment segment,
                SalesChannel channel,
                USD totalSpend // NEW!
        ){}

        class __ {
            enum Attribute {Region, Country, Sector, Segment, Channel}
            record Attr<A>(Attribute attribute, Function<Listings.Account, A> getter){}
            record Result(Boolean matched, String expected, String found){}
            /*sealed*/ interface Rule_ {
                record Equals<A>(Attr<A> field, A value) implements Rule_ {}
                record Or(Rule_ a, Rule_ b) implements Rule_ {}
                record And(Rule_ a, Rule_ b) implements Rule_ {}
                record Not(Rule_ rule) implements Rule_ {}
                // NEW!
                //                           ┌───── This is the key part. We force that `A` be comparable
                //                           ▼
                //                 ┌──────────────────────┐
                record GreaterThan<A extends Comparable<A>>(Attr<A> attr, A value) implements Rule_ {}
                record LessThan<A extends Comparable<A>>(Attr<A> attr, A value) implements Rule_ {}
            }
        }
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.62
     * ───────────────────────────────────────────────────────
     */
    @Test
    void listing8_62() {
        // Note: Commented out because this listing is showing
        // code that will not compile.
        // static Result interpret(Rule rule, Account account) {
        //    return switch (rule) {
        //        case Eq(var field, var value) -> {...}
        //        ...
        //        case GreaterThan<?> gt -> {
        //
        //                       ┌─────  Unlike our equals implementation which could “see” an object, Java
        //                       │       “sees” two Comparable instances of possibly any type
        //                       ▼
        //            Comparable<?> found = gt.attr().getter().apply(thing);  #A
        //            Comparable<?> expected = gt.value()                     #A
        //            boolean result = found.compareTo(value) > 0;  // ERROR  #B
        //                                     ▲
        //                                     └── And it can’t tell that they’re the same, so we get a
        //                                         compile error if we try to compare them
        //        }
        //    };
        //}
    }


    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.63
     * ───────────────────────────────────────────────────────
     */
    @Test
    void listing8_63() {

        class __ {
            enum Attribute {Region, Country, Sector, Segment, Channel}
            record Attr<A>(Attribute attribute, Function<Listings.Account, A> getter){}
            record Result(Boolean matched, String expected, String found){}
            /*sealed*/ interface Rule_ {
                record Equals<A>(Attr<A> field, A value) implements Rule_ {}
                record Or(Rule_ a, Rule_ b) implements Rule_ {}
                record And(Rule_ a, Rule_ b) implements Rule_ {}
                record Not(Rule_ rule) implements Rule_ {}
                // NEW!
                //                           ┌───── This is the key part. We force that `A` be comparable
                //                           ▼
                //                 ┌──────────────────────┐
                record GreaterThan<A extends Comparable<A>>(Attr<A> attr, A value) implements Rule_ {}
                record LessThan<A extends Comparable<A>>(Attr<A> attr, A value) implements Rule_ {}
            }

            // LISTING:
            // This gives java the needed type information to get us out of Comparator<?> and
            // into Comparator<A> where everything is the same
            static <A extends Comparable<A>> int compareTo(Rule_.GreaterThan<A> gt, Account account) {
                A found = gt.attr().getter().apply(account);
                return found.compareTo(gt.value());
            }


            static Result eval(Rule_ rule, Account account) {
                return switch (rule) {
                    // ...
                    case Rule_.GreaterThan<?> gt -> {
                        Comparable<?> found = gt.attr().getter.apply(account);
                        Comparable<?> expected = gt.value();
                        boolean result = compareTo(gt, account) > 0;
                        yield new Result(result,
                                format("%s>%s", gt.attr().attribute(), expected),
                                format("%s=%s", gt.attr().attribute(), found));
                    }
                    default -> null; // Only here so the example compiles.
                };
            }
        }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.64
     * ───────────────────────────────────────────────────────
     * Comparisons in action. Pretty cool, right?
     */
    @Test
    void listing8_64() {
        /*
        eq(Region, EMEA).and(gt(USD.of(10_000_000.0)))

        Result[matched=false,
            expected=(Region=EMEA AND Sales>=10,000,000.0),
            found=( Region=EMEA AND Sales=500,000.00)]

         */
    }




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

}
