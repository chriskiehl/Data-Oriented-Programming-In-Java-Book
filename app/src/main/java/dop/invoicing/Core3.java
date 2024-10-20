package dop.invoicing;

import dop.invoicing.DataTypes.BillingState.Pending;
import dop.invoicing.DataTypes.InvoicingData;
import dop.invoicing.DataTypes.Customer;
import dop.invoicing.DataTypes.Latefee;
import dop.invoicing.DataTypes.ReviewedLatefee;
import dop.invoicing.Entities.Invoice;

import java.time.LocalDate;
import java.util.List;

public class Core3 {

    public static ReviewedLatefee generateFee(InvoicingData state) {
        List<Invoice> pastDue = collectPastDue(state);
        Latefee<Pending> draft = buildLatefee(state, pastDue);
        return assessDraft(state, draft);
    }


    public static Latefee<Pending> buildLatefee(InvoicingData info, List<Invoice> pastDueInvoices) {
        Entities.USD totalPastDue = pastDueInvoices.stream()
                .map(Invoice::getCharges)
                .reduce(Entities.USD.zero(), Entities.USD::add);
        Entities.USD charges = totalPastDue.multiply(info.feePercent().value());
        return new Latefee<>(
                info.customer().id(),
                charges,
                new Pending(),
                info.closeDate(),
                pastDueInvoices);
    }

    public static ReviewedLatefee assessDraft(InvoicingData info, Latefee<Pending> draft) {
        if (draft.total().compareTo(info.config().minimumFeeThreshold()) <= 0) {
            return new ReviewedLatefee.NotBillable(draft, "too little to charge");
        } else if (draft.total().compareTo(info.config().maximumFeeThreshold()) >= 0) {
            return switch (info.customer().status()) {
                case DataTypes.ApprovalStatus.ApprovedForLargeFee -> new ReviewedLatefee.Billable(draft);
                case DataTypes.ApprovalStatus.Exempt -> new ReviewedLatefee.NotBillable(draft, "special exception");
                case DataTypes.ApprovalStatus.Unknown -> new ReviewedLatefee.HeldForReview(draft, "Cause");
            };
        } else {
            return new ReviewedLatefee.Billable(draft);
        }
    }

    public static List<Invoice> collectPastDue(InvoicingData info) {
        return info.invoices().stream()
                .filter(invoice -> invoice.getStatus().equals(Entities.InvoiceStatus.OPEN))
                .filter(invoice -> isPastDue(invoice, info.customer(), info.closeDate()))
                .toList();
    }

    public static boolean isPastDue(Invoice invoice, Customer customer, LocalDate closeDate) {
        return invoice.getDueDate().plusDays(gracePeriod(customer).value()).isBefore(closeDate);
    }

    public static DataTypes.NonNegativeInt gracePeriod(Customer customer) {
        return switch (customer.standing()) {
            case DataTypes.CustomerStanding.Good -> new DataTypes.NonNegativeInt(60);
            case DataTypes.CustomerStanding.Acceptable -> new DataTypes.NonNegativeInt(30);
            case DataTypes.CustomerStanding.Poor -> new DataTypes.NonNegativeInt(0);
        };
    }
}
