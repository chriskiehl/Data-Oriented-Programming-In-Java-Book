package dop.chapter07;

import java.util.Optional;

public class Listing7_51 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.51
   * ───────────────────────────────────────────────────────
   * Optionals as arguments?!
   * ───────────────────────────────────────────────────────
   */
  static <A> Optional<A> add(
      Optional<A> option1,   //  ┐
      Optional<A> option2    //  ┘◄── My IDE scolds me with "Optional is primarily
  ) {                    //       intended for use as a method return type"
    // ...
    return null; //(so it compiles) 
  }

}
