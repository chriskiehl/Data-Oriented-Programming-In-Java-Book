package dop.chapter07;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing7_36 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.36
   * ───────────────────────────────────────────────────────
   * Verifying associativity with an exhaustive unit test
   * ───────────────────────────────────────────────────────
   */
  void testAssociativity() {
    for (RawData a : allPossibleRows()) {      //  ┐
      for (RawData b : allPossibleRows()) {    //  │◄── Translates to “∀a,b,c ∈ RawData”
        for (RawData c : allPossibleRows()) {  //  ┘
          assertEquals(                        //  ┐
            add(a, add(b, c)),                 //  │◄── “The associativity laws must hold”
            add(add(a, b), c));                //  ┘
        }
      }
    }
  }








  record RawData(String id) {}

  static RawData[] allPossibleRows() {
    return new RawData[0];
  }

  static RawData add(RawData x, RawData y) {
    return x;
  }

}
