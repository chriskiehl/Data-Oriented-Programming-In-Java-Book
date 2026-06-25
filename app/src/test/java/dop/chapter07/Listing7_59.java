package dop.chapter07;

import java.util.Optional;
import java.util.function.BinaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing7_59 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.59
   * ───────────────────────────────────────────────────────
   * using lifted functions with Optional data
   * ───────────────────────────────────────────────────────
   */
  void example() {
    assertEquals(
        maybeAdd.apply(Optional.of(1), Optional.of(2)),
        Optional.of(3)
    );

    assertEquals(
        maybeConcat.apply(Optional.of("Hello"), Optional.of("World")),
        Optional.of("HelloWorld")
    );
    assertEquals(                                                    
        maybeConcat.apply(Optional.empty(), Optional.of("World")),
        Optional.empty()
    );
  }








  BinaryOperator<Optional<Integer>> maybeAdd = (a, b) -> Optional.empty();
  BinaryOperator<Optional<String>> maybeConcat = (a, b) -> Optional.empty();

}
