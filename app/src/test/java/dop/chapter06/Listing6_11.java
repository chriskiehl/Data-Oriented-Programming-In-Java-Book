package dop.chapter06;

public class Listing6_11 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.11
   * ───────────────────────────────────────────────────────
   * Using static to denote functions
   * ───────────────────────────────────────────────────────
   */
  class Example1 {
    public int increment(int x) {
      return x + 1;
    }
  }

  class Example2 {
    //       ┌─── The static modifier helps communicate our intentions
    //       ▼
    public static int increment(int x) {
      return x + 1;
    }
  }

}
