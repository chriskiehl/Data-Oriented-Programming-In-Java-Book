package dop.chapter02;

import java.util.List;

public class Listing2_46 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.46
   * ───────────────────────────────────────────────────────
   * Core data types for a card game in 4 lines
   * ───────────────────────────────────────────────────────
   */
  interface CardGame {
    enum Suit {Hearts, Diamonds, Clubs, Spades;}
    enum Rank {ONE, TWO, THREE, FOUR, FIVE, /*...*/;}

    record Card(Rank rank, Suit suit){};

    record GameState(List<Card> drawPile, List<Card> discards){}
  }

}
