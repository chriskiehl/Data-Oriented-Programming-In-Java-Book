package dop.chapter06;

import java.time.LocalDate;
import java.util.List;

public class Listing6_54 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.54
     * ───────────────────────────────────────────────────────
     * Roughing in the main method
     * ───────────────────────────────────────────────────────
     */
    public void processLatefees() {
// ┌────────────── NON-DETERMINISTIC OUTER SHELL ─────────────────┐
      loadInvoicingData().forEach(data -> {
        //    ▲
        //    └──── We kick off the method by loading all the data we need


//  ┌──────────────────DETERMINISTIC CORE  ──────────────────┐
        LocalDate today = data.currentDate();                              //  │
        EnrichedCustomer customer = data.customer();                       //  │
        List<Invoice> invoices = data.invoices();                          //  │◄── Then we feed that into our
        List<PastDue> pastDue = collectPastDue(customer, today, invoices); //  │    deterministic core. It works with
        LateFee<Draft> draft = buildDraft(today, customer, pastDue);       //  │    the results of that non-deterministic I/O
        ReviewedFee reviewed = assessDraft(data.rules(), draft);           //  │
//  └────────────────────────────────────────────────────────┘

        // ------- NON-DETERMINISTIC OUTER SHELL --------
        // [more work here]
        //      ▲
        //      └──── Then feed those results into the next stage of side-effects and I/O
      });
    }















    List<InvoicingData> loadInvoicingData() { return null; }
    List<PastDue> collectPastDue(EnrichedCustomer customer, LocalDate today, List<Invoice> invoices) { return null; }
    LateFee<Draft> buildDraft(LocalDate today, EnrichedCustomer customer, List<PastDue> pastDue) { return null; }
    ReviewedFee assessDraft(Rules rules, LateFee<Draft> draft) { return null; }
    record InvoicingData(LocalDate currentDate, EnrichedCustomer customer, List<Invoice> invoices, Rules rules) {}
    record EnrichedCustomer() {}
    record Invoice() {}
    record PastDue() {}
    record Draft() {}
    record LateFee<State>() {}
    record Rules() {}
    interface ReviewedFee {}
}
