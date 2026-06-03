package dop.chapter02;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Listing2_36 {
  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.36
   * ───────────────────────────────────────────────────────
   * Poisoning records with mutable references
   * ───────────────────────────────────────────────────────
   */
  @Test
  void example() {
    List<String> friends = Arrays.asList("Joe", "Jane");  // ◄── Whoops! Accidentally called the wrong list constructor
    Person person = new Person("Bob", friends);           // ◄── And now a mutable list is hidden inside our “value”

    System.out.println(person);
    // [out]  Person2[name=Bob, friends=[Joe, Jane]]

    // ┌───── Anyone that holds a reference can reach inside the “value” and change its state
    // ▼
    friends.set(0, "Billy");
    System.out.println(person);
    // [out] Person2[name=Bob, friends=[Billy, Jane]]     // ◄── This should be impossible!
  }


  record Person(
    String name,
    List<String> friends
  ) {}

}
