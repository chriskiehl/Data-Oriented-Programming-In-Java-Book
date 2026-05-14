package dop.chapter08;

import dop.chapter06.the.implementation.Types.USD;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter08.Listing8_52.Attribute.*;
import static dop.chapter08.Listing8_52.CountryCode.*;
import static dop.chapter08.Listing8_52.Region.EMEA;
import static dop.chapter08.Listing8_52.Region.LATAM;
import static dop.chapter08.Listing8_52.SalesChannel.Direct;
import static dop.chapter08.Listing8_52.Segment.Public;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class Listing8_52 {

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

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.52
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        class __ {
            // Note: to get the example in this listing to compile, we have to
            // set up a lot of the data types. The actual listing is below and
            // only two lines long ^_^



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
}
