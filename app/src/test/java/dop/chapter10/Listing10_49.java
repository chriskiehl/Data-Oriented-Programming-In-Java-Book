package dop.chapter10;

import java.util.List;

public class Listing10_49 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.49
     * ───────────────────────────────────────────────────────
     * Exposing the intermediate database types
     * ───────────────────────────────────────────────────────
     */
    private DatabaseLibrary db;

    // Here's one way we might go about it.
    // We could take inspiration from patterns like Builder
    // and expose intermediate "in progress" versions that
    // allow us to incrementally build larger requests
    void ingest(Manifest manifest) {
        List<Account> accounts = fileRepo.loadAccounts(manifest.account);
        List<Account> invoices = fileRepo.loadInvoices(manifest.invoices);
        List<Disputes> disputes = fileRepo.loadDisputes(manifest.disputes);

        UpdatesAndInserts allUpserts =
            accountRepo.stageUpsert(accounts)          //  ┐
            .add(invoiceRepo.stageUpsert(invoices))    //  │◄── creating a “staging” method that
            .add(disputeRepo.stageUpsert(disputes));   //  ┘    exposes the raw Updates and Inserts
                                                       //       so that they can be combined

        //Then the different types could be handled in one atomic write.
        db.transact(allInserts, allUpdates);
    }














    FileRepository fileRepo;
    AccountRepository accountRepo;
    InvoiceRepository invoiceRepo;
    DisputeRepository disputeRepo;
    Object allInserts;
    Object allUpdates;

    record ResourceName(String value) {}
    record Manifest(ResourceName account, ResourceName invoices, ResourceName disputes){}
    record Account(String id){}
    record Disputes(String id){}
    record UpdatesAndInserts() {
        UpdatesAndInserts add(UpdatesAndInserts other) {
            return this;
        }
    }
    interface FileRepository {
        List<Account> loadAccounts(ResourceName name);
        List<Account> loadInvoices(ResourceName name);
        List<Disputes> loadDisputes(ResourceName name);
    }
    interface AccountRepository {
        UpdatesAndInserts stageUpsert(List<Account> accounts);
    }
    interface InvoiceRepository {
        UpdatesAndInserts stageUpsert(List<Account> invoices);
    }
    interface DisputeRepository {
        UpdatesAndInserts stageUpsert(List<Disputes> disputes);
    }
    interface DatabaseLibrary {
        void transact(Object inserts, Object updates);
    }
}
