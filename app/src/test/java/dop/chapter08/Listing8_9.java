package dop.chapter08;

import org.junit.jupiter.api.Test;

public class Listing8_9 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.9
     * ───────────────────────────────────────────────────────
     * In this book, we've used data to model all kinds of things.
     * Domain information (`NonNegativeInt`, `Degree`), things we "know"
     * like an invoice being `PastDue`. Now we're going to use data
     * to model a computation we want to evaluate *later*.
     */
    @Test
    void example() {
        // We can model actions like this
        // ```
        // account.country().equals("US")
        // ```
        // with a simple data type.
        record Equals(String field, String value) {}

        Equals rule1 = new Equals("country", "US");
        Equals rule2 = new Equals("country", "BE");
        Equals rule3 = new Equals("country", "FR");
        //                   ▲
        //                   └── These all represent an equality check
        //                       that we want to evaluate later
    }
}
