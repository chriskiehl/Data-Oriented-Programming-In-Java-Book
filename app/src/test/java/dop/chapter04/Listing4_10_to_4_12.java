package dop.chapter04;


import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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
public class Listing4_10_to_4_12 {



    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.10 through 4.13
     * ───────────────────────────────────────────────────────
     * We still don't know where we should track the idea of
     * completing a step, but that's not a problem. We can keep
     * exploring the modeling of the data. Maybe were it goes will
     * naturally emerge as we fill out the rest of the domain model.
     *
     * This big point I'll keep belaboring: we're designing. This is
     * a loose, iterative, and exploratory process! Don't think of it
     * as "writing code" just yet. We're using code as a tool for
     * sketching. We'll constantly be taking a step back and looking
     * at what our choices mean and then revising from there.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // Step and Template were defined in previous listings
        record Step(String name) {}
        record Template(String name, List<Step> steps) {}
        // Here's a sketch of how we might model instances of
        // a checklist.
        record Instance(
            String name,
            Instant date, // Instance nicely captures the "Point in time"
                          // detail from our requirements (Listing 4.5)
            Template template
            // Putting the whole template on the Instance is one of the nearly
            // infinite ways we could approach modeling this data in our code.
            // It's the one we'll stick with for the sake of the book and the
            // flow of its examples. We use it as part of the "natural key" (to
            // use some database lingo) which uniquely identifies an Instance.
            // Alternative modelings are left as an exercise to the reader ^_^
        ){}

        /**
         * ───────────────────────────────────────────────────────
         * Listings 4.12
         * ───────────────────────────────────────────────────────
         * Adding template to our Status model
         * ───────────────────────────────────────────────────────
         */
        // Now -- the big remaining design question is where we put that core idea
        // that steps in our checklist get completed.
        // Rather than trying to fit it into any one of our existing models, we might
        // introduce a *new* data type that specifically exists to track whether a step is completed.
        record Status(Template template, Step step, boolean isCompleted){}
        //               ▲                      ▲                ▲
        //               │                      └────────────────┘
        //               │                           └─── Tracks the step and its completed status
        //               │
        //               │
        //               │
        //               └ Putting template here serves the same purpose as putting
        //                 it on the Instance data type: it's acting as an identifying key.


        // This design of pulling out the statuses on their own would
        // make tracking and updating them a breeze. We could imagine
        // a little in-memory data structure.
        class MyCoolChecklistInMemoryStorage {
            private Map<Step, Status> statuses;
        }
        // (My continued belaboring) all of this is still just a sketch.
        // We're seeing how each of these decisions feels and how its effects
        // ripple outward through the code.
    }
}

