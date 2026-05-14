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
public class Listing5_15_5_17 {




    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.15 through 5.17
     * ───────────────────────────────────────────────────────
     * Interesting things happen when we lift the decisions our
     * program makes into the type system. We're suddenly forced
     * to deal with each of those decisions *as a data type*.
     *
     * Our "story" starts to naturally follow those branching paths.
     */
    @Test
    public void example() {
        //
        // List<Invoice>
        // -> List<PastDue>
        // -> LateFeeDraft
        // -> BillableFee -> (BilledLateFee OR RejectedLateFee)
        //    OR NotBillable -> NotBillable  ◄── Keeping the type the same is how we can say "no change"
        //    OR NeedsApproval -> Approval
        //          ▲                ▲
        //          └────────────────┘
        //                  │ The nice thing about really descriptive types is that they largely
        //                  │ push themselves in the right direction.
        //                  │ What kind of output will something that takes a `NeedsApproval` as
        //                  │ input produce? An `Approval`!
    }
}


