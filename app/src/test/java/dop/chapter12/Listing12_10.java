package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class Listing12_10 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.10
     * ───────────────────────────────────────────────────────
     * Verifying our transitions
     * ───────────────────────────────────────────────────────
     */
    @Test
    void encodingStateTransitionsViaOrdering() {
        List<Optional<Task>> states = List.of(
            Optional.empty(),                              //  ┐
            Optional.of(new Started(new TaskId("1"))),     //  │◄── All of the states a task could be in,
            Optional.of(new Failed(new TaskId("1"))),      //  │    including the "empty" state before it exists
            Optional.of(new Completed(new TaskId("1")))    //  ┘
        );

        for (var a : states) {                             //  ┐
            for (var b : states) {                         //  ┘◄── Generating the cartesian product
                                                           //       (all combinations) of the possible states
                System.out.println(format("%-10s can transition to %-10s %s",
                    a,
                    b,
                    order(a) < order(b))
                );
            }
        }
    }

    /*
    [out]
    (Empty)    can transition to (Empty)    false
    (Empty)    can transition to Started    true   // Rule 1
    (Empty)    can transition to Failed     true   // Rule 1
    (Empty)    can transition to Completed  true   // Rule 1
    Started    can transition to (Empty)    false
    Started    can transition to Started    false  // Rule 2
    Started    can transition to Failed     true   // Rule 2
    Started    can transition to Completed  true   // Rule 2
    Failed     can transition to (Empty)    false  // Rule 3
    Failed     can transition to Started    false  // Rule 3
    Failed     can transition to Failed     false  // Rule 2
    Failed     can transition to Completed  false  // Rule 3
    Completed  can transition to (Empty)    false  // Rule 3
    Completed  can transition to Started    false  // Rule 3
    Completed  can transition to Failed     false  // Rule 3
    Completed  can transition to Completed  false  // Rule 2
    */















    int order(Task task) {
        return switch (task) {
            case Started __   -> 0;
            case Completed __ -> 1;
            case Failed __    -> 1;
        };
    }

    int order(Optional<Task> task) {
        return task.map(this::order).orElse(-1);
    }

    record TaskId(String value){}
    sealed interface Task permits Started, Completed, Failed {}
    record Started(TaskId id) implements Task {}
    record Completed(TaskId id) implements Task {}
    record Failed(TaskId id) implements Task {}
}

