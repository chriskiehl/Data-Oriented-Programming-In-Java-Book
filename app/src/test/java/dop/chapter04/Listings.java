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
public class Listings {

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
    public void listing4_1() {
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
        // ...Done?
    }


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
    public void listing4_2_to_4_5() {
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


    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.6 through 4.8
     * ───────────────────────────────────────────────────────
     * Here's our second stab at turning what we know about our
     * data into code.
     *
     * Once we've got the sketch in place, we start the important
     * part of the design process: taking a step back to look at
     * what our sketch of the data model *means*. What does it
     * communicate? What does it enable us to "say" with the code?
     * ───────────────────────────────────────────────────────
     */
    public void listing4_6_to_4_7() {


        // Reusing the model from our initial stab at this in listing 4.1 just to kick things off
        record Step(String name, boolean isCompleted) {
        }

        // Note this refinement from the initial stab at this.
        // Steps live on a "Template", rather than what we ambiguously
        // called just a “Checklist” before.
        record Template(String name, List<Step> steps) {
        }

        // [IGNORE] (We haven't modeled this yet!)
        // This is here just to make the example below compilable.
        record Instance(List<Step> steps) {}
        // [END IGNORE]

        // The quick check we can always perform while design is seeing
        // if our code lets us express anything weird.
        Template travelPrep = new Template(
                "International Travel Checklist",
                List.of(
                        new Step("Passport", false),
                        new Step("Toothbrush", false),
                        new Step("bring socks", true)
                ) //                         ▲
                //                           └─ What the heck does it mean if items on a template are pre-set
                //                              to true? This step is already always completed? All socks are
                //                              already pre-packed for all trips?
                //
                // Our data model allows us to express a bunch of nonsense!
        );

        // This is a dangerous part in the design process that we have to train
        // ourselves to notice. The "programmer" part of our brain -- the one that
        // thinks in code, might look at this modeling error and start down an
        // innocent sounding, but critically damaging thought pattern: the one that
        // usually starts with "well, we could just..."
        //
        // For example:
        //
        // "We could just..." use a defensive check or transform during construction
        // to make sure that no funky states are created.
        new Instance(travelPrep.steps().stream()
                .map(step -> new Step(step.name(), false))
                .toList());                //        └── Defensively resetting all the steps "just in case"
        //            when we create instances of our checklist.

        // This works under exactly one condition: you remember to do it.

        // We can do better. We can make the code enforce its own meaning and
        // help us, the fallible programmers, not make mistakes.
    }


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
    public void listing4_9() {
        record Step(String name /* (removed) */ ) {
        }      //                      ▲
        //                             └─ No more isCompleted here.
        //                                We have to figure out where it goes.
    }


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
    public void listing4_10_to_4_13() {
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
    public void listing4_14_to_4_16() {
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


    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.17 through 4.18
     * ───────────────────────────────────────────────────────
     * Defensive coding to the rescue?
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing4_17_to_4_18() {
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



    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.19 through 4.23
     * ───────────────────────────────────────────────────────
     * More requirements! Steps can be skipped!
     * Skipping steps on a checklist is a big deal in rocketry.
     * We have to know who did them, when, and *why*.
     *
     * Should be a small lift, right? After all, we already figured
     * out how to track when the steps got completed. Doing the
     * same thing for skipped should be a breeze.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing4_19_4_23() {
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
                Instant completedOn,
                Boolean isSkipped,    //  ◄──┐
                User skippedBy,       //     │ Copy/pasting our existing modeling.
                Instant skippedOn,    //     │ We get another flag, another user, another
                String rationale      //     │ timestamp, plus a new field for tracking why
                                      //     │ the step was skipped.
        ){
            // Fun exercise for the reader:
            // Try to write constructor validation which can catch every illegal state.
            //
            // I initially had this in the book to point out how egregious the validation
            // becomes, but it grew beyond something which would fit on a page. Also, worth
            // noting: I repeatedly got that validation wrong. I'd miss a case, or mix up
            // two cases, disable things which should be allowed -- it's the kind of validation
            // that makes you sit there and work through "Ok, so when x is set, y and z should
            // NOT be set... but when... then..."
        }

        // What surely hops out with the modeling above is that it isn't "DRY".
        // We might try to refactor it "as code" -- meaning, ignoring what the
        // meaning of the underlying data is (what we're trying to capture) and
        // instead refactoring "mechanically" -- manipulating the symbols to factor
        // out the duplication.

        // For instance, we can refactor the multiple booleans into
        // a single Enum. Nice!
        enum State {NOT_STARTED, COMPLETED, SKIPPED};
        // Ditto for the non-DRY user definitions. 
        record StatusV2(
                String name,
                State state,
                User actionedBy,    // We factor them out into a shared "actionedBy"
                Instant actionedOn, // shape that's contextual based on the current state.
                User confirmedBy,
                String rational
        ) {
            StatusV2 {
                // But, ugh.. this hasn't actually made our lives that much easier.
                // Our code is still allowed to express nonsensical states. Which means
                // we're still on the hook for defending against them. And that remains
                // extremely tedious and error prone.
                //
                if (state.equals(State.NOT_STARTED)) {   // The implementation for each case is left
                    // ...                                  as an exercise to the reader.
                }
                if (state.equals(State.COMPLETED)) {
                    // ...
                }
                if (state.equals(State.SKIPPED)) {
                    // ...
                }
                // This only gets worse as our requirements get more complex.
                // Imagine adding a Blocked or InProgress state. Each one will
                // require thinking *very* hard about the validation you and
                // what it means for existing states.
            }
        }

        // The woes with the design go beyond the difficulty in validating it.
        // We have that ongoing problem where our data is "forgetful". Aside from
        // when we're validating it, we have no idea what status it's in.
        // So it again falls to the frail humans working on the code to remember
        // that (a) all of those statuses exist, (b) only some of them apply to
        // certain behaviors in the system, and (c) we have to *remember* to check
        // before we do anything.
        Function<StatusV2, Void> doSomethingWithCompleted = (StatusV2 status) -> {
            if (!status.state().equals(State.COMPLETED)) {
                // If we remember to do this, then we know that
                // we can safely read the actionedBy/On attributes
                // without a Null Pointer getting thrown.
            }
            // otherwise, we have to throw an error.
            throw new IllegalArgumentException("Expected completed");
        };

        // The problem with our current "refactored" model is that it hasn't
        // really solved any of the problems. The way we've modeled the code
        // *creates* invalid states and potential bugs. We have to expend tons
        // of effort fighting against the monster we created.
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.24 through 4.31
     * ───────────────────────────────────────────────────────
     * Obvious things which maybe aren't so obvious: there's an
     * implicit AND between everything in a record.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing4_24_to_4_31() {
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


    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.32 through 4.34
     * ───────────────────────────────────────────────────────
     * From AND, AND, AND to OR, OR, OR
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing4_32() {
        // All defined in previous listings
        record Step(String name) {}
        record Template(String name, List<Step> steps) {}
        record Instance(String name, Instant date, Template template){}
        record User(String value){}

        // records cannot extend classes, so we tie them
        // together with an interface.
        interface StepState { }

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


    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.35
     * ───────────────────────────────────────────────────────
     * Expressing OR with records and interfaces is like having
     * super powered enums. The two modeling ideas are very similar.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing4_35() {
        enum StepStatus {
            NotStarted,
            Completed,
            Skipped;
        }

        interface StepState {}                          // An alternative way of modeling the
        record NotStarted() implements StepState {}    // idea of mutual exclusivity.
        record Completed() implements StepState {}     // Thinking of them *as* fancy enums can be
        record Skipped() implements StepState {}       // a really useful mental model.
    }



    /**
     * ───────────────────────────────────────────────────────
     * Listings 4.36 through 4.40
     * ───────────────────────────────────────────────────────
     * A very important part of modeling is being able to say
     * what something *isn't*. This is the central value proposition
     * of enums. It allows us to model a closed domain.
     *
     * A gap with how we've modeled the StepState so far is that it
     * is *not* closed. It can be freely extended by anyone. Sometimes
     * this is what we want. Open to extension is a fantastic principle
     * for library development. However, just as often, we do not
     * want our model extended. There are only two booleans. There
     * are only 4 card suits. In our domain, there are only three
     * states a checlist step can be: NotStarted, Completed, or Skipped.
     * ───────────────────────────────────────────────────────
     */
    @Test
    public void listing4_36_to_4_40() {
        interface StepState {}
        record NotStarted() implements StepState {}
        record Completed() implements StepState {}
        record Skipped() implements StepState {}

        // The problem here is that this isn't closed. It's completely
        // open to anyone who wants to extend the interface.
        // Are these members of our domain?
        record Blocked() implements StepState {}
        record Paused() implements StepState {}
        record Started() implements StepState {}
        // They might be valid in some *other* domain, but they aren't
        // valid in ours.
        //
        // This is the role of the `sealed` modifier.
        // We can tell Java that we only want to permit certain
        // data types to implement our interface.

        // Note: sealing doesn't work locally inside a method.
        //       So, it's commented out here. Checkout the supplementary
        //       file `test.dop.chapter03.SealingExample` to see it in action
        /* sealed */ interface StepStateV2 {}
        record NotStartedV2() implements StepStateV2 {}
        record CompletedV2() implements StepStateV2 {}
        record SkippedV2() implements StepStateV2 {}
    }

}

