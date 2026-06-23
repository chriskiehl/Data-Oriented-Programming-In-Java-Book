package dop.chapter06;

public class Listing6_17 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.17
   * ───────────────────────────────────────────────────────
   * Every possible implementation allowed by our types.
   * ───────────────────────────────────────────────────────
   */
  enum People {Bob, Mary};
  enum Jobs {Chef, Engineer};

  // Implementation possibility #1
  static People someMethod1(Jobs job) {
    return switch (job) {
      case Chef -> People.Bob;
      case Engineer -> People.Bob;
    };
  }
  // OR Implementation possibility #2
  static People someMethod2(Jobs job) {
    return switch (job) {
      case Chef -> People.Mary;
      case Engineer -> People.Mary;
    };
  }
  // OR Implementation possibility #3
  static People someMethod3(Jobs job) {
    return switch (job) {
      case Chef -> People.Bob;
      case Engineer -> People.Mary;
    };
  }
  // OR Implementation possibility #4
  static People someMethod4(Jobs job) {
    return switch(job) {
      case Chef -> People.Mary;
      case Engineer -> People.Bob;
    };
    // That's it!
    //
    // No other possible implementations! (we’re ignoring cases
    // where the input is ignored and only a fixed value is returned)
  }

}
