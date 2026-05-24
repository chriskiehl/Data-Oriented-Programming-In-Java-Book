package dop.chapter11;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.String.format;

public class Listing11_34 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.34
     * ───────────────────────────────────────────────────────
     * Generating randomized test data
     * ───────────────────────────────────────────────────────
     */
    Random rand = new Random();

    Account mkAcnt() {
        List<String> sectors = List.of(
            "Finance",
            "healthcare",
            "..."
        );
        return new Account(
            new AccountId(format("%09d", rand.nextLong(999999999))),         //  ┐
            Region.values()[rand.nextInt(Region.values().length)],           //  │
            new USD(BigDecimal.valueOf(rand.nextDouble(USD_ZERO, USD_MAX))), //  │
            Segment.values()[rand.nextInt(Segment.values().length)],         //  │ Rather than us hard coding values,
            new Sector(sectors.get(rand.nextInt(sectors.size()))),           //  │ we let Random pick them for us
            Instant.ofEpochSecond(rand.nextLong(99999999999L))               //  ┘
        );
    }


    // Random values can be bound to sane ranges.
    // More money than exists in the world
    // is probably a safe top end
    static double USD_ZERO = 0.0;
    static double USD_MAX = pow(10, 15);
















    enum Region {AMER, LA, EMEA}
    enum Segment {ENTERPRISE, STRATEGIC, EXISTING}
    record AccountId(String value){}
    record Sector(String value){}
    record Account(AccountId id, Region region, USD spend, Segment segment, Sector sector, Instant updatedOn){}
}
