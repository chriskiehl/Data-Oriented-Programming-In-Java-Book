package dop.chapter05;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static dop.chapter05.the.existing.world.Entities.Customer;
import static dop.chapter05.the.existing.world.Entities.Rules;
import static dop.chapter05.the.existing.world.Services.ApprovalsAPI.ApprovalStatus;
import static dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;

public class Listing5_48 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.48
     * ───────────────────────────────────────────────────────
     * The type signature for buildDraft
     * ───────────────────────────────────────────────────────
     */
    Latefee<Draft> buildDraft(
        Customer customer,
        List<PastDue> invoices,
        LocalDate today,
        PaymentTerms terms,            //  ┐◄── This is data about a customer, but it shows up
        BigDecimal feePercentage       //  ┘    as distinct inputs to our method because it happens
                                       //       to come from different external services
    ){return null;}

    ReviewedFee assessDraft(
        Rules rules,
        Customer customer,
        ApprovalStatus status,   // ◄── Same thing here. Approvals are directly related to customers, and yet they’re tracked separately
        Latefee<Draft> draft
    ){return null;}








    record Draft() {}
    record Latefee<State>(State state) {}
    record PastDue() {}
    interface ReviewedFee {}
}
