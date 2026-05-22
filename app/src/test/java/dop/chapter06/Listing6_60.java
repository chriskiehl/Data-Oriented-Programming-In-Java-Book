package dop.chapter06;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.stream.Stream;

public class Listing6_60 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.60
     * ───────────────────────────────────────────────────────
     * Our feature in all of its semantic glory
     * ───────────────────────────────────────────────────────
     */
    static class LateFeeChargingService {
      private ApprovalsAPI approvalsApi;
      private BillingAPI billingApi;
      private CustomerRepo customerRepo;
      private InvoiceRepo invoiceRepo;
      private CustomerStorageFacade customerFacade;
      private RulesRepo rulesRepo;

      public void processLatefees() {
        loadInvoicingData().forEach(data -> {
          LocalDate today = data.currentDate();
          EnrichedCustomer customer = data.customer();
          List<Invoice> invoices = data.invoices();
          List<PastDue> pastDue = collectPastDue(customer, today, invoices);
          LateFee<Draft> draft = buildDraft(today, customer, pastDue);
          ReviewedFee reviewed = assessDraft(data.rules(), draft);
          LateFee<? extends Lifecycle> latefee = switch (reviewed) {
            case Billable b -> this.submitBill(b);
            case NeedsApproval a -> this.startApproval(a);
            case NotBillable nb -> nb.latefee().markNotBilled(nb.reason());
          };
          this.save(latefee);
        });
      }

      public LateFee<? extends Lifecycle> submitBill(Billable billable) {
        BillingResponse response = this.billingApi.submit(/*...*/);
        return switch (response.status()) {
          case ACCEPTED ->
            billable.latefee().markBilled(new InvoiceId(response.invoiceId()));
          case REJECTED ->
            billable.latefee().markNotBilled(new Reason(response.error()));
        };
      }

      public LateFee<InReview> startApproval(NeedsApproval needsApproval) {
        Approval approval = this.approvalsApi.createApproval(/*...*/);
        return needsApproval.latefee().markAsBeingReviewed(approval.id());
      }

      @Transaction
      private void save(LateFee<? extends Lifecycle> latefee) {
        invoiceRepo.save(toInvoice(latefee));
        if (latefee.state() instanceof InReview approval) {
            customerRepo.save(new Customer(
                latefee.customer.id().value(),
                latefee.customer.address,
                approval.id()
            ));
        }
      }

      Stream<InvoicingData> loadInvoicingData() {
        LocalDate today = LocalDate.now();
        Rules rules = rulesRepo.loadDefaults();
        return customerFacade.findAll().map(customer ->
          new InvoicingData(
              today,
              customer,
              invoiceRepo.findInvoices(customer.id().value()),
              rules
          )
        );
      }

      static Invoice toInvoice(LateFee<? extends Lifecycle> latefee) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(switch (latefee.state()) {
            case Billed(var id) -> id.value();
            default -> Invoice.tempId();
        });
        invoice.setCustomerId(latefee.customer().id().value());
        invoice.setLineItems(List.of(new LineItem(
                null,
                "Late Fee",
                latefee.total().value(),
                Currency.getInstance("USD")
        )));
        invoice.setStatus(InvoiceStatus.OPEN);
        invoice.setInvoiceDate(latefee.invoiceDate());
        invoice.setDueDate(latefee.dueDate());
        invoice.setInvoiceType(InvoiceType.LATEFEE);
        invoice.setAuditInfo(new AuditInfo(
            null,
            latefee.includedInFee().stream().map(PastDue::invoice).toList(),
            switch(latefee.state()) {
                case Rejected(var why) -> why.value();
                case InReview(String approvalId) -> "...";
                default -> null;
            }
        ));
        return invoice;
      }
    }















    interface ApprovalsAPI { Approval createApproval( ); }
    interface BillingAPI { BillingResponse submit( ); }
    interface CustomerRepo { void save(Customer customer); }
    interface InvoiceRepo { void save(Invoice invoice); List<Invoice> findInvoices(String customerId); }
    interface CustomerStorageFacade { Stream<EnrichedCustomer> findAll(); }
    interface RulesRepo { Rules loadDefaults(); }
    @interface Transaction {}
    record InvoicingData(LocalDate currentDate, EnrichedCustomer customer, List<Invoice> invoices, Rules rules) {}
    record Rules() {}
    sealed interface Lifecycle {}
    record Draft() implements Lifecycle {}
    record Billed(InvoiceId id) implements Lifecycle {}
    record Rejected(Reason reason) implements Lifecycle {}
    record InReview(String id) implements Lifecycle {}
    record InvoiceId(String value) {}
    record Reason(String value) {}
    record CustomerId(String value) {}
    record ApprovalId(String value) {}
    record Customer(String id, Address address, String approvalId){}
    record EnrichedCustomer(CustomerId id, Address address) {}
    record Address(String value) {}
    record USD(BigDecimal value) {}
    record PastDue(Invoice invoice) {}
    record LateFee<State extends Lifecycle>(
            State state,
            EnrichedCustomer customer,
            USD total,
            LocalDate invoiceDate,
            LocalDate dueDate,
            List<PastDue> includedInFee
    ) {
        LateFee<Billed> markBilled(InvoiceId id) { return null; }
        LateFee<Rejected> markNotBilled(Reason reason) { return null; }
        LateFee<InReview> markAsBeingReviewed(String approvalId) { return null; }
    }
    sealed interface ReviewedFee {}
    record Billable(LateFee<Draft> latefee) implements ReviewedFee {}
    record NeedsApproval(LateFee<Draft> latefee) implements ReviewedFee {}
    record NotBillable(LateFee<Draft> latefee, Reason reason) implements ReviewedFee {}
    enum BillingStatus {ACCEPTED, REJECTED}
    record BillingResponse(BillingStatus status, String invoiceId, String error) {}
    record Approval(String id) {}
    static class Invoice {
        void setInvoiceId(String id) {}
        void setCustomerId(String id) {}
        void setLineItems(List<LineItem> lineItems) {}
        void setStatus(InvoiceStatus status) {}
        void setInvoiceDate(LocalDate date) {}
        void setDueDate(LocalDate date) {}
        void setInvoiceType(InvoiceType type) {}
        void setAuditInfo(AuditInfo auditInfo) {}
        static String tempId() { return null; }
    }
    record LineItem(Object id, String description, BigDecimal charges, Currency currency) {}
    enum InvoiceStatus {OPEN}
    enum InvoiceType {LATEFEE}
    record AuditInfo(Object id, List<Invoice> includedInFee, String cannotBillReason) {}
    static List<PastDue> collectPastDue(EnrichedCustomer customer, LocalDate today, List<Invoice> invoices) { return null; }
    static LateFee<Draft> buildDraft(LocalDate today, EnrichedCustomer customer, List<PastDue> pastDue) { return null; }
    static ReviewedFee assessDraft(Rules rules, LateFee<Draft> draft) { return null; }
}
