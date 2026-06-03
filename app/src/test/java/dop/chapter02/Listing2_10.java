package dop.chapter02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Listing2_10 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.10
   * ───────────────────────────────────────────────────────
   * We can convert Java collections from identity objects
   * into value objects using the Unmodifiable suite of wrapper
   * types. Super useful!
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    // We start out with an identity object.
    List<String> letters = new ArrayList<>();
    letters.add("A");
    letters.add("B");  // we can freely mutate it as we go
    letters.add("...");
    letters.add("Z");
    // However here we convert it to a **Value Object** by wrapping it
    // up as an unmodifiableList.
    List<String> immutableLetters = Collections.unmodifiableList(letters);
    // Any attempts to continue treating it as an identity are
    // met with an error. The Unmodifiable wrapping classes turn off
    // every API method that might cause a mutation.
    Assertions.assertThrows(UnsupportedOperationException.class, () -> {
      immutableLetters.add("1"); // If you're in an IDE like IntelliJ
                                 // it should highlight this as an invalid
                                 // method call.
    });

    System.out.println(letters);
    letters.set(0, "HHHH");
    System.out.println(immutableLetters);
  }

}
