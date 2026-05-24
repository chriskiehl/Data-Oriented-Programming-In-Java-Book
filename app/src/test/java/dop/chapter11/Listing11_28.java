package dop.chapter11;

import java.time.Instant;
import java.util.Optional;

public class Listing11_28 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.28
     * ───────────────────────────────────────────────────────
     * Something like...
     * ───────────────────────────────────────────────────────
     */
    public record Export(Instant createdOn /* etc */) {}
    public interface LegacyLedger {
        Export getLatestExport();
    }
    public interface Mirror {
        Optional<Export> getLatestExport();
        void save(Export file);
    }
    class Synchronizer {
        private LegacyLedger ledger;
        private Mirror mirror;
        // constructor

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
}
