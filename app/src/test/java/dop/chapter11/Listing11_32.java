package dop.chapter11;

import java.time.Instant;

import static dop.chapter11.Listing11_32.Segment.ENTERPRISE;

public class Listing11_32 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.32
     * ───────────────────────────────────────────────────────
     * The seeds of our demise
     * ───────────────────────────────────────────────────────
     */
    Account mkAcnt(Region region, USD spend) {
        return new Account(
           new AccountId("000000001"),
           region,
           spend,
           Segment.ENTERPRISE,
           new Sector("Education"),
           Instant.now()
        );
    }














    enum Region {AMER}
    enum Segment {ENTERPRISE}
    record AccountId(String value){}
    record Sector(String value){}
    record Account(AccountId id, Region region, USD spend, Segment segment, Sector sector, Instant updatedOn){}
}
