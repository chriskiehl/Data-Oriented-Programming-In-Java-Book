package dop.chapter10;

public class Listing10_26 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.26
     * ───────────────────────────────────────────────────────
     * These are the data types we want to use
     * ───────────────────────────────────────────────────────
     */
    record ResourcePath(String value) {}
    record Manifest(
        ResourcePath accounts,  //  ┐
        ResourcePath invoices,  //  │◄── Our application uses descriptive data types
        ResourcePath disputes   //  ┘    to model the resource paths
    ){}
}
