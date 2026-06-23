package dop.chapter02;

public class Listing2_01 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.1
   * ───────────────────────────────────────────────────────
   * A relationship between state and time
   * ───────────────────────────────────────────────────────
   */
  public static void main(String... args) { //  |
    double xPosition = 4.2;                 //  |
                                            //  |
    xPosition++;                            //  |(time)
    xPosition++;                            //  |
    xPosition *= xPosition;                 //  ▼
  }

}
