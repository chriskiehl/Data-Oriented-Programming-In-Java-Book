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
public class Listing5_19 {




    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.19
     * ───────────────────────────────────────────────────────
     * We use this design to guide our implementation.
     * Each arrow can be turned directly into a method.
     */
    @Test
    public void example() {
        // Note: We don't define these in the listing. They're
        // just here as minimal shims to enable compilation.
        record PastDue(){}
        record LatefeeDraft(){}
        record ReviewedDraft(){}
        record BillingResult(){}
        record NeedsReview(){}

        //
        //      ┌──── (this class is here just so we can show the method definitions)
        //      ▼
        class ___ {
            List<PastDue> collectPastDue(List<Invoice> invoices){return null;}  // ◄──┐ (All return null just so they
            LatefeeDraft buildTheDraft(List<PastDue> invoices) {return null;}   //    │  will compile)
            ReviewedDraft assesTheDraft(LatefeeDraft invoice) {return null;}
            BillingResult submitBill(LatefeeDraft draft){return null;}
            Approval startApproval(NeedsReview needsReview) {return null;}
        }
    }
}


