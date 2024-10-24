package dop.invoicing;

import java.util.Optional;

public class Clients {
    /**
     * Tiny little wrapper around our pretend "billing" downstream.
     * We're just a middle man. Another system handles getting the
     * invoices we generate into the official ledger and yet another
     * system handles getting those invoices in front of customers.
     *
     */
    public static class BillingSystem {
        public enum BillStatus { ACCEPTED, REJECTED}
        public record BillSubmitResponse(
                BillStatus status,
                Optional<String> invoiceId,  // defined when BillStatus is Acceptable
                Optional<String> cause   // defined when Rejected
                // System errors will be surfaces as exceptions
        ){}

        public sealed interface ImprovedResponse {
            record Accepted(String invoiceId) implements ImprovedResponse {}
            record Rejected(String reason) implements ImprovedResponse {}
        }


        public BillSubmitResponse submit(Entities.Invoice invoice) {
            return null;
        }

        public ImprovedResponse improvedSubmit(DataTypes.Latefee invoice) {
            return null;
        }

    }



    public static class ApprovalService {
        enum ApprovalStatus {Pending, Approved, Rejected}
        public record ApprovalStatusResponse(ApprovalStatus status){}

        public ApprovalStatusResponse getArr

    }
}
