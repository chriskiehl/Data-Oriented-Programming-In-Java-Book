package dop.chapter06;

public class Listing6_58 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.58
     * ───────────────────────────────────────────────────────
     * Using existing machinery by mapping back to its world
     * ───────────────────────────────────────────────────────
     */
    private void save(LateFee<? extends Lifecycle> latefee) {
        invoiceRepo.save(toInvoice(latefee));
//      ▲
//      └──── Aside from the quick transformation from LateFee → Invoice,
//            it looks like business as usual
        if (latefee.state() instanceof InReview review) {
            customerRepo.save(new Customer(
                latefee.customer.id().value(),
                latefee.customer.address,
                review.id()
            ));
        }
    }















    InvoiceRepo invoiceRepo;
    CustomerRepo customerRepo;
    Invoice toInvoice(LateFee<? extends Lifecycle> latefee) { return null; }
    Customer toCustomer(LateFee<? extends Lifecycle> latefee) { return null; }
    interface InvoiceRepo { void save(Invoice invoice); }
    interface CustomerRepo { void save(Customer customer); }
    record Invoice() {}
    record Address(){}
    record CustomerId(String value){}
    record Customer(String id, Address address, String approvalId) {}
    record EnhancedCustomer(CustomerId id, Address address){}
    sealed interface Lifecycle {}
    record ApprovalId(){}
    record InReview(String id) implements Lifecycle {}
    record LateFee<State extends Lifecycle>(State state, EnhancedCustomer customer) {}
}
