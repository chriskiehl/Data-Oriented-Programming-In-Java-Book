package dop.chapter04;

public class Listing4_40 {

  enum BeverageType {COFFEE, TEA}

  enum Temperature {HOT, COLD}

  record Beverage(BeverageType beverageType, Temperature temperature) {}

  static final BeverageType COFFEE = BeverageType.COFFEE;
  static final BeverageType TEA = BeverageType.TEA;
  static final Temperature HOT = Temperature.HOT;
  static final Temperature COLD = Temperature.COLD;

  /**
   * ───────────────────────────────────────────────────────
   * Listing 4.40
   * ───────────────────────────────────────────────────────
   * Every possible state we can create with our new type
   * ───────────────────────────────────────────────────────
   */
  void example() {
    new Beverage(COFFEE, HOT);     //  ◄──┐ The “state space” of our new type is the set
    new Beverage(TEA,   HOT);      //     │ product of the types from which it’s built.
    new Beverage(COFFEE, COLD);    //     │
    new Beverage(TEA,   COLD);     //     │
  }

}
