package dop.chapter02;
import lombok.Value;

public class Listing2_47 {
  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.47
   * ───────────────────────────────────────────────────────
   * Lombok annotated class
   * ───────────────────────────────────────────────────────
   */

  @Value
  // ▲
  // └───── The value annotation creates a constructor, marks our fields final, generates getters. The whole thing.
  public class Person {
    String name;
    Integer age;
    // ▲
    // └───── Similar to records, this body is blank because the annotation processor handles everything
  }

  void example() {
    Person person1 = new Person("Bob", 32);
    Person person2 = new Person("Bob", 32);
    //     ▲
    //     └───── The constructor shows up for free.

    person1.equals(person2);  // ◄── Value annotated classes are proper value classes. They behave like values.
  }

}
