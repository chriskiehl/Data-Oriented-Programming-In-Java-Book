package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Listing11_31 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.31
     * ───────────────────────────────────────────────────────
     * Representing the state of the world with data
     * ───────────────────────────────────────────────────────
     */
    @Test
    void replicatingToTheMirror() {
        enum MirrorIs {EMPTY, UP_TO_DATE, STALE;}  //  ┐
        record State(MirrorIs state,               //  │
                     Export primaryHas,            //  │◄── Data types can be declared
                     Optional<Export> mirrorHas) { //  │    locally in the test
        }                                          //  ┘

        Instant age = Instant.now();
        List<State> possibleStates = List.of(
            new State(
                MirrorIs.EMPTY,                                 //  ┐
                new Export(age),                                //  │
                Optional.empty()                                //  │
            ),                                                  //  │
            new State(                                          //  │
                MirrorIs.STALE,                                 //  │
                new Export(age),                                //  │◄── And we can use them to show
                Optional.of(new Export(age.minus(1, SECONDS)))  //  │    exactly what each state means
            ),                                                  //  │
            new State(                                          //  │
                MirrorIs.UP_TO_DATE,                            //  │
                new Export(age),                                //  │
                Optional.of(new Export(age))                    //  │
            )                                                   //  ┘
        );

        for (State testState : possibleStates) {
            LegacyLedger primary = mock(LegacyLedger.class);
            when(primary.getLatestExport()).thenReturn(testState.primaryHas());  //  ┐
            Mirror mirror = mock(Mirror.class);                                  //  │
            when(mirror.getLatestExport()).thenReturn(testState.mirrorHas());    //  │
            Synchronizer sychronizer = new Synchronizer(primary, mirror);        //  │
                                                                                 //  │
            sychronizer.run();                                                   //  │
            if (testState.state().equals(MirrorIs.UP_TO_DATE)) {                 //  │
                                                                                 //  │◄── We don’t mock hard
                                                                                 //  │    coded values; we drive
                                                                                 //  │    them from our data model
                verify(mirror, never()).save(testState.primaryHas());            //  │
            } else {                                                             //  │
                verify(mirror, times(1)).save(testState.primaryHas());           //  │
            }                                                                    //  ┘
        }
    }














    public record Export(Instant createdOn) {}
    public interface LegacyLedger {
        Export getLatestExport();
    }
    public interface Mirror {
        Optional<Export> getLatestExport();
        void save(Export file);
    }
    class Synchronizer {
        Synchronizer(LegacyLedger ledger, Mirror mirror) {}
        void run() {}
    }
}
