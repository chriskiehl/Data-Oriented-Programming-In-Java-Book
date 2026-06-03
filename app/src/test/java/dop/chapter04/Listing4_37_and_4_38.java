package dop.chapter04;

public class Listing4_37_and_4_38 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 4.37
   * ───────────────────────────────────────────────────────
   * Sealing the ChecklistState type
   * ───────────────────────────────────────────────────────
   */
  sealed interface ChecklistStatus  // ◄── Adding the “sealed” modifier to our interface definition
      permits NotStarted, Completed, Skipped    // ◄── And then explicitly specifying what’s allowed to implement it
  {}

  record NotStarted(/*...*/) implements ChecklistStatus {};  //  ◄──┐ These are assumed to be in their own files
  record Completed(/*...*/) implements ChecklistStatus {};   //     │
  record Skipped(/*...*/) implements ChecklistStatus {};     //     │

  /**
   * ───────────────────────────────────────────────────────
   * Listing 4.38
   * ───────────────────────────────────────────────────────
   * Trying to extend the interface
   * ───────────────────────────────────────────────────────
   */
  // record Blocked() implements ChecklistStatus {}  // ERROR
  //            ▲
  //            └───── Nope! Java will give a compiler error that Blocked is not part of the sealed hierarchy

}
