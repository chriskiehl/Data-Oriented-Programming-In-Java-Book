package dop.chapter11;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Listing11_56 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.56
     * ───────────────────────────────────────────────────────
     * The test is hard coded, yet exhaustive
     * ───────────────────────────────────────────────────────
     */
    void example() {
        // The biggest thing you have to watch out for when getting
        // into Property Based Testing is trying to use that new
        // shiny hammer *everywhere*.
        //
        // Not everything benefits from PBT!
        // Example based testing if more than adequate in many situations.
        enum MirrorIs {EMPTY, UP_TO_DATE, STALE;}
//           ▲
//           └──── The interesting cases in the test are right here.
        record State(MirrorIs state,
                     Export primaryHas,
                     Optional<Export> mirrorHas) {
        }

        Instant age = Instant.now();
        List<State> possibleStates = List.of(
            new State(
                MirrorIs.EMPTY,                                //  ┐
                new Export(age),                               //  │
                Optional.empty()                               //  │
            ),                                                 //  │
            new State(                                         //  │
                MirrorIs.STALE,                                //  │
                new Export(age),                               //  │
                Optional.of(new Export(age.minus(1, SECONDS))) //  │
            ),                                                 //  │
            new State(                                         //  │◄── Generating data wouldn’t add anything.
                MirrorIs.UP_TO_DATE,                           //  │    These states already cover every
                new Export(age),                               //  │    notable case.
                Optional.of(new Export(age))                   //  │
            )                                                  //  ┘
        );
        // rest of test elided
    }














    public record Export(Instant createdOn) {}
}
