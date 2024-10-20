package dop.invoicing;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale.IsoCountryCode;

public class Entities {

    record USD(BigDecimal amount) implements Comparable<USD> {

        public USD add(USD usd) {
            return new USD(this.amount.add(usd.amount()));
        }

        public USD multiply(BigDecimal amount) {
            return new USD(this.amount.add(amount));
        }

        public static USD zero() {
            return new USD(BigDecimal.ZERO);
        }

        @Override
        public int compareTo(USD o) {
            return amount().compareTo(o.amount());
        }
    }

    record Address(IsoCountryCode country) {}

    enum CustomerStanding {Good, Acceptable, Poor}

    @Springish.Entity
    public record Customer(
            String id,
            Address billingAddress,
            Boolean approvedForLargeFees,
            CustomerStanding standing
    ) {}


    enum CannotBillReasons {
        BelowMinimumThreshold,
        AboveMaximumThreshold,
        AboveApprovedThreshold
    }

    enum InvoiceStatus {OPEN, Closed};
    @Springish.Entity
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Invoice {
        String invoiceId;
        String customerId;
        USD charges;
        USD credits;
        InvoiceStatus status;
        LocalDate invoiceDate;
        LocalDate dueDate;
        boolean isLatefee;
        boolean canCharge;
        CannotBillReasons reason;
        List<Invoice> includedInFee;
        List<Invoice> excludedFromFee;


    }

    @Springish.Entity
    public record Config(USD minimumFeeThreshold, USD maximumFeeThreshold) {}

}
