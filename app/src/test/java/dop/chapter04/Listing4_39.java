package dop.chapter04;

public class Listing4_39 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 4.39
     * ───────────────────────────────────────────────────────
     * Sealing without an explicit Permits
     * ───────────────────────────────────────────────────────
     */
    sealed interface ChecklistStatus {  // ◄── We don't list out the explicit permits clause
        record NotStarted() implements ChecklistStatus {};        //   ◄──┐ Because these are defined in the interface's body.
        record Completed(/*...*/) implements ChecklistStatus {}   //      │
        record Skipped(/*...*/) implements ChecklistStatus {}     //      │
    }
}
