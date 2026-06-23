package dop.chapter06;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

public class Listing6_57 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 6.57
   * ───────────────────────────────────────────────────────
   * Deterministically mapping between worlds
   * ───────────────────────────────────────────────────────
   */
  static Invoice toInvoice(LateFee<? extends Lifecycle> latefee) {
//                 ▲
//                 └──── We define it as a static to cue that it’s a function
    Invoice invoice = new Invoice();
    invoice.setInvoiceId(switch (latefee.state()) {
//                         ▲
//                         └──── We pattern match where needed
      case Billed(var id) -> id.value();
      default -> Invoice.tempId();
//                 ▲
//                 └──── This bit of weirdness was defined in chapter 5.
//                       We don’t have "real" Ids until successfully billed
    });
    invoice.setCustomerId(latefee.customer().id().value());
    invoice.setLineItems(List.of(new LineItem(
        null,
        "Late Fee",
        latefee.total().value(),
        Currency.getInstance("USD")
    )));
    invoice.setStatus(InvoiceStatus.OPEN);                         //  ┐
    invoice.setInvoiceDate(latefee.invoiceDate());                 //  │◄── Aside from the occasional
    invoice.setDueDate(latefee.dueDate());                         //  │    pattern match, it’s just some
    invoice.setInvoiceType(InvoiceType.LATEFEE);                   //  ┘    ho-hum copying between data types.
    invoice.setAuditInfo(new AuditInfo(
      null,
      latefee.includedInFee().stream().map(PastDue::invoice).toList(),
      switch(latefee.state()) {
        case Rejected(var why) -> why.value();
        case InReview(String approvalId) -> approvalId;
        default -> null;
      }
    ));
    return invoice;
  }








  sealed interface Lifecycle {}
  record Billed(InvoiceId id) implements Lifecycle {}
  record Rejected(Reason why) implements Lifecycle {}
  record InReview(String approvalId) implements Lifecycle {}
  record InvoiceId(String value) {}
  record Reason(String value) {}
  record Customer(CustomerId id) {}
  record CustomerId(String value) {}
  record USD(BigDecimal value) {}
  record PastDue(Invoice invoice) {}
  record LateFee<State extends Lifecycle>(
      State state,
      Customer customer,
      USD total,
      LocalDate invoiceDate,
      LocalDate dueDate,
      List<PastDue> includedInFee
  ) {}
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

}
