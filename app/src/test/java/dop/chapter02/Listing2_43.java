package dop.chapter02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class Listing2_43 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.43
     * ───────────────────────────────────────────────────────
     * defending against nulls in the constructor
     * ───────────────────────────────────────────────────────
     */
    record Person(String firstName, String lastName) {
        Person {
            Objects.requireNonNull(firstName);      //   ◄──┐ Using the compact constructor form make sure
            Objects.requireNonNull(lastName);       //      │ nothing is null before allowing creation
            //  ▲
            //  └── This ensures that we explode with a stack
            //      trace rather than silently propagating nulls
            //      through our system (potentially causing much more harm)
        }
    }
    @Test
    void example() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Person person = new Person(null, null); // ERROR
        });
    }
}
