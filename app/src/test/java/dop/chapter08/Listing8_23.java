package dop.chapter08;

import dop.chapter06.the.implementation.Types.USD;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter08.Listing8_23.Attribute.*;
import static dop.chapter08.Listing8_23.CountryCode.*;
import static dop.chapter08.Listing8_23.Region.EMEA;
import static dop.chapter08.Listing8_23.Region.LATAM;
import static dop.chapter08.Listing8_23.Rule.*;
import static dop.chapter08.Listing8_23.SalesChannel.Direct;
import static dop.chapter08.Listing8_23.Segment.Public;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class Listing8_23 {

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

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.23
     * ───────────────────────────────────────────────────────
     * The code so far!
     * Here's the core of the algebra in all of its glory.
     * The helper and convenience methods are elided.
     */
    @Test
    void example() {
        class __ {

        }
    }
}
