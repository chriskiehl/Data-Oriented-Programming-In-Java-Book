package dop.chapter08;

import dop.chapter06.the.implementation.Types.USD;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter08.Listing8_61.Attribute.*;
import static dop.chapter08.Listing8_61.CountryCode.*;
import static dop.chapter08.Listing8_61.Region.EMEA;
import static dop.chapter08.Listing8_61.Region.LATAM;
import static dop.chapter08.Listing8_61.SalesChannel.Direct;
import static dop.chapter08.Listing8_61.Segment.Public;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class Listing8_61 {

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
        // NEW!
        //                           ┌───── This is the key part. We force that `A` be comparable
        //                           ▼
        //                 ┌──────────────────────┐
        record GreaterThan<A extends Comparable<A>>(Attr<A> attr, A value) implements Rule_ {}
        record LessThan<A extends Comparable<A>>(Attr<A> attr, A value) implements Rule_ {}
        }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.61
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
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

        }
    }
}
