package dop.chapter05;

import com.google.common.base.Strings;
import dop.chapter05.the.existing.world.Entities.*;
import dop.chapter05.the.existing.world.Repositories;
import dop.chapter05.the.existing.world.Repositories.FeesRepo;
import dop.chapter05.the.existing.world.Services;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.Approval;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.ApprovalStatus;
import dop.chapter05.the.existing.world.Services.ContractsAPI;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.util.stream.Collectors.*;

/**
 * Chapter 5 takes all the modeling tools we've explored
 * so far and applies them to building a complex feature.
 * No more simple domains. No more isolated modeling. We
 * dive into the messy world of building software. That
 * means everything that makes it hard: databases, ORMS,
 * third party services (with APIs we don't control), and
 * the absolute worst thing of all: prior decisions.
 *
 * We'll learn how to work with all of these limitations
 * and produce clean, clear, data-oriented code.
 */
public class Listing5_39_to_5_40 {





    record PastDue(Invoice invoice) {}
    record USD(BigDecimal value) {}
    record InvoiceId(String value){}
    record Rejection(String why){}
    record Draft() implements Lifecycle{}
    record Billed(InvoiceId id) implements Lifecycle {}
    record Rejected(Rejection why) implements Lifecycle {}
    record LateFee<State extends Lifecycle>(
            State state,
            USD total,
            LocalDate invoiceDate,
            LocalDate dueDate,
            List<PastDue> includedInFee
    ){}
    record Billable(LateFee<Draft> latefee) implements ReviewedFee {}
    record NeedsReview(LateFee<Draft> latefee) implements ReviewedFee {}
    record NotBillable(LateFee<Draft> latefee, String rationale) implements ReviewedFee {}

        sealed interface Lifecycle {}

        sealed interface ReviewedFee {}

        interface TheBehaviorsWillLookLikeThis {
        List<PastDue> collectPastDue(List<Invoice> invoices);
        LateFee<Draft> buildDraft(List<PastDue> pastDue);
        ReviewedFee assessDraft(LateFee<Draft> draft);
        LateFee<? extends Lifecycle> submitBill(Billable billableFee);
        Approval startApproval(NeedsReview needsReview);
        }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.39 - 5.40
     * ───────────────────────────────────────────────────────
     * Here's our initial sketch of the data model
     */
    @Test
    public void example() {
        class ___ {
            record PastDue(Invoice invoice) {}
            record InvoiceId(String value){}
            record Rejection(String why){}
            record USD(BigDecimal value) {        }



        }

    }
}


