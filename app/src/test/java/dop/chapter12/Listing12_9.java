package dop.chapter12;

import dop.chapter12.Listing12_9.Task.Completed;
import dop.chapter12.Listing12_9.Task.Failed;
import dop.chapter12.Listing12_9.Task.Started;

import java.util.Optional;

public class Listing12_9 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.9
     * ───────────────────────────────────────────────────────
     * Encoding the ordering
     * ───────────────────────────────────────────────────────
     */
    int order(Task task) {
        // Encoding the ordering
        return switch (task) {
            case Started __   -> 0;
            case Completed __ -> 1;   //  ┐
            case Failed __    -> 1;   //  ┘
        };
    }

    int order(Optional<Task> task) {
        // Encoding the ordering for the empty state
        // Note that we're breaking the "rule" of not taking
        // Optionals as arguments! Sometimes, doing the "wrong"
        // thing is the best way to clarify our code. 
        return task.map(this::order).orElse(-1);
    }
    

























    record TaskId(String value){}
    sealed interface Task {
        record Started(TaskId id) implements Task {}
        record Completed(TaskId id) implements Task {}
        record Failed(TaskId id) implements Task {}
    }

}

