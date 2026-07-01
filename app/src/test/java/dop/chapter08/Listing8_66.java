package dop.chapter08;

public class Listing8_66 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.66
   * ───────────────────────────────────────────────────────
   * Writing rules that compare data
   * ───────────────────────────────────────────────────────
   */
  /*
  eq(Region, EMEA).and(gt(USD.of(10_000_000.0)))

  Result[matched=false,                                 //  ┐
      expected=(Region=EMEA AND Sales>=10,000,000.0),   //  │◄── Pretty cool, right?
      found=( Region=EMEA AND Sales=500,000.00)]        //  ┘
  */

}
