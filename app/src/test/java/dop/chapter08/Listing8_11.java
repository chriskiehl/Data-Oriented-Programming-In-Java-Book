package dop.chapter08;

public class Listing8_11 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.11
     * ───────────────────────────────────────────────────────
     * Typos will be our toughest enemy
     * ───────────────────────────────────────────────────────
     */
    record Equals(String attributeName, String value){};
//  ▲
//  └──── This String allows many crimes to be committed

    void example() {
        Equals rule1 = new Equals("county", "US");
                        //            ▲
                        //            └──── Good luck finding this typo in a sea in a sea of
                        //                  similar rules
        Equals rule2 = new Equals("i-am-not-valid", "BE");
                        //             ▲
                        //             └──── We can supply any garbage we want here
    }
}
