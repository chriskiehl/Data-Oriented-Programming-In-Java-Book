package dop.chapter11;

import org.junit.jupiter.api.Test;

public class Listing11_35 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.35
     * ───────────────────────────────────────────────────────
     * It’s no longer possible to couple yourself to hidden values
     * ───────────────────────────────────────────────────────
     */
    @Test
    void myTestThatNeedsEnterpriseAccounts() {
        Account nope = mkAcnt();
//              ▲
//              └──── The original version secretly relied on a hard coded
//                    Enterprise value inside mkAcnt. That style of
//                    coupling is no longer possible. There are no hard
//                    coded values!
    }














    record Account(String id){}
    static Account mkAcnt() {
        return new Account("");
    }
}
