package dop.chapter05.the.existing.world;

import java.util.Optional;

public class Services {
    public interface RatingsAPI {
        enum CustomerStanding {GOOD, ACCEPTABLE, POOR}
        CustomerStanding getRating(String customerId);
    }

    public interface ContractsAPI {
        enum PaymentTerms {NET_30, NET_60, END_OF_MONTH, DUE_ON_RECEIPT}
        PaymentTerms getPaymentTerms(String customerId);
    }

    public interface ApprovalsAPI {
        enum Status {Pending, Approved, Denied}
        record Approval(String id, Status status){}
        record CreateApprovalRequest(/*...*/) {}
        Approval createApproval(CreateApprovalRequest request);
        Optional<Approval> getApproval(String approvalId);
    }

    public interface BillingAPI {
        enum Status {ACCEPTED, REJECTED}
        record SubmitInvoiceRequest(/*...*/) {}
        record BillingResponse(
                Status status,
                String invoiceId,
                String error
        ){}
        BillingResponse submit(SubmitInvoiceRequest request);
    }
}
