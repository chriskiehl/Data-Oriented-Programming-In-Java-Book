package dop.chapter08;

public class Listing8_10 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 8.10
     * ───────────────────────────────────────────────────────
     * Expressing rules as plain data
     * ───────────────────────────────────────────────────────
     */
    void example() {
        Equals rule1 = new Equals("country", "US");  //  ┐
        Equals rule2 = new Equals("country", "BE");  //  │◄── All of these describe an equality check
        Equals rule3 = new Equals("country", "FR");  //  ┘    that we want to perform later
    }















    record Equals(String attributeName, String value) {}
}
