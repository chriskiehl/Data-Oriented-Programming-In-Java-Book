package dop.chapter10;

import java.util.List;

public class Listing10_38 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.38
     * ───────────────────────────────────────────────────────
     * The indignity!
     * ───────────────────────────────────────────────────────
     */
    void ingest(Manifest manifest) {
        List<Account> accounts = fileRepo.loadAccounts(manifest.account);
        List<Account> invoices = fileRepo.loadInvoices(manifest.invoices);
        List<Disputes> disputes = fileRepo.loadDisputes(manifest.disputes);
        List<Cash> cash = fileRepo.loadCash(manifest.cash);
        List<Seller> sellers = fileRepo.loadSellers(manifest.sellers);
//                   ▲
//                   └──── And yet another line!
    }














    FileRepository fileRepo;
    record ResourceName(String value) {}
    record Manifest(
        ResourceName account,
        ResourceName invoices,
        ResourceName disputes,
        ResourceName cash,
        ResourceName sellers
    ){}
    record Account(String id){}
    record Disputes(String id){}
    record Cash(String id){}
    record Seller(String id){}
    interface FileRepository {
        List<Account> loadAccounts(ResourceName name);
        List<Account> loadInvoices(ResourceName name);
        List<Disputes> loadDisputes(ResourceName name);
        List<Cash> loadCash(ResourceName name);
        List<Seller> loadSellers(ResourceName name);
    }
}
