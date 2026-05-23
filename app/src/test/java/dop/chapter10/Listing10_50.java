package dop.chapter10;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Listing10_50 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.50
     * ───────────────────────────────────────────────────────
     * Collect writes into a fake transaction we commit later
     * ───────────────────────────────────────────────────────
     */
    // Here's another way we could do it that keeps the boundaries in tact.
    // Note: this is not **the** way to do it. It's **a** way to do it.
    interface Transact extends BiConsumer<List<Insert>, List<Update>> {}
//               ▲
//               └──── We can describe the implicit interface of the database
//                     library’s transact method

    class Transaction implements Transact {
//            ▲
//            └──── That allows us to sneakily intercept those calls, but without
//                  running the actual method
        List<Insert> inserts;
        List<Update> updates;

        public Transaction() {
            this.inserts = new ArrayList<>();
            this.updates = new ArrayList<>();
        }

        @Override
        public void accept(List<Insert> inserts, List<Update> updates) {
            this.inserts.addAll(inserts);           //  ◄── All this “transaction” does is record
            this.updates.addAll(updates);           //       its inputs that we’ll execute later
                                                    //
        }
    }

    class Transactor {
        private DatabaseLibrary db;

        //constructor

        public void transact(Consumer<Transaction> f) {
            Transaction tx = new Transaction();             //
            f.accept(tx);                                   //  ◄── The inputs are all eventually fed
            db.transact(tx.inserts, tx.updates);            //      to the real transaction method, thus
                                                            //      getting atomic behavior across multiple
                                                            //      disparate repositories.
        }
    }














    record Insert(String table, TableRecord record){}
    record Update(String table, TableRecord record){}
    record TableRecord(){}
    interface DatabaseLibrary {
        void transact(List<Insert> inserts, List<Update> updates);
    }
}
