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
public class Listing4_2_to_4_5 {



    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.2 through 4.5
     * ───────────────────────────────────────────────────────
     * We have to force ourselves to *not* jump straight to the
     * code! Code is a terrible medium for thinking. It forces us
     * to think "in Java," which is extremely limited and has
     * nothing to do with the thing we're modeling.
     *  
     * Instead, we want to do the exercise from the previous chapter.
     * Step away from the code, and think about what it is we're
     * trying to model.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // Once we sketch out what the data in our domain is, we can
        // start with a process very similar to OOP: pick out the nouns.
        //
        // All models are an approximation. The challenge when designing
        // is figuring out which properties capture the essence of the
        // thing we're modeling.
        //
        // The good news is that we don't have to get it right the first time!
        // This is just a starting point. DoP makes it easy to iterate on
        // these choices. So, we'll loop through, pick out some things that
        // look important, and then refine from there.
        /**
         * Template:
         *                               ┌── Seems Important!
         *                          ┌───────────┐
         *     A named collection of things to do
         *         ▲
         *         └── Important!
         *
         * Instance:
         *
         *     ┌─── Important! It's how we can identity different "runs" of a checklist
         *     ▼
         * A named run-through of a template
         *                               ┌─── Time is an interesting part of the data
         *                               ▼
         *     at a particular point in time where
         *     we complete the things to do and
         *            ▲
         *            └── Another interesting piece. "Complete" paired with "point in time"
         *                has very interesting implications for our modeling.
         *     record the results.
         */
    }
}

