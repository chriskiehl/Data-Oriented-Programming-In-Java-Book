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

public class Listing5_19 {

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.19
     * ───────────────────────────────────────────────────────
     * A narrative for human consumption.
     *
     * This is why we go through this design exercise. You can see
     * all of the important requirements directly in the data types.
     * We have an outline for our code that looks like it was written
     * for humans, rather than machines.
     */
    @Test
    public void example() {
        // step 1: collect the past due
        // List<Invoice> -> List<PastDue>

        // Step 2: use those to build the draft
        // List<PastDue> -> LateFeeDraft

        // Step 3: decide what to do with the draft
        // LateFeeDraft -> (BillableFee OR NotBillable OR NeedsApproval

        // then depending on what we decided, either:
        // Step 4.1: submit billable items
        // BillableFee -> (BilledLateFee OR RejectedLateFee)

        // OR Step 4.2: Start the approval process for those that need it
        // NeedsApproval -> Approval

        // OR Step 4.3: keep the non-billable data for posterity
        // NotBillable -> NotBillable  (Stays the same)
    }
}


