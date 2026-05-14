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
public class Listing4_17_to_4_18 {



    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.17 through 4.18
     * ───────────────────────────────────────────────────────
     * Defensive coding to the rescue?
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // All defined in previous listings
        record Step(String name) {}
        record Template(String name, List<Step> steps) {}
        record Instance(String name, Instant date, Template template){}
        record User(String value){}

        record Status(
                Template template,
                Step step,
                boolean isCompleted,
                User completedBy,
                Instant completedOn
        ){
            Status {
                // this is tedious to write, but it gets the job done.
                // We're kind of back on track. We've "prevented" invalid
                // data from being created.
                if (isCompleted && (completedBy == null || completedOn == null)) {
                    throw new IllegalArgumentException(
                        "completedBy and completedOn cannot be null " +
                        "when isCompleted is true"
                    );
                }
                if (!isCompleted && (completedBy != null || completedOn != null )) {
                    throw new IllegalArgumentException(
                        "completedBy and completedOn cannot be populated " +
                        "when isCompleted is false"
                    );
                }
            }
        }

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Template template = new Template("Cool Template", List.of(new Step("Step 1")));
            new Status(
                template,
                template.steps().getFirst(),
                false,
                null,  // Now any attempts at creating invalid states will be rejected
                Instant.now()
            );
        });
    }
}

