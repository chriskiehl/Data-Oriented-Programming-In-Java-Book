package dop.chapter11;

public class Listing11_36 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.36
     * ───────────────────────────────────────────────────────
     * Overloading randomized methods to lock down specific attributes
     * ───────────────────────────────────────────────────────
     */
    Account mkAcnt(Region region, USD spend) {
        // We start with completely randomized data
        Account account = mkAcnt();
        // And then lock down the values that are relevant to our test
        return new Account(account.id(), region, spend /*...*/);
    }

    Account mkAcnt(Segment segment) {
        Account account = mkAcnt();
        return new Account(account.id(), account.region(), /*...*/ segment /*...*/);
    }














    enum Region {AMER}
    enum Segment {ENTERPRISE}
    record AccountId(String value){}
    record Account(AccountId id, Region region, Object... rest){}
    static Account mkAcnt() {
        return new Account(new AccountId(""), Region.AMER, 0.0);
    }
}
