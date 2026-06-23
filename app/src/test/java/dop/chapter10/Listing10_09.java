package dop.chapter10;

import java.util.List;
import java.util.Optional;

public class Listing10_09 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.9
     * ───────────────────────────────────────────────────────
     * Sketching out the pieces that interact with the database
     * ───────────────────────────────────────────────────────
     */
    class InvoiceRepository implements BulkLoader {

        @Override
        public void load(UpdatesAndInserts records) {}
    }
    class DisputeRepository implements BulkLoader {

        @Override
        public void load(UpdatesAndInserts records) {}
    }

    class AccountRepository implements BulkLoader {
        private DatabaseLib db;

        @Override
        public void load(UpdatesAndInserts records) {
            db.transact(records.inserts(), records.updates());
        }

        public Optional<Account> get(String id) {
            return __;
        }

        public boolean exists(String id) {
            return this.get(id).isPresent();
        }
        // other methods

    }
















    static Optional<Account> __;
    interface BulkLoader {
        void load(UpdatesAndInserts records);
    }
    record UpdatesAndInserts(List<Insert> inserts, List<Update> updates){}
    record Insert(String table, TableRecord record){}
    record Update(String table, TableRecord record){}
    record TableRecord(){}
    record Account(String id){}
    interface DatabaseLib {
        void transact(List<Insert> inserts, List<Update> updates);
    }
}
