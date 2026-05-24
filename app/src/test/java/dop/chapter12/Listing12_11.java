package dop.chapter12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class Listing12_11 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.11
     * ───────────────────────────────────────────────────────
     * Verifying the database behaves the way we need it to
     * ───────────────────────────────────────────────────────
     */
    @Test
    void onlyValidStateTransitionsAllowed() {
        List<Optional<Job>> states = List.of(
            Optional.empty(),
            Optional.of(new Started(new TaskId("1"))),
            Optional.of(new Failed(new TaskId("1"))),
            Optional.of(new Completed(new TaskId("1")))
        );

        MyRepository repo = new MyRepository(db);
        for (var a : states) {
            for (var b : states) {
                if (order(a) < order(b)) {                //  ┐
                    Assertions.assertDoesNotThrow(() -> { //  │◄── If state A is "less than"
                        a.ifPresent(repo::save);          //  │    (can transition to) state B,
                        b.ifPresent(repo::save);          //  │    then the database should allow it
                    });                                   //  ┘
                } else {
                    Assertions.assertThrows(ConditionFailedException.class, () -> {
//                      ^ All invalid transitions should be caught by our Condition Expression
                        a.ifPresent(repo::save);
                        b.ifPresent(repo::save);
                    });
                }
            }
        }
    }















    Object db;
    int order(Optional<Job> job) { return 0; }
    record TaskId(String value){}
    interface Job {}
    record Started(TaskId id) implements Job {}
    record Failed(TaskId id) implements Job {}
    record Completed(TaskId id) implements Job {}
    static class MyRepository {
        MyRepository(Object db) {}
        void save(Job job) {}
    }
    static class ConditionFailedException extends RuntimeException {}
}

