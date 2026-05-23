package dop.chapter10;

public class Listing10_18 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.18
     * ───────────────────────────────────────────────────────
     * something like…
     * ───────────────────────────────────────────────────────
     */
    record ResourcePath(String value) {}
    //         ▲
    //         └──── A small record lets us differentiate what these strings mean
    //               from all other strings in the system
    record Manifest(
        ResourcePath accounts,
        ResourcePath invoices,
        ResourcePath disputes
    ){}
}
