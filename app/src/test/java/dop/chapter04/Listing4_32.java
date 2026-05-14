package dop.chapter04;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Chapter 4 builds on top of chapter 3's exploration of
 * learning to see through our code into what it actually
 * means and communicates. This is a fun one, because it
 * walks through the design process in all of its messy
 * glory. We'll make mistakes, refine, refactor -- and even
 * go back to the drawing board a few time.
 *
 * What I hope to show is that focusing on the data and getting its
 * representation pinned down *before* we start worrying about
 * its behaviors makes any mistakes low cost and fast to correct.
 * This approach enables rapid prototyping and immediate feedback.
 * We can learn from our mistakes before we start pouring concrete
 * in the form of implementation code.
 */
public class Listing4_32 {



    // records cannot extend classes, so we tie them
    // together with an interface.
    interface StepState { }

    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.32 through 4.34
     * ───────────────────────────────────────────────────────
     * From AND, AND, AND to OR, OR, OR
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // All defined in previous listings
        record Step(String name) {}
        record Template(String name, List<Step> steps) {}
        record Instance(String name, Instant date, Template template){}
        record User(String value){}



        record NotStarted(
                Template template,
                Step step
        ) implements StepState {}  // Each record type implements this interface

        record Completed(
                Template template,
                Step step,
                User completedBy,
                Instant completedOn
        ) implements StepState {}  // Here, too

        record Skipped(
                Template template,
                Step step,
                User skippedBy,
                Instant skippedOn,
                String rationale
        ) implements StepState {}  // and here.


        // Now we can use this interface to unite the disparate types.
        // All of them belong to / are "about" this idea of Step Statuses.
        Template template = new Template("Howdy", List.of(
                new Step("1"),
                new Step("2")
        ));
        Step step = template.steps().getFirst();

        //    ┌─ We can assign the Completed data type to StepStatus
        //    │  because the interface unites them under the same "family"
        //    │
        //    ▼                                       │
        StepState completed = new Completed( //  ◄───┘
                template,
                step,
                new User("Bob"),
                Instant.now()
        );

        // This modeling lets us express *choice* within Java.
        // A StepStatus is either NotStarted, Completed, or Skipped.
    }
}

