package dop.chapter11;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.generate;

public class Listing11_51 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.51
     * ───────────────────────────────────────────────────────
     * The same test can fill multiple roles just by changing the amount of data
     * ───────────────────────────────────────────────────────
     */
    void example() {
        Set<Account> accounts = generate(this::mkAcnt)
                .limit(10)                            //  ┐
                // .limit(1000)                       //  │◄── We can change the role of the test
                // .limit(1_000_000)                  //  ┘    just by changing the amount of data
                .collect(toSet());                    //       it exercises
        // rest of test is the same
    }














    record Account(String id){}
    Account mkAcnt() { return new Account(""); }
}
