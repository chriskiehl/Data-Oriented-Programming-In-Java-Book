package dop.chapter02;

import java.util.Arrays;
import java.util.List;

public class Listing2_38 {
  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.38
   * ───────────────────────────────────────────────────────
   * Anti-poison technology
   * ───────────────────────────────────────────────────────
   */
  void example() {
    List<String> friends = Arrays.asList("Joe", "Jane");
    Person person = new Person("Bob", friends);        // ◄── Inside our constructor, we’ve defended against this common mistake

    friends.set(0, "Billy");                           //   ◄──┐ Out here, they can freely mutate their collection
    friends.set(1, "Michael");                         //      │ as much as they want

    System.out.println(person);                        //   ◄──┐ But we remain an unchanging, immutable value.
    // [out] Person2[name=Bob, friends=[Joe, Jane]]    //      │
  }





  record Person(String name, List<String> friends){
    Person {
      friends = List.copyOf(friends);
    }
  }

}
