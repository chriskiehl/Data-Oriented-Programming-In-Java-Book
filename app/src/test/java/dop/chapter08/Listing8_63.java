package dop.chapter08;

import dop.chapter06.the.implementation.Types.USD;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter08.Listing8_63.Attribute.*;
import static dop.chapter08.Listing8_63.CountryCode.*;
import static dop.chapter08.Listing8_63.Region.EMEA;
import static dop.chapter08.Listing8_63.Region.LATAM;
import static dop.chapter08.Listing8_63.SalesChannel.Direct;
import static dop.chapter08.Listing8_63.Segment.Public;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class Listing8_63 {

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
     * Listing 8.63
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {

        class __ {


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
}
