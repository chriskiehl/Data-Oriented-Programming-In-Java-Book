package dop.chapter06;

import java.util.List;
import java.util.Optional;

public class Listing6_03 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.3
   * ───────────────────────────────────────────────────────
   * The same input might lead to a different output
   * ───────────────────────────────────────────────────────
   */
  class Person {
    PersonId id;
    private List<Person> friends;
    // constructor, getters, setters, etc.

    public boolean isFriendsWith(Person p) {  //  ┐
      return this.friends.contains(p);        //  │◄── The same input can yield different outputs
    }                                         //  ┘    depending on the state of the system
  }
  class PersonRepository {
    private Database db;
    // construtor, etc…
    public Optional<Person> getPerson(PersonId id) {
      return db.findById(id);   //  ◄───── Not deterministic!
    }
  }

  record PersonId(String value) {}

  interface Database {
    Optional<Person> findById(PersonId id);
  }

}
