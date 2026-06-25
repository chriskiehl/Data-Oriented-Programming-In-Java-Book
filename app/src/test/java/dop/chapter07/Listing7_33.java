package dop.chapter07;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing7_33 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.33
   * ───────────────────────────────────────────────────────
   * “of course” all of these should give the same answer
   * ───────────────────────────────────────────────────────
   */
  void example() {
    RawData row1 = new RawData(/*...*/);
    RawData row2 = new RawData(/*...*/);
    RawData row3 = new RawData(/*...*/);
    assertEquals(
        add(row1, add(row2, row3)),  //  ┐
        add(add(row1, row2), row3)   //  ┘◄── It would be straight up weird
                                     //       if these led to different answers
    );
  }








  record RawData() {}

  static RawData add(RawData x, RawData y) {
    return x;
  }

}
