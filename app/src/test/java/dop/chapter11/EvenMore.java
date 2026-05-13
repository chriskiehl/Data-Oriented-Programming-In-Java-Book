package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.mockito.Mockito.*;

public class EvenMore {

  public record Export(Instant createdOn /* etc */) {
  }
  public interface LegacyLedger {
    Export getLatestExport();
  }
  public interface Mirror {
    Optional<Export> getLatestExport();
    void save(Export file);
  }

  static class Synchronizer {
    private LegacyLedger ledger;
    private Mirror mirror;

    public Synchronizer(LegacyLedger ledger, Mirror mirror) {
      this.ledger = ledger;
      this.mirror = mirror;
    }

    public void run() {
      Export latestInLedger = this.ledger.getLatestExport();
      Optional<Export> latestInMirror = this.mirror.getLatestExport();
      if (latestInMirror.isEmpty()) {
        mirror.save(latestInLedger);
      } else {
        Export mirrorFile = latestInMirror.get();
        if (latestInLedger.createdOn().isAfter(mirrorFile.createdOn())) {
          // we only copy down the file if it's newer to avoid
          // unnecessary load on the server
          mirror.save(latestInLedger);
        }
      }
    }
  }


@Test
void asdfasdf() {
  enum MirrorIs {EMPTY, UP_TO_DATE, STALE;}
  record State(MirrorIs state,
               Export primaryHas,
               Optional<Export> mirrorHas) {
  }

  Instant age = Instant.now();
  List<State> possibleStates = List.of(
    new State(
      MirrorIs.EMPTY,
      new Export(age),
      Optional.empty()
    ),
    new State(
      MirrorIs.STALE,
      new Export(age),
      Optional.of(new Export(age.minus(1, SECONDS)))
    ),
    new State(
      MirrorIs.UP_TO_DATE,
      new Export(age),
      Optional.of(new Export(age))
    )
  );

  for (State state : possibleStates) {
    LegacyLedger primary = mock(LegacyLedger.class);
    when(primary.getLatestExport()).thenReturn(state.primaryHas());
    Mirror mirror = mock(Mirror.class);
    when(mirror.getLatestExport()).thenReturn(state.mirrorHas());
    Synchronizer sychronizer = new Synchronizer(primary, mirror);

    sychronizer.run();
    if (state.state().equals(MirrorIs.UP_TO_DATE)) {
      verify(mirror, never()).save(state.primaryHas());
    } else {
      verify(mirror, times(1)).save(state.primaryHas());
    }
  }
}

}
