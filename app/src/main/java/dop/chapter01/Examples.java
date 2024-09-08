package dop.chapter01;

import dop.chapter01.Examples.RetryDecision.Abandoned;
import dop.chapter01.Examples.RetryDecision.ReattemptLater;
import dop.chapter01.Examples.RetryDecision.RetryImmediately;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.time.LocalDateTime.now;


public class Examples {

    /**
     * ───────────────────────────────────────────────────────
     *                      Listing 1.1
     * ───────────────────────────────────────────────────────
     *
     * Here's an example of how we might traditionally model
     * data "as data" using a Java object.
     * ───────────────────────────────────────────────────────
     */
    public static class Point {
        private final double x;   // ◄── The object is entirely defined by these attributes.
        private final double y;   // ◄── It has no behaviors. It has no hidden state. It's "just" data.

        // We omit everything below in the chapter for brevity. But we need
        // all this stuff in order to get an object in Java to behave like a
        // value rather than an identity (we'll explore that idea in detail
        // in chapter 02!)
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        // Equality is very important when modeling data, but that's a topic
        // for chapter 02 ^_^
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return Double.compare(x, point.x) == 0 && Double.compare(y, point.y) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        // Any accessors we define manually will be done without the
        // Java Bean style `get` prefix. This is so that our transition to
        // records is easy.
        public double x() {
            return x;
        }

        public double y() {
            return y;
        }
    }


    /**
     * ───────────────────────────────────────────────────────
     *                      Listing 1.2
     * ───────────────────────────────────────────────────────
     * This example is all about the ambiguity that most of our
     * representations carry with them. Checkout this attribute
     * we've called ID. What goes there? What does the data type
     * of String communicate to us as we read the code? Nothing!
     * String could be any infinite number of things.
     * ───────────────────────────────────────────────────────
     */
    static class AmbiguousRepresentationExample {
        String id; // ◄── DoP is largely just the act of noticing
                   //     that code like this is extremely vague.
    }


    /**
     * ───────────────────────────────────────────────────────
     *                      Listing 1.3
     * ───────────────────────────────────────────────────────
     * The representation we've picked for the ID, despite being
     * trivial and one line, captures the soul of data oriented
     * programming. Our representation **creates** the possibility
     * for invalid program states. There are more wrong ways to create
     * this thing we've called ID than there are correct ones!
     * In fact, we still have no idea what ID really means. All we
     * can do with the current code is guess.
     * ───────────────────────────────────────────────────────
     */
    public static void wrongWaysOfCreatingAnId() {
        // Every one of these is valid from the perspective of
        // how we've represented the idea in the type system.
        // Every one of these is invalid because they're all values
        // which don't belong to our domain (which hasn't been
        // communicated to us in the code)
        String id;
        id = "not-a-valid-uuid";
        id = "Hello World!";
        id = "2024-05-04";
        id = "2024-05-04";
        id = "1010011001011011";
    }


    /**
     * ───────────────────────────────────────────────────────
     *                      Listing 1.4
     * ───────────────────────────────────────────────────────
     * Here's the magic of representation. I don't have to tell
     * you out of band what that ID is supposed to be. You don't
     * have to read an internal wiki, or ask a coworker, or look
     * inside the database. We can make the code itself communicate
     * what ID means by picking a better representation.
     * ───────────────────────────────────────────────────────
     */
    static class ImprovedRepresentation {
        UUID id;   //  ◄── THIS tells us exactly what that ID should be! A UUID.
                   //      Not an arbitrary string. Not a Product Code or SKU.
                   //      ID is a UUID. Try to give it anything else and your
                   //      code won't compile.
    }






    /**
     * ───────────────────────────────────────────────────────
     *                  Listings 1.5 & 1.6
     * ───────────────────────────────────────────────────────
     * Representation affects our ability to understand the code
     * as a whole. The class below, ScheduledTask, is an example
     * I stole (after simplifying and anonymizing) from a project
     * I worked on. Without knowing anything other than that fact
     * that it deals with scheduling (which we can tell from its
     * name), the challenge we take on in the chapter is simply
     * trying to understand what the heck the `reschedule() method
     * is trying to do.
     * ───────────────────────────────────────────────────────
     */
    static class ScheduledTask {
        private LocalDateTime scheduledAt;
        private int attempts;


        //────────────────────────────────────────────────────────────────┐
        void reschedule() {                                             //│ Checkout this method. What does it do?
            if (this.someSuperComplexCondition()) {                     //│ Or, more specifically, what does it mean?
                this.setScheduledAt(now().plusSeconds(this.delay()));   //│ It clearly assigns some values to some
                this.setAttempts(this.attempts() + 1);                  //│ variables, but... we as newcomers to this
            } else if (this.someOtherComplexCondition()) {              //│ code, what information can we extract from
                this.setScheduledAt(this.standardInterval());           //│ just this method?
                this.setAttempts(0);                                    //│
            } else {                                                    //│
                this.setScheduledAt(null);                              //│
                this.setAttempts(0);                                    //│
            }                                                           //│
        }                                                               //│
        // ───────────────────────────────────────────────────────────────┘

        //───────────────────────────────────────────────────┐
        boolean someSuperComplexCondition() {            //  │ Note!
            return false;                                //  │ These are just here so the code will
        }                                                //  │ compile. They return fixed junk values.
        boolean someOtherComplexCondition() {            //  │ They should be ignored
            return false;                                //  │ for the purposes of the exercise.
        }                                                //  │
        int delay() {                                    //  │
            return 0;                                    //  │
        }                                                //  │
        //  │
        private LocalDateTime standardInterval() {       //  │
            return now();                                //  │
        }                                                //  │
        //───────────────────────────────────────────────────┘

        private LocalDateTime scheduledAt() {
            return scheduledAt;
        }

        private int attempts() {
            return attempts;
        }

        public void setScheduledAt(LocalDateTime scheduledAt) {
            this.scheduledAt = scheduledAt;
        }

        public void setAttempts(int attempts) {
            this.attempts = attempts;
        }
    }




    /**
     * ───────────────────────────────────────────────────────
     *                     Listings 1.7
     * ───────────────────────────────────────────────────────
     * The problem with the example above (listing 1.5 & 1.6) is
     * that we can't tell what any of it means. The code doesn't
     * tell us why it's setting those variables. Instead, we have
     * to piece it together "non-locally" by hunting down clues in
     * other parts of the codebase.
     * ───────────────────────────────────────────────────────
     */
    static class Scheduler {
        List<ScheduledTask> tasks;

        // (Imagine a bunch of other methods here...)

        // If we're lucky, we might eventually stumble on one that
        // explains what the heck a particular state means.
        // In this case, we figure out that if we set scheduledAt to
        // null, that implicitly means that the scheduler should remove
        // this tasks and give up on it.
        private void pruneTasks() {
            this.tasks.removeIf((task) -> task.scheduledAt() == null);
        }
    }


    /**
     * (This modeling is not shown in the book for brevity)
     * We're creating a family of related data types. The mechanics of this
     * construct will be covered in Chapters 3 and 4.
     */
    sealed interface RetryDecision {
        record RetryImmediately(LocalDateTime next, int attemptsSoFar) implements RetryDecision {}
        record ReattemptLater(LocalDateTime next) implements RetryDecision {}
        record Abandoned() implements RetryDecision {}
    }


    /**
     * ───────────────────────────────────────────────────────
     *                  Listings 1.8
     * ───────────────────────────────────────────────────────
     * The thing we strive for in data-oriented programming is
     * to be able to communicate effectively within the code.
     * We want to use a representation that tells any reader
     * exactly what we're trying to accomplish. Those opaque
     * variable assignments can be made clear by giving them
     * names. Naming is a magical thing with tons of power!
     * ───────────────────────────────────────────────────────
     */
    static class ScheduledTaskV2 {
        // We've replaced the ambiguous instance variables with
        // a new descriptive data type.
        private RetryDecision status;  // ◄── NEW!


        // Checkout how different this code *feels*.
        // it now tells us exactly what it does.
        // It chooses between 1 of 3 possible actions:
        //    * Retry Immediately
        //    * Attempt this later
        //    * Abandon it entirely
        void reschedule() {
            if (this.someSuperComplexCondition()) {
                this.setStatus(new RetryImmediately(  // ◄── NEW!
                    now().plusSeconds(this.delay()),
                    this.attempts(status) + 1
                ));
            } else if (this.someOtherComplexCondition()) {
                this.setStatus(new ReattemptLater(this.standardInterval())); // ◄── NEW!
            } else {
                this.setStatus(new Abandoned());    // ◄── NEW!
            }
        }

        //───────────────────────────────────────────────────┐
        boolean someSuperComplexCondition() {            //  │ Note!
            return false;                                //  │ These are just here so the code will
        }                                                //  │ compile. They return fixed junk values
        boolean someOtherComplexCondition() {            //  │ because they're supposed to be ignored
            return false;                                //  │ for the purposes of the exercise.
        }                                                //  │
        int delay() {                                    //  │
            return 0;                                    //  │
        }                                                //  │
        //  │
        private LocalDateTime standardInterval() {       //  │
            return now();                                //  │
        }                                                //  │
                                                         //  │
        private int attempts(RetryDecision decision) {   //  │
            return 1;                                    //  │
        }                                                //  │
        //───────────────────────────────────────────────────┘

        private void setStatus(RetryDecision decision) {
            this.status = decision;
        }
        public RetryDecision status() {
            return this.status;
        }
    }







    /**
     * ───────────────────────────────────────────────────────
     *                 Listings 1.9 & 1.10
     * ───────────────────────────────────────────────────────
     * Good modeling has a simplifying effect on the entire
     * codebase. We can refactor other parts of the code to
     * use the domain concepts. It replaces "hmm... Well, null
     * must mean that..." style reasoning with concrete, declarative
     * code that tells you *exactly* what it means.
     * ───────────────────────────────────────────────────────
     */
    static class SchedulerV2 {
        List<ScheduledTaskV2> tasks;

        // (Imagine a bunch of other methods here...)

        // Refactoring to use our explicit data type
        private void pruneTasks() {
            // Don't let this instanceof scare you off!
            // This would be "bad" when doing OOP and dealing with objects (with
            // identities), but we're not doing OOP. We're programming with data.
            this.tasks.removeIf((task) -> task.status() instanceof Abandoned);
            // Compare this to the original version!
            // [Original]: this.tasks.removeIf((task) -> task.scheduledAt() == null);
        }
    }





    /**
     * ───────────────────────────────────────────────────────
     *                  Listings 1.11
     * ───────────────────────────────────────────────────────
     * You might argue that the problem with the original code
     * was that it leaked information. It didn't define domain
     * level API methods for consumers. Fair! Let's see what
     * that looks like.
     * ───────────────────────────────────────────────────────
     */
    static class ScheduledTaskWithBetterOOP {
        private LocalDateTime scheduledAt;   // we might keep the design of
        private int attempts;                // these attributes the same


        void reschedule() {
            // body omitted for brevity
        }

        //──────────────────────────────────────┐
        public boolean isAbandoned() {        //│ And use this to hide the implementation details while
            return this.scheduledAt == null;  //│ also clarifying what the state assignments mean.
        }                                     //│ A nice improvement!
        //──────────────────────────────────────┘
    }





    /**
     * ───────────────────────────────────────────────────────
     *                   Listings 1.12
     * ───────────────────────────────────────────────────────
     * The improvements from Listing 1.11 ripple outward in a
     * similar way to the improvements we made in listing 1.9 & 1.10.
     * ───────────────────────────────────────────────────────
     */
    static class SchedulerV3 {
        List<ScheduledTaskWithBetterOOP> tasks;

        // (Imagine a bunch of other methods here...)

        private void pruneTasks() {
            // The API method nicely clarifies what the state of the
            // task means. This is much better than an ambiguous null check.
            this.tasks.removeIf((task) -> task.isAbandoned());
        }
    }




    /**
     * ───────────────────────────────────────────────────────
     *                   Listings 1.13
     * ───────────────────────────────────────────────────────
     * Luckily, it's not one or the other. It's not OOP vs DoP.
     * We can combine the strengths of both approaches.
     * ───────────────────────────────────────────────────────
     */
    class ScheduledTaskWithBestOfBothWorlds {
        private RetryDecision status;

        void reschedule() {
            /// ...
        }

        public boolean isAbandoned() {
            // By combing the approaches, we get a nice internal
            // representation to program against. We can still use
            // OOP to control the interfaces. Further, doesn't this
            // code feel almost like it's writing itself? Of course
            // we'd expose this method from our object -- it's a
            // core idea we uncovered while modeling the data!
            return this.status instanceof Abandoned;
        }
    }

}
