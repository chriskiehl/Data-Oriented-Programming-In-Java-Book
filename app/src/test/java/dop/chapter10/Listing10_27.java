package dop.chapter10;

import java.util.List;

public class Listing10_27 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.27
     * ───────────────────────────────────────────────────────
     * Forcing the external world to speak ResourcePath rather than String
     * ───────────────────────────────────────────────────────
     */
    class FileRepository {
        private CloudStore store;

        public List<Account> loadAccounts(ResourcePath path){return __;}    //  ┐
        public List<Invoice> loadInvoices(ResourcePath path){return ___;}   //  │◄── Now we can program with the
        public List<Dispute> loadDisputes(ResourcePath path){return ____;}  //  ┘    types we’ve designed!
    }














    interface CloudStore {}
    record ResourcePath(String value) {}
    record Account(){}
    record Invoice(){}
    record Dispute(){}
    static List<Account> __;
    static List<Invoice> ___;
    static List<Dispute> ____;
}
