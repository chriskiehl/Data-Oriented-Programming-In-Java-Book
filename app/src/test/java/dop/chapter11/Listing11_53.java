package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static java.lang.String.format;
import static java.util.stream.Stream.generate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_53 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.53
     * ───────────────────────────────────────────────────────
     * It doesn’t look like much, but it’s one of the most useful tests in a project
     * ───────────────────────────────────────────────────────
     */
    Random rand = new Random();

    Account mkAcnt() {
        //...
        return new Account(
            new AccountId(format("%09d", rand.nextLong(999999999))), //  ┐
            Region.values()[rand.nextInt(Region.values().length)]    //  │◄── (Abbreviated version of the
            //...                                                    //  ┘     randomized account function we’ve
        );                                                           //        been using.)
    }

    @Test
    void roundTrip() {
        generate(this::mkAcnt).limit(10000).forEach(original -> {
            assertEquals(original, deserialize(serialize(original)));
//          ▲
//          └──── This test asserts that I can round-trip the data
//                through my serialization layer without losing fidelity
//                (for some approximation of all possible states)
        });
    }














    enum Region {AMER, LA, EMEA}
    record AccountId(String value){}
    record Account(AccountId id, Region region, Object... rest){}
    String serialize(Account account) { return ""; }
    Account deserialize(String raw) { return new Account(new AccountId(""), Region.AMER); }
}
