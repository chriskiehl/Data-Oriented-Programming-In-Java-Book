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
public class Listing4_1 {

    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.1
     * ───────────────────────────────────────────────────────
     * We're going to model a checklist! How could anyone possibly
     * screw up making a checklist? I'm pretty sure I accidentally
     * found all the ways.
     *
     * We might start with the "obvious" approach:
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        //             ┌── A named "thing to do"
        //             ▼
        record Step(String name, boolean isComplete) {
        }
        //                           ▲
        //                           └── And it's either done or not
        record Checklist(List<Step> steps) {
        }
        //          ▲
        //          └─ A checklist is just a collection of steps.
        //             If all the steps are complete. The checklist is complete.
        // Easy Peasy?
        //
        // Done?
    }
}

