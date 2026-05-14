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
public class Listing4_14_to_4_16 {



    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.14 through 4.16
     * ───────────────────────────────────────────────────────
     * A new requirement appears!
     * We want to be able to track who performed an individual step
     * in the checklist. This should be an easy addition given our
     * modeling, right...?
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // Step, Template, and Instance were defined in previous listings
        record Step(String name) {}
        record Template(String name, List<Step> steps) {}
        record Instance(String name, Instant date, Template template){}

        // Minimally viable user type.
        // It's not that interesting to our example, so we
        // keep it pretty bare bones.
        record User(String value){}

        record Status(
                Template template,
                Step step,
                boolean isCompleted,
                User completedBy,     // ◄────┐
                Instant completedOn   //      │ Just plug these in here...?
        ){}

        // Here's where we take another step back
        // and perform our gut check: does this modeling
        // let us express anything weird?
        // Let's try creating a new Status that hasn't been
        // completed yet.

        /**
         * (Note: commented out because the example would not compile)
         *
         * new Status(
         *     template,
         *     step,
         *     false,
         *     ???  ◄──┐
         *     ???  ◄─── Uhh... what goes here? We don't *have* a user yet
         *               because nobody has completed the step yet!
         * );
         */

        // Another dangerous "Java" thought might pop into your head that begins
        // with "well we could just..." and ends with "use nulls"
        //
        // We've got to push those thoughts down! Our *modeling* is incorrect.
        // Patching it over with nulls only introduces more problems. Check this out:
        Template template = new Template("Cool Template", List.of(new Step("Step 1")));

        // We can break causality itself!
        new Status(
                template,
                template.steps().getFirst(),
                false,             // ◄───┐ Our code allows us to say something nonsensical.
                new User("Bob"),   //     ┘ We've added "who did it" before "it" was done!
                Instant.now()
        );

        // The inverse is true as well.
        new Status(
                template,
                template.steps().getFirst(),
                true,  // ◄──── Now our step is marked as complete
                null,  // ◄───┐ But who did it (and when!) is completely
                null   //     ┘ missing.
        );

        // The design of our data model **created** these invalid states.
    }
}

