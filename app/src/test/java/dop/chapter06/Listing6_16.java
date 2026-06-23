package dop.chapter06;

public class Listing6_16 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.16
   * ───────────────────────────────────────────────────────
   * using static to signal that this should be a deterministic function
   * ───────────────────────────────────────────────────────
   */
  enum People {Bob, Mary}
  enum Jobs {Cook, Engineer}
//
//    ┌──── We’re using static to signal that this method will behave
//    │     as a deterministic function
//    ▼
  static People someMethod(Jobs job) {
    // [Hidden]
    return null; // (so example compiles)
}

}
