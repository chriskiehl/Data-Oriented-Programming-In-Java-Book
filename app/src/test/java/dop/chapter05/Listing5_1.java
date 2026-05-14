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
public class Listing5_1 {




    interface RatingsAPI {
        enum CustomerStanding {GOOD, ACCEPTABLE, POOR}
        CustomerStanding getRating(String customerId);
    }

    interface ContractsAPI {
        enum PaymentTerms {NET_30, NET_60, END_OF_MONTH, DUE_ON_RECEIPT}
        PaymentTerms getPaymentTerms(String customerId);
    }

    interface ApprovalsAPI {
        enum Status {Pending, Approved, Denied}
        record Approval(String id, Status status){}
        record CreateApprovalRequest(/*...*/) {}
        Approval createApproval(CreateApprovalRequest request);
        Optional<Approval> getApproval(String approvalId);
    }

    interface billingAPI {
        enum Status {ACCEPTED, REJECTED}
        record SubmitInvoiceRequest(/*...*/) {}
        record BillingResponse(
            Status status,
            String invoiceId,
            String error
        ){}
        BillingResponse submit(SubmitInvoiceRequest request);
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.1
     * ───────────────────────────────────────────────────────
     * We're implementing the feature in an application that
     * already "exists." So, the first thing we do in the chapter
     * is set the stage.
     *
     * This pretend app is "modern" and "service oriented." These
     * are the external APIs we'll interact with.
     * We cheat a bit and ignore stuff like HTTP and failures.
     */
    @Test
    public void example() {







    }
}


