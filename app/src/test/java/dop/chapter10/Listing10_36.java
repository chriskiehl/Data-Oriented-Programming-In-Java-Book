package dop.chapter10;

import java.util.List;

public class Listing10_36 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.36
     * ───────────────────────────────────────────────────────
     * Shield your eyes! AHHHH!
     * ───────────────────────────────────────────────────────
     */
    class Ingestion {
        FileRepository fileRepo;

        void ingest(Manifest manifest) {
            List<Account> accounts = fileRepo.loadAccounts(manifest.account);
            List<Invoice> invoices = fileRepo.loadInvoices(manifest.invoices);
            List<Disputes> disputes = fileRepo.loadDisputes(manifest.disputes);
            // THE HORROR
            List<Cash> cash = fileRepo.loadCash(manifest.cash);
//                     ▲
//                     └──── Another line!

        }
    }














    record ResourceName(String value) {}
    record Manifest(ResourceName account, ResourceName invoices, ResourceName disputes, ResourceName cash){}
    record Account(String id){}
    record Invoice(String id){}
    record Disputes(String id){}
    record Cash(String id){}
    interface FileRepository {
        List<Account> loadAccounts(ResourceName name);
        List<Invoice> loadInvoices(ResourceName name);
        List<Disputes> loadDisputes(ResourceName name);
        List<Cash> loadCash(ResourceName name);
    }
}
