package dop.chapter02;

import java.util.*;

import static java.lang.Integer.compare;
import static java.util.Comparator.comparingDouble;

public class Listing2_44_and_2_45 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.44
     * ───────────────────────────────────────────────────────
     * combining results with positional data structures
     * ───────────────────────────────────────────────────────
     */
    Optional<Person> mostPopular(List<Person> people) {
        return people.stream()
            .map(person -> new Tuple<>(
                    person,
                    totalFriends(person, people)))
            .sorted((a,b) -> compare(b.second(), a.second()))
            .map(Tuple::first)
            .findFirst();
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.45
     * ───────────────────────────────────────────────────────
     * Defining records locally in a method
     * ───────────────────────────────────────────────────────
     */
    Optional<Person> mostPopular2(List<Person> people) {
        record Popularity(Person person, int totalFriends){};
        //     ▲
        //     └───── Declaring a new record type right where we need it.

        return people.stream()
                .map(person -> new Popularity(
                    person,
                    totalFriends(person, people))
                )
                .sorted(comparingDouble(Popularity::totalFriends).reversed())
                //      ▲
                //      └───── Now we can use clear names where we previously would have used opaque tuples or lists
                .map(Popularity::person)
                .findFirst();
    }



















    int totalFriends(Person person, List<Person> people) {
        return new Random().nextInt(10);
    }

    record Person(String firstName, String lastName) {}
    record Tuple<A,B>(A first, B second){}
}
