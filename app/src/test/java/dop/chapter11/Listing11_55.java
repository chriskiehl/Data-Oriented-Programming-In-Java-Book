package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Character.MAX_CODE_POINT;
import static java.lang.Character.toChars;
import static java.lang.Math.pow;
import static java.lang.String.join;
import static java.util.stream.Stream.generate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_55 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.55
     * ───────────────────────────────────────────────────────
     * Becoming reasonably confident our code is associative
     * ───────────────────────────────────────────────────────
     */
    Random rand = new Random();

    // A few extra helpers for generating random values
    <A> Optional<A> maybe(A value) {
        return rand.nextBoolean()
            ? Optional.of(value)
            : Optional.empty();
    }
    String randStr() {
        return join("",
            generate(() -> toChars(rand.nextInt(MAX_CODE_POINT)))
                .map(String::new)
                .limit(rand.nextInt(10))
                .toList());
    }

    RawData randomRawData() {
        Policy[] policies = Policy.values();                //  ┐
        AuditFinding[] audits = AuditFinding.values();      //  │
        return new RawData(                                 //  │
            maybe(policies[rand.nextInt(policies.length)]), //  │◄── A new random generator for the
            maybe(audits[rand.nextInt(audits.length)]),     //  │    RawData type
            maybe(rand.nextBoolean()),                      //  │
            USD.valueOf(rand.nextDouble(0.0, pow(10, 5))),  //  │
            randStr()                                       //  │
        );                                                  //  ┘
    }

    @Test
    void associativity() {
        IntStream.range(0, 10_000).forEach(__ -> {
//               ▲
//               └──── The magic of generative testing is that we can dial
//                     this number up or down until we’re confident that
//                     our invariant holds
            RawData a = randomRawData();
            RawData b = randomRawData();
            RawData c = randomRawData();
            assertEquals(
                add(a, add(b, c)),              //
                add(add(a, b), c)               //  ◄── The algebraic property is no weaker
            );                                  //      just because it's an approximation of
                                                //      “for all” rather than the real thing.
        });
    }














    public enum Policy {
        GRACE_PERIOD, FLEXIBLE, IMMEDIATE, STRICT, MANUAL_REVIEW
    }
    public enum AuditFinding {
        NO_ISSUE, INACCURATE, BILLING_ERROR, OUT_OF_COMPLIANCE
    }
    public record RawData(
        Optional<Policy> policy,
        Optional<AuditFinding> findings,
        Optional<Boolean> premiumStatus,
        USD total,
        String tag
    ){}
    static RawData add(RawData a, RawData b) { return a; }
}
