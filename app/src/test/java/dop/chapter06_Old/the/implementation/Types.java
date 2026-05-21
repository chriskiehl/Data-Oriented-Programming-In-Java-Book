package dop.chapter06.the.implementation;

import dop.chapter05.the.existing.world.Entities;
import dop.chapter05.the.existing.world.Services;
import dop.chapter06.the.implementation.Types.Lifecycle.Billed;
import dop.chapter06.the.implementation.Types.Lifecycle.Draft;
import dop.chapter06.the.implementation.Types.Lifecycle.Rejected;
import dop.chapter06.the.implementation.Types.Lifecycle.UnderReview;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Types {

    public record USD(BigDecimal value) {
        public USD multiply(BigDecimal amount){
            return new USD(value.multiply(amount));
        }
        public static USD zero() {return new USD(BigDecimal.ZERO);}
        public static USD add(USD a, USD b) { return new USD(a.value().add(b.value()));}

        public static Collector<USD,?, USD> summing() {
            return Collectors.reducing(USD.zero(), USD::add);
        }
    }

    public record Percent(double numerator, double denominator) {
        public Percent {
            if (numerator > denominator) {
                throw new IllegalArgumentException(
                        "Percentages are 0..1 and must be expressed " +
                                "as a proper fraction. e.g. 1/100");
            }
        }
        BigDecimal decimalValue() {
            return BigDecimal.valueOf(numerator / denominator);
        }
    }
    public record CustomerId(String value){}
    public record PastDue(Entities.Invoice invoice) {}
    public record InvoiceId(String value){}
    public record Reason(String value){}
    public sealed interface Lifecycle {
        record Draft() implements Lifecycle{}
        record UnderReview(String id) implements Lifecycle {}
        record Billed(InvoiceId id) implements Lifecycle {}
        record Rejected(Reason why) implements Lifecycle {}
    }


    public record LateFee<State extends Lifecycle>(
            State state,
            EnrichedCustomer customer,
            USD total,
            LocalDate invoiceDate,
            LocalDate dueDate,
            List<PastDue> includedInFee
    ){
        public LateFee<Billed> markBilled(InvoiceId id) {
            return new LateFee<>(new Billed(id), customer, total, invoiceDate, dueDate, includedInFee);
        }

        public LateFee<Rejected> markNotBilled(Reason reason) {
            return new LateFee<>(new Rejected(reason), customer, total, invoiceDate, dueDate, includedInFee);
        }

        public LateFee<UnderReview> markAsBeingReviewed(String approvalId) {
            return new LateFee<>(new UnderReview(approvalId), customer, total, invoiceDate, dueDate, includedInFee);
        }

        public <A extends Lifecycle> LateFee<A> inState(A evidence) {
            return new LateFee<>(evidence, customer, total, invoiceDate, dueDate, includedInFee);
        }
    }


    public sealed interface ReviewedFee {
        record Billable(LateFee<Draft> latefee) implements ReviewedFee {}
        record NeedsApproval(LateFee<Draft> latefee) implements ReviewedFee {}
        record NotBillable(LateFee<Draft> latefee, Reason reason) implements ReviewedFee {}
    }

    public record EnrichedCustomer(
            CustomerId id,
            Entities.Address address,
            Percent feePercentage,
            Services.ContractsAPI.PaymentTerms terms,
            Services.RatingsAPI.CustomerRating rating,
            Optional<Services.ApprovalsAPI.Approval> approval
    ) {}
}
