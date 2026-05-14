package dop.chapter08;

import dop.chapter06.the.implementation.Types.USD;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter08.Listing8_14.Attribute.*;
import static dop.chapter08.Listing8_14.CountryCode.*;
import static dop.chapter08.Listing8_14.Region.EMEA;
import static dop.chapter08.Listing8_14.Region.LATAM;
import static dop.chapter08.Listing8_14.Rule.*;
import static dop.chapter08.Listing8_14.SalesChannel.Direct;
import static dop.chapter08.Listing8_14.Segment.Public;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class Listing8_14 {

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
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.14
     * ───────────────────────────────────────────────────────
     * This algebra is currently super verbose, but we'll fix
     * that in a bit.
     */
    @Test
    void example() {

        //         ┌─── This isn't exactly elegant, but it can be make
        //         ▼    so with just a few changes
        new Or(
            new And(new Equals(COUNTRY, "US"), new Equals(SEGMENT, "Strategic")),
            new Not(new Equals(REGION, "AMER")));
    }
}
