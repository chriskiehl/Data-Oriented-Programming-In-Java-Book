package dop.chapter10;

import java.util.List;

public class Listing10_52 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.52
     * ───────────────────────────────────────────────────────
     * Excellent boundaries and correct code
     * ───────────────────────────────────────────────────────
     */
    class Ingestion {
        FileRepository fileRepo;
        Transactor transactor;
        AccountRepository accountRepo;
        DisputeRepository disputeRepo;
        InvoiceRepository invoiceRepo;

        void ingest(Manifest manifest) {
            List<Account> accounts = fileRepo.loadAccounts(manifest.account);
            List<Account> invoices = fileRepo.loadInvoices(manifest.invoices);
            List<Disputes> disputes = fileRepo.loadDisputes(manifest.disputes);
            transactor.transact((tx) -> {         //  ┐
                accountRepo.upsert(tx, accounts); //  │
                invoiceRepo.upsert(tx, invoices); //  │◄── The transactor solves the problem
                disputeRepo.upsert(tx, disputes); //  │    without leaking details about Inserts
            });                                   //  ┘    or Updates. To our program, it’s just
                                                  //       another “dependency we use.”
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
    interface Transactor {
        void transact(java.util.function.Consumer<Transaction> f);
    }
    interface Transaction {}
    interface AccountRepository {
        void upsert(Transaction tx, List<Account> accounts);
    }
    interface InvoiceRepository {
        void upsert(Transaction tx, List<Account> invoices);
    }
    interface DisputeRepository {
        void upsert(Transaction tx, List<Disputes> disputes);
    }
}
