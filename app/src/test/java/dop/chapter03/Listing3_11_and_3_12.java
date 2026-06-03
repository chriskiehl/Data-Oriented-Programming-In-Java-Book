package dop.chapter03;

import java.util.UUID;

public class Listing3_11_and_3_12 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 3.11
   * ───────────────────────────────────────────────────────
   * Adding extensive documentation to our record
   * ───────────────────────────────────────────────────────
   */
  /**
   * An individual observation tracking how water contact
   * angles on a surface changes as oil curing progresses by day
   * // ▲
   * // └───── a helpful high-level comment about what this record is modeling
   *
   * @param sampleId
   *     A pair (CuringMethod, positive int) represented
   *     as a String of the form "{curingMethod}-{number}"
   *     CuringMethod will be one of {AIR, UV, HEAT}
   * @param daysElapsed
   *     A positive integer (0..n)
   * @param contactAngleDegrees
   *    Water contact angle measured in degrees
   *    ranging from 0.0 (inclusive) to 360 (non-inclusive)
   *    // ▲
   *    // └───── Adding all of our domain information as param level documentation
   */
  record Measurement(
      String sampleId,
      int daysElapsed,
      double contactAngleDegrees
      //     ▲
      //     └───── Documenting the units directly in the variable name
  ) {}


  /**
   * ───────────────────────────────────────────────────────
   * Listings 3.10
   * ───────────────────────────────────────────────────────
   * breaking every single invariant at once
   * ───────────────────────────────────────────────────────
   */
  void example() {
    new Measurement(
        UUID.randomUUID().toString(),  //  ◄──┐ Despite those variable names and a bunch of
        -32,                           //     │ extensive doc strings, anybody can still march into
        9129.912                       //     │ our codebase and enter completely invalid data.
    );
  }

}
