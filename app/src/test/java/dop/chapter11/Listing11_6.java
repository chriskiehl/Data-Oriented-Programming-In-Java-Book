package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class Listing11_6 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.6
     * ───────────────────────────────────────────────────────
     * How much nicer is this?
     * ───────────────────────────────────────────────────────
     */
    @Test
    void goodCustomersGetTheMaximumGracePeriod() {
        // Seriously, how much nicer is it when we can look at a test
        // and reason about it locally? We don't have to think about
        // state or mocks or how to synchronize the environment "inside"
        // the function with the environment "outside" of it.
        //
        // Functions build on pure data are delightful to reason about
        // and test. 
        LocalDate dueDate = LocalDate.now();
        LocalDate currentDate = dueDate.plusDays(MAX_GRACE_PERIOD);

        // ...
    }














    static int MAX_GRACE_PERIOD = 60;
}
