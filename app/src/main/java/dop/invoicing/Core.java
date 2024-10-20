package dop.invoicing;

import dop.invoicing.Core.BilledStatus.Pending;
import dop.invoicing.Entities.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Core {
    // TODO: [Note to self]
    // There's a meta pattern across all of these actions
    // (if you make the invoices track relevant / irrelevant
    //      1. stuff I include
    //      2. stuff I reject and why
    // Could this be captured as a type? Maybe pattern fodder?

    public record NonNegativeInt(int value) {
        public NonNegativeInt {
            if (value < 0) {
                throw new IllegalStateException("Boo");
            }
        }
    }

    enum ApprovalStatus {Unknown, ApprovedForLargeFee, Exempt}

    public record CustomerV2(
            String id,
            Address billingAddress,
            ApprovalStatus status,
            CustomerStanding standing
    ) {
    }

    public record Justification(
            LocalDate asOfDate,
            List<Invoice> includedInFee,
            List<Invoice> excludedFromFee
    ) {
    }

    // TODO: [Story Arc]
    // We should start with Latefee and then
    // discover that we still have this awkward problem
    // at billing time.
    // ...or do we...?
    sealed interface LatefeeV3 {

        record DraftInvoice(
                String customerId,
                USD total,
                Justification justification
        ) implements LatefeeV3 {
        }

        record LatefeeInvoice(
                String invoiceId,
                String customerId,
                USD total,
                Justification justification
        ) implements LatefeeV3 {}
    }


    sealed interface BilledStatus {
        record Pending() implements BilledStatus {}
        record Billed(String invoiceId) implements BilledStatus {}
    }

    public record LatefeeV5(
            String customerId,
            BilledStatus billed,
            USD total,
            Justification justification
    ) {
    }

    record LatefeeV6<A extends BilledStatus>(
            String customerId,
            USD total,
            BilledStatus billed,
            Justification justification
    ){}

    sealed interface ReviewedLatefeeV2 {
        record Billable(LatefeeV6<Pending> latefee) implements ReviewedLatefeeV2 {}
        record NotBillable(LatefeeV6<Pending> latefee, String whyNot) implements ReviewedLatefeeV2 {}
        record HeldForReview(LatefeeV6<Pending> latefee, String why) implements ReviewedLatefeeV2 {}
    }


    // TODO: [Note]
    // OK. Actually, this is kind of interesting, because
    // we've got the accretion problem. Latefee is a 'subtype'
    // of "draft," but we have no way of expressing that.
    record LatefeeV1(
            Optional<String> invoiceId, // because we don't know it until the ledger tell us
            InvoiceStatus status,
            String customerId,
            USD charges,
            Justification justification
    ) {
        // whoops. Look what we have to do here.
        LatefeeV1 {
            if (status.equals(InvoiceStatus.OPEN)
                    && invoiceId.isEmpty()) {
                throw new RuntimeException("OPEN Invoices MUST have a known Invoice ID");
            }
            // plus the invoice. It cannot be draft
        }
        // This also means that we haven't sovled the billing part.
    }

    sealed interface LatefeeSomething {
        record Draft() implements LatefeeSomething {}
        record Open(String invoiceId) implements LatefeeSomething {}
    }


    record LatefeeV2(
            String customerId,
            USD charges,
            Justification justification
    ) {
    }


    record State(
            USD minThreshold,
            USD maxThreshold
    ) {
    }

    sealed interface ReviewedLatefee {
        record Billable() implements ReviewedLatefee {
        }

        record NotBillable(String whyNot) implements ReviewedLatefee {
        }

        record HeldForReview() implements ReviewedLatefee {
        }
    }

    record CustomerInvoices(
            CustomerV2 customer,
            List<Invoice> invoices
    ) {
    }



    public static void main2(String... args) {
        // validate date
        // get customer
        // get config
        // for each
        //     get invoiecs
        //     doTheStuff(customer, invoices, asOf, config)
        // save()
        // dispatch()
    }

    // OK, what if we didn't let someone else's design dominate how
    // we write our code? We know the data we need.
    // Smart Repositories
    public void asdfadfdas() {

    }




    public static ReviewedLatefee doTheStuff(CustomerV2 customer, List<Invoice> invoices, LocalDate asOf, Config config) {
        // TODO: make sure all invoices belong to same billing country
        // TODO: [Note] talk about letting the shape of services dominate our design.
        return generateFee(customer, invoices, asOf, config);

//        for (ReviewedLatefee fee : fees) {
//            switch (fee) {
//                case ReviewedLatefee.Billable a -> doSomething();
//                case ReviewedLatefee.HeldForReview b -> doSomething();
//                case ReviewedLatefee.NotBillable c -> doSomething();
//            }
//        }
    }

    static void doSomething() {
    }

    public static ReviewedLatefee generateFee(CustomerV2 customer, List<Invoice> invoices, LocalDate asOf, Config config) {
        List<Invoice> pastDue = collectPastDue(customer, invoices, asOf);
        LatefeeV5 draft = buildLatefee(customer, invoices, asOf);
        return assessDraft(customer, draft, config);
    }


    public static LatefeeV5 buildLatefee(CustomerV2 customer, List<Invoice> invoices, LocalDate asOf) {
        return new LatefeeV5(
                customer.id(),
                new Pending(),
                invoices.stream().map(Invoice::getCharges).reduce(USD.zero(), USD::add),
                new Justification(asOf, invoices, List.of())
        );
    }

    public static ReviewedLatefee assessDraft(CustomerV2 customer, LatefeeV5 draft, Config config) {
        if (draft.total().compareTo(config.minimumFeeThreshold()) <= 0) {
            return new ReviewedLatefee.NotBillable("too little to charge");
        } else if (draft.total().compareTo(config.maximumFeeThreshold()) >= 0) {
            return switch (customer.status()) {
                case ApprovalStatus.ApprovedForLargeFee -> new ReviewedLatefee.Billable();
                case ApprovalStatus.Exempt -> new ReviewedLatefee.NotBillable("special exception");
                case ApprovalStatus.Unknown -> new ReviewedLatefee.HeldForReview();
            };
        } else {
            return new ReviewedLatefee.Billable();
        }
    }


    public static List<Invoice> collectPastDue(CustomerV2 customer, List<Invoice> invoices, LocalDate asOfDate) {
        return invoices.stream()
                .filter(invoice -> invoice.getStatus().equals(InvoiceStatus.OPEN))
                .filter(invoice -> isPastDue(invoice, customer, asOfDate))
                .toList();
    }

    public static boolean isPastDue(Invoice invoice, CustomerV2 customer, LocalDate asOf) {
        return invoice.getDueDate().plusDays(gracePeriod(customer).value()).isBefore(asOf);
    }

    public static NonNegativeInt gracePeriod(CustomerV2 customer) {
        return switch (customer.standing()) {
            case Entities.CustomerStanding.Good -> new NonNegativeInt(60);
            case Entities.CustomerStanding.Acceptable -> new NonNegativeInt(30);
            case Entities.CustomerStanding.Poor -> new NonNegativeInt(0);
        };
    }
}
