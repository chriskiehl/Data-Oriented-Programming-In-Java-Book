package dop.chapter06;

import java.time.LocalDate;
import java.util.List;

public class Listing6_59 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.59
   * ───────────────────────────────────────────────────────
   * Potential failure points
   * ───────────────────────────────────────────────────────
   */
  public void processLatefees() {
    loadInvoicingData().forEach(data -> {
      LocalDate today = data.currentDate();
      EnrichedCustomer customer = data.customer();
      List<Invoice> invoices = data.invoices();
      List<PastDue> pastDue = collectPastDue(customer, today, invoices);
      LateFee<Draft> draft = buildDraft(today, customer, pastDue);
      ReviewedFee reviewed = assessDraft(data.rules(), draft);
      LateFee<? extends Lifecycle> latefee = switch (reviewed) {        //  ┐
        case Billable b -> this.submitBill(b);                          //  │◄── What happens if any
        case NeedsApproval a -> this.startApproval(a);                  //  │    of these fail?
        case NotBillable nb -> nb.latefee().markNotBilled(nb.reason()); //  │
      };                                                                //  │
      this.save(latefee);                                               //  ┘
    });
  }








  List<InvoicingData> loadInvoicingData() { return null; }
  List<PastDue> collectPastDue(EnrichedCustomer customer, LocalDate today, List<Invoice> invoices) { return null; }
  LateFee<Draft> buildDraft(LocalDate today, EnrichedCustomer customer, List<PastDue> pastDue) { return null; }
  ReviewedFee assessDraft(Rules rules, LateFee<Draft> draft) { return null; }
  LateFee<? extends Lifecycle> submitBill(Billable billable) { return null; }
  LateFee<? extends Lifecycle> startApproval(NeedsApproval needsApproval) { return null; }
  void save(LateFee<? extends Lifecycle> latefee) {}
  record InvoicingData(LocalDate currentDate, EnrichedCustomer customer, List<Invoice> invoices, Rules rules) {}
  record EnrichedCustomer() {}
  record Invoice() {}
  record PastDue() {}
  record Rules() {}
  sealed interface Lifecycle {}
  record Draft() implements Lifecycle {}
  record Rejected(Reason reason) implements Lifecycle {}
  record Reason(String value) {}
  record LateFee<State extends Lifecycle>() {
    LateFee<Rejected> markNotBilled(Reason reason) { return null; }
  }
  sealed interface ReviewedFee {}
  record Billable() implements ReviewedFee {}
  record NeedsApproval() implements ReviewedFee {}
  record NotBillable(LateFee<Draft> latefee, Reason reason) implements ReviewedFee {}

}
