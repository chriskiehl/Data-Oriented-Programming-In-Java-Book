package dop.chapter07;

public class Listing7_48 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.48
     * ───────────────────────────────────────────────────────
     * A formal specification of our requirements
     * ───────────────────────────────────────────────────────
     */
    /*
    ∀a,b,c,d ∈ ConflictingRows
    Properties:
       a + b = "a more favorable record" (whatever that means)  // ◄── Now we know what it means!
       Monotonicity: a ≤ c AND b ≤ d THEN a + b ≤ c + d         // ◄── For an implementation to be
                                                                //     correct, order must be preserved
                                                                //     during merging
       Associativity: (a + (b + c)) = ((a + b) + c)
       Ordered:
         Reflexivity: a ≤ a
         Antisymmetry: IF a ≤ b AND b ≤ a THEN a = b
         Transitivity: IF a ≤ b AND b ≤ c THEN a ≤ c
    */
}
