package dop.invoicing;

import dop.invoicing.DataTypes.BillingState.Billed;
import dop.invoicing.DataTypes.BillingState.Pending;
import dop.invoicing.DataTypes.BillingState.Rejected;
import dop.invoicing.Entities.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class DataTypes {

    public record NonNegativeInt(int value) {
        public NonNegativeInt {
            if (value < 0) {
                throw new IllegalStateException("Boo");
            }
        }
    }

    public record Percent(double num, double denom) {
        public Percent {
            if (num > denom) {
                throw new IllegalArgumentException("Invalid percentage");
            }
        }

        public BigDecimal value() {
            return BigDecimal.valueOf(num / denom);
        }
    }



    public record Address(Locale.IsoCountryCode country) {}

    public enum ApprovalStatus {Unknown, ApprovedForLargeFee, Exempt}
    public enum CustomerStanding {Good, Acceptable, Poor}

    public record Customer(
            String id,
            Address billingAddress,
            ApprovalStatus status,
            CustomerStanding standing
    ) {}

    public record CustomerInvoices(Customer customer, List<Invoice> invoices){}



    public sealed interface BillingState {
        record Pending() implements BillingState {}
        record Billed(String invoiceId, LocalDate invoiceDate, LocalDate due) implements BillingState {}
        record Rejected(String why) implements BillingState {}
    }

    public record Latefee<State extends BillingState>(
            String customerId,
            Entities.USD total,
            State state,
            LocalDate asOfDate,
            List<Invoice> includedInFee
    ){

        public Latefee<Billed> markAsBilled(String invoiceId) {
            return new Latefee<>(customerId, total, new Billed(invoiceId), asOfDate, includedInFee);
        }

        public Latefee<Rejected> markAsFailed(String reason) {
            return new Latefee<>(customerId, total, new Rejected(reason), asOfDate, includedInFee);
        }
    }


    public sealed interface ReviewedLatefee {
        public record Billable(Latefee<Pending> latefee) implements ReviewedLatefee {}
        public record NotBillable(Latefee<Pending> latefee, String rationale) implements ReviewedLatefee {}
        public record HeldForReview(Latefee<Pending> latefee, String rationale) implements ReviewedLatefee {}
    }


    public record InvoicingData(
            Customer customer,
            List<Invoice> invoices,
            LocalDate closeDate,
            Locale.IsoCountryCode country,

            Percent feePercent,
            Entities.Config config
    ){}

}
