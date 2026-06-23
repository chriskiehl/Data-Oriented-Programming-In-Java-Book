package dop.chapter12;

public class Listing12_07 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.7
     * ───────────────────────────────────────────────────────
     * The lifecycle modeled as a sealed data type
     * ───────────────────────────────────────────────────────
     */
    record TaskId(String value){}
    sealed interface Task {
        record Started(TaskId id /* other attrs */) implements Task {}   //  ┐
        record Completed(TaskId id /* other attrs */) implements Task {} //  │◄── The three states
        record Failed(TaskId id /* other attrs */) implements Task {}    //  ┘
    }
}

