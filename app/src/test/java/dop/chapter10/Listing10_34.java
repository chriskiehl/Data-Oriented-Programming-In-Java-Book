package dop.chapter10;

import java.util.List;

public class Listing10_34 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.34
     * ───────────────────────────────────────────────────────
     * Well typed. Extensible. Flexible. What’s not to love?
     * ───────────────────────────────────────────────────────
     */
    void example(Manifest manifest) {
        List<Account> accounts = fileRepo.load(manifest.account, AccountParser::parse);
        List<Invoice> invoice = fileRepo.load(manifest.invoices, InvoiceParser::parse);
        List<Dispute> dispute = fileRepo.load(manifest.invoices, DisputeParser::parse);
    }














    FileRepository fileRepo;
    record ResourceName(String value) {}
    record Manifest(ResourceName account, ResourceName invoices){}
    record Account(String id){}
    record Invoice(String id){}
    record Dispute(String id){}
    interface FileRepository {
        <A> List<A> load(ResourceName name, java.util.function.Function<String[], A> parser);
    }
    static class AccountParser {
        static Account parse(String[] row) { return new Account(""); }
    }
    static class InvoiceParser {
        static Invoice parse(String[] row) { return new Invoice(""); }
    }
    static class DisputeParser {
        static Dispute parse(String[] row) { return new Dispute(""); }
    }
}
