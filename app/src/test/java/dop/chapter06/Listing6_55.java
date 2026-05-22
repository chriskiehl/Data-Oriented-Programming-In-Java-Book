package dop.chapter06;

import java.util.List;

public class Listing6_55 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.55
     * ───────────────────────────────────────────────────────
     * We just do whatever the type system tells us
     * ───────────────────────────────────────────────────────
     */
    public void processLatefees() {
      loadInvoicingData().forEach(data -> {
        /* ... */
        ReviewedFee reviewed = Core.assessDraft(data.rules(), draft);
//                     ▲
//                     └──── ReviewedFee is a sum type, so the only
//                           thing we can do next is pattern match on it
        LateFee<? extends Lifecycle> latefee = switch(reviewed) {         //  ┐
          case Billable b -> this.submitBill(b);                          //  │◄── What do we do here? Just follow
          case NeedsApproval a -> this.startApproval(a);                  //  │    the types! They tell us what
          case NotBillable nb -> nb.latefee().markNotBilled(nb.reason()); //  ┘    cases to handle
        };
        this.save(latefee);
      });
    }















    List<InvoicingData> loadInvoicingData() { return null; }
    LateFee<? extends Lifecycle> submitBill(Billable billable) { return null; }
    LateFee<? extends Lifecycle> startApproval(NeedsApproval needsApproval) { return null; }
    void save(LateFee<? extends Lifecycle> latefee) {}
    record InvoicingData(Rules rules) {}
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
    static class Core {
        static ReviewedFee assessDraft(Rules rules, LateFee<Draft> draft) { return null; }
    }
    LateFee<Draft> draft;
}
