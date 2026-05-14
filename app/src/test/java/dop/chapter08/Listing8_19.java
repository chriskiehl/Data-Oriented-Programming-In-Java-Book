package dop.chapter08;

import dop.chapter06.the.implementation.Types.USD;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter08.Listing8_19.Attribute.*;
import static dop.chapter08.Listing8_19.CountryCode.*;
import static dop.chapter08.Listing8_19.Region.EMEA;
import static dop.chapter08.Listing8_19.Region.LATAM;
import static dop.chapter08.Listing8_19.Rule.*;
import static dop.chapter08.Listing8_19.SalesChannel.Direct;
import static dop.chapter08.Listing8_19.Segment.Public;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class Listing8_19 {

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

        // NEW!
        static Rule any(Rule a, Rule... rest) {
        return Arrays.stream(rest).reduce(a, Or::new);//  ◄── New rules can be made by
        }//                                                       combining existing ones
        // NEW!
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

    static Rule eq(Attribute field, String value) {
        return new Rule.Equals(field, value);}

    static Rule eq(Attribute field, Enum<?> value) {
        return new Rule.Equals(field, value.toString());}

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.19
     * ───────────────────────────────────────────────────────
     * We don't have to model everything directing "in" our algebra.
     * We can build more convenient and powerful interfaces.
     */
    @Test
    void example() {



        any(eq(COUNTRY, "US"), eq(COUNTRY, "FR"), eq(COUNTRY, "BE")); // ◄───┐ Despite the different APIs, these
        eq(COUNTRY, "NA").or(eq(COUNTRY, "FR")).or(eq(COUNTRY, "BE"));// ◄───┘ two are equivalent


        all(eq(COUNTRY, "US"), eq(SEGMENT, "Enterprise"));// ◄───┐ Same here.
        eq(COUNTRY, "US").and(eq(SEGMENT, "Enterprise")); // ◄───┘

    }
}
