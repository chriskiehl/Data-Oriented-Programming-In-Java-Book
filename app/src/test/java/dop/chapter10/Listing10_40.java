package dop.chapter10;

import java.util.List;

public class Listing10_40 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.40
     * ───────────────────────────────────────────────────────
     * Recreating someone else’s view of the world, but with better data types
     * ───────────────────────────────────────────────────────
     */
    class AccountRepoRepackaged {
        private DatabaseLib databaseLib;

        // This fixes the types, but we’re still just repackaging how they
        // interact with the database
        public void transact(List<Account> updates, List<Account> inserts) {
            databaseLib.transact(/*convert to table records*/);
        }
    }
    // Elsewhere...
    void example(AccountRepoRepackaged accountRepo, List<Account> accounts) {
        accountRepo.transact(                                    //  ┐
            accounts.stream()                                    //  │
                .filter(account -> /*is an udpate? */__)         //  │
                .toList(),                                       //  │◄── To interact with this code, we still
            accounts.stream()                                    //  │    have to do the grunt work of figuring out
                .filter(account -> /*is an insert?*/__)          //  │    what goes where
                .toList());                                      //  ┘
    }




















    static boolean __;

    record Account(String id){}
    interface DatabaseLib {
        void transact();
    }
}
