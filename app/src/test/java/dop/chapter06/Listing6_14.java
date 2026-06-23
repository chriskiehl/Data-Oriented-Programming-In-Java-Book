package dop.chapter06;

public class Listing6_14 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.14
   * ───────────────────────────────────────────────────────
   * How many ways could we write an implementation for this?
   * ───────────────────────────────────────────────────────
   */
  enum People {Bob, Mary}
  enum Jobs {Cook, Engineer}

  class SomeClass {
    // [Hidden]

    People someMethod(Job job) {
      // [Hidden]
      //    ▲
      //    └──── What can we guess about what goes on inside here?


      return null; // (to compile)
    }
  }








  interface Job {}

}
