package dop.invoicing;

import dop.invoicing.DataTypes.Latefee;
import dop.invoicing.Entities.Invoice;

import java.util.Optional;

/**
 * Tiny little wrapper around our pretend "billing" downstream.
 * We're just a middle man. Another system handles getting the
 * invoices we generate into the official ledger and yet another
 * system handles getting those invoices in front of customers.
 *
 */
public class BillingSystem {
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


    public BillSubmitResponse submit(Invoice invoice) {
        return null;
    }

    public ImprovedResponse improvedSubmit(Latefee invoice) {
        return null;
    }

}
