package dop.chapter08;

public class Listing8_51 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.51
   * ───────────────────────────────────────────────────────
   * Plain data that we “interpreted” later on
   * ───────────────────────────────────────────────────────
   */
  sealed interface RetryDecision {
    record RetryImmediately(/*???*/) implements RetryDecision {}  //  ┐
    record ReattemptLater(/*???*/) implements RetryDecision {}    //  │◄── Data for an interpreter!
    record Abandon(/*???*/) implements RetryDecision {}           //  ┘
  }

}
