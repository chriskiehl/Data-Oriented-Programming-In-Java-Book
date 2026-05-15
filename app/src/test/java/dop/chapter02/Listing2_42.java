package dop.chapter02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class Listing2_42 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.42
     * ───────────────────────────────────────────────────────
     * Creating a piece of “data” that consists entirely of nulls
     * ───────────────────────────────────────────────────────
     */
    void example() {
        Person person = new Person(null, null);   // ◄── A garbage piece of data that consists entirely of nulls
    }





    record Person(
        String name,
        Listing2_40_and_2_41.ImmutableList<String> friends
    ){}
}
