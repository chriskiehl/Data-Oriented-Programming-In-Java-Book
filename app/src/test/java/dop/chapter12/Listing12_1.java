package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing12_1 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.1
     * ───────────────────────────────────────────────────────
     * A basic integration test
     * ───────────────────────────────────────────────────────
     */
    @Test
    void weCanReadAndWriteFromTheDB() {
        //  Integration tests can start as humble "units" of functionality,
        // like sanity checking you can write to and read from your database.
        MyRepository repo = MyDependecyInjector.myRepository();
        // We make a bit of data
        Person person = new Person(UUID.randomUUID(), "Bob");
        // Write it to the database
        repo.save(person);
        assertEquals(               //  ┐
            person,                 //  │◄── And finally check that we can read it back out again.
            repo.load(person.id())  //  │    Nothing fancy, but it's useful in the early days of a project.
        );                          //  ┘
    }































    record Person(UUID id, String name){}
    interface MyRepository {
        void save(Person person);
        Person load(UUID id);
    }
    static class MyDependecyInjector {
        static MyRepository myRepository() {
            return null;
        }
    }
}

