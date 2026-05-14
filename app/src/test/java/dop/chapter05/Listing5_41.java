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
public class Listing5_41 {




    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.41
     * ───────────────────────────────────────────────────────
     * Our data modeling was done in a loose isolation of the rest
     * of the world. Now we have to merge them together.
     *
     * As an initial starting point, we need to map out what each
     * "new" method depends on from the "old" world.
     *
     */
    @Test
    public void example() {
        /*
        collectPastDue
            DependsOn:
                List<Invoice> (Entity / Database (read))
                CurrentDate (Environment (read))
                CustomerRating (API (read))
            Output:
                List<PastDueInvoice>

        buildDraft
            DependsOn:
                List<PastDue> (Output from collectPastDue)
                CurrentDate (Environment (read))
                PaymentTerms (API (read))
                FeePercentage (Database (read))
                Customer (Database (read))
          Output:
                Latefee<Draft>

        assessDraft
            DependsOn:
                LateFee<Draft> (Output from buildDraft)
                Rules (Database (read))
                Customer (Database (read))
                ApprovalStatus (API (read))
            Output:
                Billable
                OR NeedsReview
                OR NotBillable
        submitBill
            DependsOn:
                BillableFee (output from AssessDraft)
                BillingService (API write)
            Output:
                BilledLateFee
                OR RejectedLateFee

        startApproval
            DependsOn:
                NeedsReview (output from AssessDraft)
                CustomerID (database (read))
                ApprovalsService (API read/write)

             */
    }
}


