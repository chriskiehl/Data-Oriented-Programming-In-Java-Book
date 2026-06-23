package dop.chapter06;

import java.util.Optional;

public class Listing6_47 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.47
   * ───────────────────────────────────────────────────────
   * Comparing imperative and functional interactions
   * ───────────────────────────────────────────────────────
   */
  String imperativeExample(String thingId) {
    Optional<String> maybeThing = this.tryToFindThing(thingId);

                        // ┌─────The imperative way involves manually checking the state of the Optional
                        // ▼
    return maybeThing.isPresent()
        ? maybeThing.get().toUpperCase()
//                     ▲
//                     └──── as well as manually grabbing its contents
        : "Default thing!";
  }

  String functionalExample(String thingId) {
    // The functional approach stays above the details
    return this.tryToFindThing(thingId)
        .map(String::toUpperCase)
        .orElse("Default thing!");
  }








  Optional<String> tryToFindThing(String thingId) { return Optional.empty(); }

}
