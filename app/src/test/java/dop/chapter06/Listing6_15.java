package dop.chapter06;

public class Listing6_15 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.15
     * ───────────────────────────────────────────────────────
     * One of an infinite set of possible implementations
     * ───────────────────────────────────────────────────────
     */
    enum People {Bob, Mary}
    enum Jobs {Cook, Engineer}

    class SomeClass {
        JobsRepo jobsRepo;
        // [Hidden]

        People someMethod(Job job) {
//             ▲
//             └──── Maybe it looks like this?
            return this.jobsRepo.findPerson(job);
        }
    }

    // Or maybe...
    class SomeClassV2 {
        // [Hidden]

        People someMethod(Job job) {
            this.setCombobulator("large");   //  ┐
            universe.runSimulation(job);     //  │◄── Or like this! Methods can engage in any kind of
            collapseSingularity(this);       //  │    nonsense because they aren’t constrained by anything.
            return getChosenOne();           //  │    Their names and types are only a hint.
        }                                    //  ┘
        // Or it could...
        // Or maybe...
        // And so on off into infinity





        private void setCombobulator(String size) {

        }
    }















    interface Job {}

    interface JobsRepo {
        People findPerson(Job job);
    }

    JobsRepo jobsRepo;
    Universe universe;

    interface Universe {
        void runSimulation(Job job);
    }

    void collapseSingularity(Object value) {}

    People getChosenOne() { return People.Bob; }
}
