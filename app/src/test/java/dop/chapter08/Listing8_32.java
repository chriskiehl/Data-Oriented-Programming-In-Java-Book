package dop.chapter08;

public class Listing8_32 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.32
   * ───────────────────────────────────────────────────────
   * A Boolean that tracks why it’s in a certain state
   * ───────────────────────────────────────────────────────
   */
  record Result(
      boolean matched,  // ◄── Our core data type is still a boolean underneath the covers
      String expected,  //  ┐
      String found      //  ┘◄── But we upgrade it to carry around why it’s in its current state
  ) {}

}
