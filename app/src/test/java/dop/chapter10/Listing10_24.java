package dop.chapter10;

import java.util.List;

public class Listing10_24 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.24
     * ───────────────────────────────────────────────────────
     * Something like…
     * ───────────────────────────────────────────────────────
     */
    class FileRepository {         //  ┐
        private CloudStore store;  //  ┘◄── The design so far is a wrapper around the CloudStore

        public List<Account> loadAccounts(/*...*/) {return __;}     //  ┐
        public List<Invoice> loadInvoices(/*...*/) {return ___;}    //  │◄── We haven’t designed its API yet,
        public List<Dispute> loadDisputes(/*...*/) {return ____;}   //  ┘    but we do know what it’s expected
                                                                    //       to do: vend data in the shape
                                                                    //       our program demands.

    }














    interface CloudStore {}
    record Account(){}
    record Invoice(){}
    record Dispute(){}

    static List<Account> __;
    static List<Invoice> ___;
    static List<Dispute> ____;
}
