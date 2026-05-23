package dop.chapter10;

import java.util.List;

public class Listing10_47 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.47
     * ───────────────────────────────────────────────────────
     * Excellent boundaries. Broken code.
     * ───────────────────────────────────────────────────────
     */
    class Ingestion {
        FileRepository fileRepo;
        AccountRepository accountRepo;
        DisputeRepository disputeRepo;
        InvoiceRepository invoiceRepo;

        void ingest(Manifest manifest) {
            List<Account> accounts = fileRepo.loadAccounts(manifest.account);
            List<Account> invoices = fileRepo.loadInvoices(manifest.invoices);
            List<Disputes> disputes = fileRepo.loadDisputes(manifest.disputes);
            accountRepo.upsert(accounts);         //  ┐
            invoiceRepo.upsert(invoices);         //  │◄── These are still subject to failure
            disputeRepo.upsert(disputes);         //  ┘
        }
    }














    record ResourceName(String value) {}
    record Manifest(ResourceName account, ResourceName invoices, ResourceName disputes){}
    record Account(String id){}
    record Disputes(String id){}
    interface FileRepository {
        List<Account> loadAccounts(ResourceName name);
        List<Account> loadInvoices(ResourceName name);
        List<Disputes> loadDisputes(ResourceName name);
    }
    interface AccountRepository {
        void upsert(List<Account> accounts);
    }
    interface InvoiceRepository {
        void upsert(List<Account> invoices);
    }
    interface DisputeRepository {
        void upsert(List<Disputes> disputes);
    }
}
