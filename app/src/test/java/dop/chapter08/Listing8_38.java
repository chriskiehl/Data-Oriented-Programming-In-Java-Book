package dop.chapter08;

public class Listing8_38 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.38
     * ───────────────────────────────────────────────────────
     * The current modeling of the Equals data type
     * ───────────────────────────────────────────────────────
     */
    record Equals(Attribute attribute, String value){}
            //         ▲                  ▲
            //         └──── Any attribute can be paired with any string














    enum Attribute {REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL}
}
