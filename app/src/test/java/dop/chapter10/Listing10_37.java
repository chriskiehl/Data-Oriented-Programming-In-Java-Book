package dop.chapter10;

import java.util.List;

public class Listing10_37 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.37
     * ───────────────────────────────────────────────────────
     * The humanity!
     * ───────────────────────────────────────────────────────
     */
    class FileRepository {
        public List<Account> loadAccounts(ResourceName name) {return __;}
        public List<Invoice> loadInvoice(ResourceName name) {return ___;}
        public List<Dispute> loadDispute(ResourceName name) {return ____;}
        public List<Cash> loadCash(ResourceName name) {return ______;}
        // ANYTHING BUT THIS
        public List<Seller> loadSellers(ResourceName name) {return _______;}
//                              ▲
//                              └──── And another line!!
    }














    record ResourceName(String value) {}
    record Account(String id){}
    record Invoice(String id){}
    record Dispute(String id){}
    record Cash(String id){}
    record Seller(String id){}
    static List<Account> __;
    static List<Invoice> ___;
    static List<Dispute> ____;
    static List<Cash> ______;
    static List<Seller> _______;
}
