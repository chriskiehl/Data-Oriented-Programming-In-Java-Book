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
public class Listing5_6 {




    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.6
     * ───────────────────────────────────────────────────────
     * These listings explore one of my favorite design tools for
     * behaviors: thinking in transforms between data.
     *
     * We represent a transformation with an arrow (->).
     * Data goes in one side, new data comes out the other.
     *
     * This is about as lightweight as design processes get. We
     * invent data on the fly and give it 'movement' through our
     * system via the arrows. From this movement comes continuity.
     * The data types moving through our system begin to tell a
     * story -- if we do it well, this story reads almost like
     * a plain english version of our requirements.
     */
    @Test
    public void example() {
        // We represent transforms with the ascii arrow (->)
        //
        //      ┌─────────────────────────────┐  This transform says that
        //      ▼                             │  a List of Invoices is used to
        // List<Invoice> -> LateFeeInvoice  ◄─┘  produce a new LateFeeInvoice
    }
}


