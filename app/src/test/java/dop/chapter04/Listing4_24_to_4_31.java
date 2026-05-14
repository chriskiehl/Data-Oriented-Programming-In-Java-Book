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
public class Listing4_24_to_4_31 {



    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.24 through 4.31
     * ───────────────────────────────────────────────────────
     * Obvious things which maybe aren't so obvious: there's an
     * implicit AND between everything in a record.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void example() {
        // All defined in previous listings
        record Step(String name) {}
        record Template(String name, List<Step> steps) {}
        record Instance(String name, Instant date, Template template){}
        record User(String value){}
        enum State {NOT_STARTED, COMPLETED, SKIPPED};

        record Status(    // ◄─────────────────┐
                String name,             //    │
       //       (AND)                    //    │ When we define a data type, we’re saying it’s made up of
                State state,             //    │ attribute 1 AND attribute 2 AND ... AND ... AND ...AND.
       //       (AND)                    //    │
                User actionedBy,         //    │
       //       (AND)                    //    │
                Instant actionedOn,      //    │
       //       (AND)
                User confirmedBy,
       //       (AND)
                String rational
        ) {}

        // This ANDing is the source of our code's lying.
        // It's saying that a status is **always** name AND state AND actionedBy AND ...
        // But that's not true.
        // Attributes like `actionedBy` and `confirmedBy` are only available **sometimes**

        // A big part of DoP is retraining ourselves to read the code for exactly what it
        // says. The code is directly lying to us, but we're used to mentally "patching"
        // around those lies.

        // STARTING FRESH.
        // Let's revert back to before we did our "refactoring"
        record OriginalStatusModel(
            Template template,
            Step step,
            boolean isCompleted,
            User completedBy,
            Instant completedOn
       ){}


                //
        // We'll rebuild the Status data type piece by piece, at each
        // step we're going to force ourselves to read the code for exactly
        // what it says. We'll explcitly pause to notice what the implicit ANDs
        // are doing to our data model.

        record StatusV1(
                String name, // │
        //      (AND)           │  So far so good. These ANDs make sense
                Step step   //  │
        ) {}

        // Adding our next attribute back in:
        record StatusV2(
                String name,         //  │
        //      (AND)                //  │
                Step step,           //  │
        //      (AND)                //  │ This one feels a little weird, but it still
                boolean isCompleted  //  │ seems reasonable overall
        ) {}

        // If we keep going, we slam into a problem
        record StatusV3(
                String name,         //  │
        //      (AND)                //  │
                Step step,           //  │
        //      (AND)                //  │
                boolean isCompleted, //  │
        //      (AND)                //  │ Right here we hit a hard wall. This attribute cannot be ANDed
                User completedBy     //  │ with the rest, because it’s only defined *sometimes*
        ) {}

        // This is where we really have to read the code for exactly what's there.
        // What about this combination of attributes makes the modeling "wrong"?
        // Anything? Nothing?
        //
        // The friction is between what we're trying to model (some kind of generic
        // "status" data type) and what the code, as we've written it, actually represents.
        // This current collection of attributes is fine to AND together -- as long as we
        // listen to what their meaning tells us.
        //
        // This combination of attributes doesn't describe a Status that can be Not started
        // OR completed, it specifically describes something that's Completed -- that's why
        // the attributes are there, after all.
        //
        // Listening to what the attributes mean, we can adjust our naming of the record:
        //
        record Completed(
                String name,         //  │ Look how cohesive these attribute are
        //      (AND)                //  │ now that we've scoped them to exactly what
                Step step,           //  │ they "wanted" to represent: a step that
        //      (AND)                //  │ has been completed.
                User completedBy,    //  │
        //      (AND)                //  │ We also simplified the model. We no longer
                Instant completedOn  //  │ need the isCompleted boolean. This *is* completed!
        ) {}

        // This "aha!" moment is the best part of the design process.
        // If the above only and exclusively represents Completed steps, then we
        // also need to model steps before they're completed. i.e.

        record NotStarted(
                Template template,   //  │
        //      (AND)                //  │ There is no mention of completedBy here, because it’s not completed!
                Step step            //  │ It’s Not Started!
        ){}

        // Now adding skipped back into the model is *actually* simple.
        // This is what good modeling feels like. It feels smooth. Without friction.
        record Skipped(
                Template template,
        //      (AND)
                Step step,
        //      (AND)
                User skippedBy,
        //      (AND)
                Instant skippedOn,
        //      (AND)
                String rationale
        //      (AND)
        ){}
    }
}

