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
public class Listing4_9 {



    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.9
     * ───────────────────────────────────────────────────────
     * Here's the messiness of the design process (which is both
     * expected, normal, and OK). We don't know where this concept
     * of `isCompleted` goes yet, but we *do* know from the previous
     * listing that it doesn't belong on `Step`.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        record Step(String name /* (removed) */ ) {
        }      //                      ▲
        //                             └─ No more isCompleted here.
        //                                We have to figure out where it goes.
    }
}

