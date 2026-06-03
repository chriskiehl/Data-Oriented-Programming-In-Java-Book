package dop.chapter02;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class Listing2_18 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 2.18
   * ───────────────────────────────────────────────────────
   * Mutability is infectious. All it takes is a single mutable
   * reference on our value class to drag it back into the land
   * of identities.
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {

    final class Person {
      final String name;
      final Date birthday;  // ◄─┐ Mutability can be sneaky in Java.
                            //   │ The assignment to this variable is final, but
                            //   │ the object itself is mutable
      public Person(String name, Date birthday) {
        this.name = name;
        this.birthday = birthday;
      }

      public String name() { return name; }
      public Date birthday() { return birthday; }
    }

    final Person person = new Person("Bob", new Date());  // ◄── Despite our efforts, this doesn’t create a value object.
    System.out.println(person);
    // [out] Person[name=Bob, birthday=Sun Aug 11 22:42:57 PDT 2024]  // ◄── If this was a value, then the date should never change

    person.birthday().setTime(                             //  ◄──┐ But mutable references undermine our value semantics.
        new Date().getTime() + TimeUnit.DAYS.toMillis(1)   //     │ Outside code can modify our state.
    );                                                     //     │
    System.out.println(person);
  }

}
