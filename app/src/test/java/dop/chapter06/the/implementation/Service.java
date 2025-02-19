package dop.chapter06.the.implementation;

import dop.chapter05.the.existing.world.Annotations.Transaction;
import dop.chapter05.the.existing.world.Entities;
import dop.chapter05.the.existing.world.Repositories;
import dop.chapter05.the.existing.world.Services;
import dop.chapter06.the.implementation.Types.*;
import dop.chapter06.the.implementation.Types.Lifecycle.Billed;
import dop.chapter06.the.implementation.Types.Lifecycle.Draft;
import dop.chapter06.the.implementation.Types.Lifecycle.UnderReview;
import dop.chapter06.the.implementation.Types.ReviewedFee.Billable;
import dop.chapter06.the.implementation.Types.ReviewedFee.NeedsApproval;
import dop.chapter06.the.implementation.Types.ReviewedFee.NotBillable;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.stream.Stream;

public class Service {
    private Services.ApprovalsAPI approvalsApi;
    private Services.BillingAPI billingApi;
    private Repositories.CustomerRepo customerRepo;
    private Repositories.InvoiceRepo invoiceRepo;
    private Facade facade;
    private Repositories.RulesRepo rulesRepo;

    record InvoicingData(
            LocalDate currentDate,
            Types.EnrichedCustomer customer,
            List<Entities.Invoice> invoices,
            Entities.Rules rules
    ){}

    public void processLatefees() {
        //  ---- NON-DETERMINISTIC SHELL --------
        loadInvoicingData().forEach(data -> {
            // ---- DETERMINISTIC CORE --------
            LocalDate today = data.currentDate();
            Types.EnrichedCustomer customer = data.customer();
            List<PastDue> pastDue = Core.collectPastDue(customer, today, data.invoices());
            LateFee<Draft> draft = Core.buildDraft(today, customer, pastDue);
            ReviewedFee reviewed = Core.assessDraft(data.rules(), draft);
            // ---- NON-DETERMINISTIC SHELL --------
            LateFee<? extends Lifecycle> latefee = switch (reviewed) {
                case Billable b -> this.submitBill(b);
                case NeedsApproval a -> this.startApproval(a);
                case NotBillable nb -> nb.latefee().markNotBilled(nb.reason());
            };
            this.save(latefee);
        });
    }

    public LateFee<? extends Lifecycle> submitBill(Billable billable) {
        Services.BillingAPI.BillingResponse response = this.billingApi.submit(
                new Services.BillingAPI.SubmitInvoiceRequest(/*ignored*/));
        return switch (response.status()) {
            case ACCEPTED -> billable.latefee().markBilled(new InvoiceId(response.invoiceId()));
            case REJECTED -> billable.latefee().markNotBilled(new Reason(response.error()));
        };
    }

    public LateFee<UnderReview> startApproval(NeedsApproval needsApproval) {
        Services.ApprovalsAPI.Approval approval = this.approvalsApi.createApproval(
                new Services.ApprovalsAPI.CreateApprovalRequest(/*ignored*/));
        return needsApproval.latefee().markAsBeingReviewed(approval.id());
    }


    @Transaction
    private void save(LateFee<? extends Lifecycle> latefee) {
        invoiceRepo.save(toInvoice(latefee));
        if (latefee.state() instanceof UnderReview review) {
            customerRepo.save(toCustomer(latefee.inState(review)));
        }
    }


    Stream<InvoicingData> loadInvoicingData() {
        LocalDate today = LocalDate.now();
        Entities.Rules rules = rulesRepo.loadDefaults();
        return facade.findAll().map(customer ->
            new InvoicingData(
                    today,
                    customer,
                    invoiceRepo.findInvoices(customer.id().value()),
                    rules
            )
        );
    }

    /**
     * Here's the exchange between our two worlds.
     * We have to convert from our domain specific "Late Fee" back
     * to the OMR friendly Invoice.
     */
    static Entities.Invoice toInvoice(LateFee<? extends Lifecycle> latefee) {
        Entities.Invoice invoice = new Entities.Invoice();
        invoice.setInvoiceId(switch (latefee.state()) {
            case Billed(var id) -> id.value();
            default -> Entities.Invoice.tempId();
        });
        invoice.setCustomerId(latefee.customer().id().value());
        invoice.setLineItems(List.of(new Entities.LineItem(
                null,
                "Late Fee",
                latefee.total().value(),
                Currency.getInstance("USD")
        )));
        invoice.setStatus(Entities.InvoiceStatus.OPEN);
        invoice.setInvoiceDate(latefee.invoiceDate());
        invoice.setDueDate(latefee.dueDate());
        invoice.setInvoiceType(Entities.InvoiceType.LATEFEE);
        invoice.setAuditInfo(new Entities.AuditInfo(
                null,
                latefee.includedInFee().stream().map(PastDue::invoice).toList(),
                switch(latefee.state()) {
                    case Lifecycle.Rejected(var why) -> why.value();
                    case UnderReview(String approvalId) -> "...";
                    default -> null;
                }
        ));
        return invoice;
    }

    Entities.Customer toCustomer(LateFee<UnderReview> latefee) {
        return new Entities.Customer(
                latefee.customer().id().value(),
                latefee.customer().address(),
                latefee.state().id()
        );
    }
}
