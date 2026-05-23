package dop.chapter10;

import java.util.List;

public class Listing10_32 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.32
     * ───────────────────────────────────────────────────────
     * Where are you, SOLID?
     * ───────────────────────────────────────────────────────
     */
    class FileRepository {
        // Scoff! Look at this. The work of an amateur!
        // "Good" developers know to abstract and DRY repeated code like this.
        public List<Account> loadAccounts(ResourceName name) {return __;}
        public List<Invoice> loadInvoices(ResourceName name) {return ___;}
        public List<Dispute> loadDisputes(ResourceName name) {return ____;}
    }














    record ResourceName(String value) {}
    record Account(String id){}
    record Invoice(String id){}
    record Dispute(String id){}
    static List<Account> __;
    static List<Invoice> ___;
    static List<Dispute> ____;
}
