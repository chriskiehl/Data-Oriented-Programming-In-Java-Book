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
public class Listing5_7_to_5_9 {




    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.6 through 5.9
     * ───────────────────────────────────────────────────────
     * The design work is refining the "story" that our data types communicate.
     *
     * You might imagine explaining the feature to a coworker. Actually just
     * putting the requirements to words (rather than code) tends to quickly
     * clarify when your story is "saying" the wrong thing or missing details.
     */
    @Test
    public void example() {
        //
        // List<Invoice>
        // -> List<PastDue>   ◄──────┐ This is what was missing from out first
        // -> LateFeeInvoice         │ description. We don't process ANY invoices, we
        //                           │ process *Past Due* invoices.
        //
        // Is this the whole story? Would the imaginary coworker we're explaining
        // all of this to have a good mental model at this point?
        // Probably not! There's still much left unsaid.
        //
        // We keep refining
        //
        // List<Invoice>
        // -> List<PastDue>
        // -> DraftLateFee    ◄──────┐ Another clarification. Our service is just a
        // -> LateFeeInvoice         │ middleman. We don't make the late fees directly.
        //                           │ We make a description of a latefee we want to have later.
        //
        // If you mentally swap those arrows (->) for something like "and then we use
        // that to make..." you the story this data is telling becomes explicit
        //
        //  (we start with) List<Invoice>
        //  (and then we use that to make) List<PastDue>
        //  (and then we use that to make a) LateFeeDraft
        //  (and then we use that to make a) LateFeeInvoice
        //
    }
}


