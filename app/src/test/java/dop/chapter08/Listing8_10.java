package dop.chapter08;

import org.junit.jupiter.api.Test;

public class Listing8_10 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.10
     * ───────────────────────────────────────────────────────
     * The current design makes typos our biggest foe
     */
    @Test
    void example() {
        //               ┌───── This being a string allows us to express silly things
        //               ▼
        record Equals(String field, String value) {}

        //                           ┌───── Good luck noticing this one! (A counTY is not a CounTRY)
        //                           ▼
        Equals rule1 = new Equals("county", "US");
        Equals rule2 = new Equals("i-am-not-valid", "BE");
    }
}
