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
public class Listing5_26_to_5_27 {




    interface ReviewedFee{}

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.26 through 5.27
     * ───────────────────────────────────────────────────────
     * Next up, we tackle how to represent the life cycle of
     * a Late Fee.
     *
     * There are no shortage of options, we walk through the
     * most "obvious" first.
     */
    @Test
    public void example() {
        record USD(BigDecimal value){}
        record PastDue(Invoice invoice){}
        record InvoiceId(String value){}
        record Rejection(String why){}
 // (not defined as part of this listing)

        // Is this good modeling?
        // I'd argue it's not terrible. It enables some really
        // powerful things. However, it's extremely clunky from
        // an ergonomics perspective. We've got a ton of repeated
        // fields between each data type.
        record DraftLateFee(
            String customerId,
            USD total,
            LocalDate invoiceDate,
            LocalDate dueDate,
            List<PastDue> includedInFee
        ){}

        record BilledLatefee(
            InvoiceId id,  //  ◄── This data type is 1:1 with Draft sans this single field
            String customerId,
            USD total,
            LocalDate invoiceDate,
            LocalDate dueDate,
            List<PastDue> includedInFee
        ){}

        record RejectedLatefee(
            Rejection reason,    //  ◄── Ditto here. This is the only thing that's different.
            String customerId,
            USD total,
            LocalDate invoiceDate,
            LocalDate dueDate,
            List<PastDue> includedInFee
        ){}

        // They're clunky, but their modeling gives us power.
        // We can specify exactly which methods operate on which
        // lifecycle states.
        // For instance:
        class Example {
            //
            //                          ┌── We only assess DRAFT late fees!
            //                          ▼
            ReviewedFee assessDraft(DraftLateFee fee) {
                // ...
                return null;
            }
        }

        // However, they remain too clunky to use in practice.
        // Developers recoil from this kind of duplication.
    }
}


