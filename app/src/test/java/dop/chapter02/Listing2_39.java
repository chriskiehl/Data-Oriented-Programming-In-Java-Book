package dop.chapter02;

import java.util.Arrays;
import java.util.List;

public class Listing2_39 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.39
     * ───────────────────────────────────────────────────────
     * An easy mistake, because the code is lying to us about what it allows
     * ───────────────────────────────────────────────────────
     */
    void example() {
        List<String> friends = Arrays.asList("Joe", "Jane");
        Person person = new Person("Bob", friends);

        person.friends().add("Billy");  // ERROR!
        //               ▲
        //               └───── As far as what the code tells us, it looks like this should totally work, but anyone who tries will end up with an unexpected runtime error
    }





    record Person(String name, List<String> friends){
        Person {
            friends = List.copyOf(friends);
        }
    }
}
