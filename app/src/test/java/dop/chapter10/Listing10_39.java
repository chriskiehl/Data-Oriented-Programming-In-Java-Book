package dop.chapter10;

import java.util.List;

public class Listing10_39 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.39
     * ───────────────────────────────────────────────────────
     * Sketching out the orchestration in the application tier
     * ───────────────────────────────────────────────────────
     */
    class FileRepository {
        public List<Account> loadAccounts(ResourceName name) {return __;}    //  ┐
        public List<Invoice> loadInvoices(ResourceName name) {return ___;}    //  │◄── Boring methods with no
        public List<Dispute> loadDisputes(ResourceName name) {return ____;}   //  ┘    fancy abstractions
    }

    class Ingestion {
        FileRepository fileRepo;

        void ingest(Manifest manifest) {
            List<Account> accounts = fileRepo.loadAccounts(manifest.account);   //  ┐
            List<Invoice> invoices = fileRepo.loadInvoices(manifest.invoices);  //  │  ◄─ Boring calls to those boring
            List<Dispute> disputes = fileRepo.loadDisputes(manifest.disputes);  //  ┘     methods with no fancy abstractions
        }
    }














    record ResourceName(String value) {}
    record Manifest(ResourceName account, ResourceName invoices, ResourceName disputes){}
    record Account(String id){}
    record Invoice(String id){}
    record Dispute(String id){}
    record Disputes(String id){}
    static List<Account> __;
    static List<Invoice> ___;
    static List<Dispute> ____;
}
