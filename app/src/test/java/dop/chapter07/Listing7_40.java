package dop.chapter07;

public class Listing7_40 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.40
   * ───────────────────────────────────────────────────────
   * Further refining what correct means in our domain
   * ───────────────────────────────────────────────────────
   */
  /*
  ∀a,b,c,d ∈ ConflictingRows

  Properties:
     Associativity: (a + (b + c)) = ((a + b) + c)
     Ordered:
       Reflexivity: a ≤ a                           //  ┐
       Antisymmetry: IF a ≤ b AND b ≤ a THEN a = b  //  │◄── Writing down what we know about
       Transitivity: IF a ≤ b AND b ≤ c THEN a ≤ c  //  ┘    our data’s ordering
  */

}
