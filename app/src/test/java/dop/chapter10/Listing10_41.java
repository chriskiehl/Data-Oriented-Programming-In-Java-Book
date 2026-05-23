package dop.chapter10;

import java.util.List;

public class Listing10_41 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.41
     * ───────────────────────────────────────────────────────
     * A wrapper that speaks our semantics
     * ───────────────────────────────────────────────────────
     */
    class AccountRepo {
        private DatabaseLib databaseLib;

        public void upsert(List<Account> accounts) {
//                                 ▲
//                                 └──── We expose a method which mirrors the semantics
//                                       desired by the application
            databaseLib.transact(/*Updates and Inserts*/);
//                                         ▲
//                                         └──── The details of “how” are hidden away
        }
    }














    record Account(String id){}
    interface DatabaseLib {
        void transact();
    }
}
