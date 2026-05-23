package dop.chapter10;

import java.util.List;
import java.util.function.BiConsumer;

public class Listing10_51 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.51
     * ───────────────────────────────────────────────────────
     * Updating the repositories to optionally use this “transaction”
     * ───────────────────────────────────────────────────────
     */
    class AccountRepo {
        private DatabaseLib db;

        public void upsert(List<Dispute> disputes) {  //  ┐
            this.upsert(db::transact, disputes);      //  ┘◄── This method now just delegates to the
                                                      //       transactional one using the library’s
                                                      //       implicit BiConsumer interface
        }

        public void upsert(Transact tx, List<Dispute> disputes) {
            List<Insert> inserts = /* logic here */__;
            List<Update> updates = /* logic here */___;
            tx.accept(inserts, updates);
//                  ▲
//                  └──── If it’s the real library, we’ll run immediately.
//                        Otherwise, the “transaction” just records these
//                        inputs for later.
        }
    }














    interface Transact extends BiConsumer<List<Insert>, List<Update>> {}
    record Insert(String table, TableRecord record){}
    record Update(String table, TableRecord record){}
    record TableRecord(){}
    record Dispute(String id){}
    interface DatabaseLib {
        void transact(List<Insert> inserts, List<Update> updates);
    }
    static List<Insert> __;
    static List<Update> ___;
}
