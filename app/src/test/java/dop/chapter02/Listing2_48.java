package dop.chapter02;
import org.immutables.value.Value;

public class Listing2_48 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.48
   * ───────────────────────────────────────────────────────
   * Immutables annotated class
   * ───────────────────────────────────────────────────────
   */
  @Value.Immutable
  interface Person {
    //         ▲
    //         └───── The two frameworks have very different approaches. Immutables’ annotations are attached to interfaces or abstract classes
    String name();
    Integer age();
  }

  void example() {
    Person person = ImmutablePerson
        //            ▲
        //            └───── And unlike Lombok, it generates a totally new class. We use that class when instantiating the data types
        .builder()
        // ▲
        // └───── The other big departure is that Immutables doesn’t generate a public constructor by default. It generates a builder.
        .name("Bob")
        .age(32)
        .build();
  }

}
