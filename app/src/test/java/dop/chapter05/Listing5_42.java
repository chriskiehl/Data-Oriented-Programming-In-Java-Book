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

public class Listing5_42 {




    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.42
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


