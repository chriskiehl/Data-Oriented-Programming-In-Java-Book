package dop.chapter02;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Listing2_19_and_2_20 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 2.19 & 2.20
   * ───────────────────────────────────────────────────────
   * Mutability where it doesn't belong is dangerous. It breaks
   * the semantics of our code. The ground shakes under our feet
   * when our core assumptions get violated.
   *
   * What's crummy is that the poison runs deep. Any mutable
   * reference *anywhere* ruins the value-ness of the entire
   * structure.
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    // This is just getting our "value" class into the
    // shape we need. (Don't worry, we'll soon explore records
    // which let us define value classes without all the
    // ceremony.
    final class Person {
      final String name;
      final Date birthday;
      public Person(String name, Date birthday) {
        this.name = name;
        this.birthday = birthday; // Like listing 2.18, we create value class that
                                  // has an accidental "back door" for mutability
      }

      public String name() { return name; }
      public Date birthday() { return birthday; }

      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(birthday, person.birthday);
      }

      @Override
      public int hashCode() {
        return Objects.hash(name, birthday);
      }
    }

    Person person = new Person("Bob", new Date());
    Set<Person> people = new HashSet<>(); // ◄─┐ Mutability can break core invariants in our code.
    people.add(person);                   //   │ What should be inviolable, like the fact that sets
    people.add(person);                   //   │ hold no duplicates, can be subtly violated.
    Assertions.assertEquals(1, people.size());

    // The set behaves like a set right up until something
    // on our "value" gets mutated.
    person.birthday().setTime(new Date().getTime() + 123456789);
    people.add(person);
    // The mutation breaks the HashSet contract, and we end up with two
    // copies of the exact same object
    Assertions.assertEquals(2, people.size());

    // │ Note: this is the example from listing 2.20.
    // │ It's combined with Listing 2.19 so that we don't
    // │ have to define the Person class again.

    // The cruel part of mutable references is that even if we
    // do everything else "right" (we use value-based
    // constructors filled with "value" objects, and mark everything
    // final) a single mutable reference can still betray all of
    // that work.
    final List<List<Person>> peoplelist = List.of(List.of(person));
    int original = peoplelist.hashCode();
    // Some random piece of code can always reach in and modify things
    // thus wreaking havoc on our expectations.
    peoplelist.get(0).get(0).birthday().setTime(12345678);
    System.out.println(original == people.hashCode());  // FALSE
  }

}
