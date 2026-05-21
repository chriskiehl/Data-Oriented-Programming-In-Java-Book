package dop.chapter06.the.implementation;

import dop.chapter05.the.existing.world.Entities.*;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.ApprovalStatus;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;
import dop.chapter06.the.implementation.Types.*;
import dop.chapter06.the.implementation.Types.Lifecycle.Draft;
import dop.chapter06.the.implementation.Types.ReviewedFee.Billable;
import dop.chapter06.the.implementation.Types.ReviewedFee.NeedsApproval;
import dop.chapter06.the.implementation.Types.ReviewedFee.NotBillable;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static dop.chapter05.the.existing.world.Entities.InvoiceStatus.OPEN;
import static dop.chapter05.the.existing.world.Entities.InvoiceType.STANDARD;
import static java.time.temporal.ChronoUnit.DAYS;

public class Core {


    static LateFee<Draft> buildDraft(LocalDate today, EnrichedCustomer customer, List<PastDue> invoices) {
        return new LateFee<>(
                new Draft(),
                customer,
                computeFee(invoices, customer.feePercentage()),
                today,
                dueDate(today, customer.terms()),
                invoices
        );
    }

    static USD computeFee(List<PastDue> pastDue, Percent percentage) {
        return computeTotal(pastDue).multiply(percentage.decimalValue());
    }


    static USD computeTotal(List<PastDue> invoices) {
        return invoices.stream().map(PastDue::invoice)
                .flatMap(x -> x.getLineItems().stream())
                .map(Core::unsafeGetChargesInUSD)
                .reduce(USD.zero(), USD::add);
    }

    static USD unsafeGetChargesInUSD(LineItem lineItem) throws IllegalArgumentException {
        if (!lineItem.getCurrency().getCurrencyCode().equals("USD")) {
            throw new IllegalArgumentException("Kaboom");
        } else {
            return new USD(lineItem.getCharges());
        }
    }

    static LocalDate dueDate(LocalDate today, PaymentTerms terms) {
        return switch (terms) {
            case PaymentTerms.NET_30 -> today.plusDays(30);
            case PaymentTerms.NET_60 -> today.plusDays(60);
            case PaymentTerms.DUE_ON_RECEIPT -> today;
            case PaymentTerms.END_OF_MONTH -> today.with(TemporalAdjusters.lastDayOfMonth());
        };
    }

    public static ReviewedFee assessDraft(Rules rules, LateFee<Draft> draft) {
        return switch (assessTotal(rules, draft.total())) {
            case Assessment.WITHIN_RANGE -> new Billable(draft);
            case Assessment.BELOW_MINIMUM -> new NotBillable(draft, new Reason("Below threshold"));
            case Assessment.ABOVE_MAXIMUM -> draft.customer().approval().isEmpty()
                ? new NeedsApproval(draft)
                : switch (draft.customer().approval().get().status()) {
                    case ApprovalStatus.APPROVED -> new Billable(draft);
                    case ApprovalStatus.PENDING -> new NotBillable(draft, new Reason("Pending decision"));
                    case ApprovalStatus.DENIED -> new NotBillable(draft, new Reason("exempt from large fees"));
                };
        };
    }


    private enum Assessment {ABOVE_MAXIMUM, BELOW_MINIMUM, WITHIN_RANGE}

    static Assessment assessTotal(Rules rules, USD total) {
        if (total.value().compareTo(rules.getMinimumFeeThreshold()) < 0) {
            return Assessment.BELOW_MINIMUM;
        } else if (total.value().compareTo(rules.getMaximumFeeThreshold()) > 0) {
            return Assessment.ABOVE_MAXIMUM;
        } else {
            return Assessment.WITHIN_RANGE;
        }
    }

    public static List<PastDue> collectPastDue(EnrichedCustomer customer, LocalDate today, List<Invoice> invoices) {
        return invoices.stream()
                .filter(invoice -> isPastDue(invoice, customer.rating(), today))
                .map(PastDue::new) // Capturing what we know as this new type (and quarantining its mutability)
                // we're now "out" of the existing world and into ours.
                .toList();
    }

    static boolean isPastDue(Invoice invoice, CustomerRating rating, LocalDate today) {
        return invoice.getInvoiceType().equals(STANDARD)
            && invoice.getStatus().equals(OPEN)
            && today.isAfter(invoice.getDueDate().with(gracePeriod(rating)));
    }

    static TemporalAdjuster gracePeriod(CustomerRating rating) {
        return switch (rating) {
            case CustomerRating.GOOD -> date -> date.plus(60, DAYS);
            case CustomerRating.ACCEPTABLE -> date -> date.plus(30, DAYS);
            case CustomerRating.POOR -> TemporalAdjusters.lastDayOfMonth();
        };
    }
}
