package dop.chapter05;

public class Listing5_12 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.12
   * ───────────────────────────────────────────────────────
   * Runtime if/else as Compile time data
   * ───────────────────────────────────────────────────────
   */
  // void example() {                     //   ◄──┐ Decisions we make at runtime using if/else
  //   if (/* option #1 */) {...}         //      │
  //   else if(/* option #2 */) {...}     //      │
  //   else {...}
  // }

  sealed interface Decision {                   //  ◄──┐ can be represented at compile time using
    record Option1() implements Decision {}     //     │ sealed (sum) types
    record Option2() implements Decision {}     //     │
    record Option3() implements Decision {}     //     │
  }

}
