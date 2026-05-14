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
public class Listing5_48_to_5_49 {




    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.48 through 5.49
     * ───────────────────────────────────────────────────────
     * A big theme that permeates this chapter is that engineering
     * is contextual. We're always making tradeoffs.
     *
     * In this listing we explore a refactoring that we *might* want
     * to do. The "cost" here is that we begin carving into the world
     * of "what's already there" and rebuilding it into a new shape.
     *
     * This is a valuable thing to do in most codebases, but not always
     * the "right" thing to do as part of an individual feature.
     */
    @Test
    public void example() {
        // ┌────────────────────────────────────────────────────────────────────┐
        // │        Decoupling from the Tyranny of What's Already There         │
        // └────────────────────────────────────────────────────────────────────┘
        //

        // minimal shims just to enable the main example.
        record CustomerId(){}
        record Percent(){}

        // We don't have to settle for the world as we found it -- as it was dictated
        // by other people. We can unify disparate services and APIs.
        //
        // Design is about making the world as we want it to be.
        //
        // An obvious problem with the service oriented hellscape in which our feature
        // lives is that the boundaries, which may be totally sane at the system level, do
        // not fit what we're trying to do at the service level. They force our data to be
        // fragmented.
        //
        // We can hide these arbitrary service boundaries behind a new data type.
        record EnrichedCustomer(
                CustomerId id,
                Address address,
                Percent feePercentage,
                PaymentTerms terms,
                CustomerRating rating,
                Optional<Approval> approval
        ) {}
    }
}


