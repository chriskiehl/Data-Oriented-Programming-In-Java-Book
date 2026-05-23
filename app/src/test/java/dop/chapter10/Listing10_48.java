package dop.chapter10;

import java.util.List;

public class Listing10_48 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.48
     * ───────────────────────────────────────────────────────
     * The database’s transact method controls write atomicity
     * ───────────────────────────────────────────────────────
     */
    class AccountRepo {
        private DatabaseLib db;

        public void upsert(List<Account> accounts) {
            List<Insert> inserts = /* logic elided */__;
            List<Update> updates = /* logic elided */___;
            db.transact(/*...*/);
//                ▲
//                └──── Transaction semantics are bound to the library’s transact
//                      method. Once this is done, the transaction is done.
        }
    }














    record Account(String id){}
    record Insert(String table, TableRecord record){}
    record Update(String table, TableRecord record){}
    record TableRecord(){}
    static List<Insert> __;
    static List<Update> ___;
    interface DatabaseLib {
        void transact();
    }
}
