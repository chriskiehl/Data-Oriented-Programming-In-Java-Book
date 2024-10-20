package dop.invoicing;

import dop.invoicing.DataTypes.*;
import dop.invoicing.DataTypes.ReviewedLatefee.Billable;
import dop.invoicing.DataTypes.ReviewedLatefee.HeldForReview;
import dop.invoicing.DataTypes.ReviewedLatefee.NotBillable;

import java.time.LocalDate;
import java.util.List;

import static dop.invoicing.DataTypes.BillingState.*;

public class Core2 {


    public static ReviewedLatefee generateFee(Customer customer, List<Entities.Invoice> invoices, LocalDate asOf, Entities.Config config) {
        List<Entities.Invoice> pastDue = collectPastDue(customer, invoices, asOf);
        Latefee<Pending> draft = buildLatefee(customer, pastDue, asOf);
        return assessDraft(customer, draft, config);
    }


    public static Latefee<Pending> buildLatefee(Customer customer, List<Entities.Invoice> invoices, LocalDate asOf) {
        return new Latefee<>(
                customer.id(),
                invoices.stream().map(Entities.Invoice::getCharges).reduce(Entities.USD.zero(), Entities.USD::add),
                new Pending(),
                asOf,
                invoices);
    }

    public static ReviewedLatefee assessDraft(Customer customer, Latefee<Pending> draft, Entities.Config config) {
        if (draft.total().compareTo(config.minimumFeeThreshold()) <= 0) {
            return new NotBillable(draft, "too little to charge");
        } else if (draft.total().compareTo(config.maximumFeeThreshold()) >= 0) {
            return switch (customer.status()) {
                case ApprovalStatus.ApprovedForLargeFee -> new Billable(draft);
                case ApprovalStatus.Exempt -> new NotBillable(draft, "special exception");
                case ApprovalStatus.Unknown -> new HeldForReview(draft, "Cause");
            };
        } else {
            return new Billable(draft);
        }
    }

    public static List<Entities.Invoice> collectPastDue(Customer customer, List<Entities.Invoice> invoices, LocalDate asOfDate) {
        return invoices.stream()
                .filter(invoice -> invoice.getStatus().equals(Entities.InvoiceStatus.OPEN))
                .filter(invoice -> isPastDue(invoice, customer, asOfDate))
                .toList();
    }

    public static boolean isPastDue(Entities.Invoice invoice, Customer customer, LocalDate asOf) {
        return invoice.getDueDate().plusDays(gracePeriod(customer).value()).isBefore(asOf);
    }

    public static NonNegativeInt gracePeriod(Customer customer) {
        return switch (customer.standing()) {
            case CustomerStanding.Good -> new NonNegativeInt(60);
            case CustomerStanding.Acceptable -> new NonNegativeInt(30);
            case CustomerStanding.Poor -> new NonNegativeInt(0);
        };
    }
}
