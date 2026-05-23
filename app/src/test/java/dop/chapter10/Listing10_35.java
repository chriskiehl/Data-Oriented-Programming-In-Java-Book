package dop.chapter10;

import java.util.List;

public class Listing10_35 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.35
     * ───────────────────────────────────────────────────────
     * The work of someone who isn’t very SOLID
     * ───────────────────────────────────────────────────────
     */
    class FileRepository {
        public List<Account> loadAccounts(ResourceName name) {return __;}
        public List<Invoice> loadInvoice(ResourceName name) {return ___;}
        public List<Dispute> loadDispute(ResourceName name) {return ____;}
    }














    record ResourceName(String value) {}
    record Account(String id){}
    record Invoice(String id){}
    record Dispute(String id){}
    static List<Account> __;
    static List<Invoice> ___;
    static List<Dispute> ____;
}
