package dop.chapter06;

import dop.chapter05.the.existing.world.Entities;
import dop.chapter05.the.existing.world.Entities.Invoice;
import dop.chapter05.the.existing.world.Entities.LineItem;
import dop.chapter05.the.existing.world.Services;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.ApprovalStatus;
import dop.chapter05.the.existing.world.Services.ContractsAPI;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;
import dop.chapter06.the.implementation.Core;
import dop.chapter06.the.implementation.Service;
import dop.chapter06.the.implementation.Types;
import dop.chapter06.the.implementation.Types.*;
import dop.chapter06.the.implementation.Types.Lifecycle.Draft;
import dop.chapter06.the.implementation.Types.ReviewedFee.Billable;
import dop.chapter06.the.implementation.Types.ReviewedFee.NotBillable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter05.the.existing.world.Entities.InvoiceStatus.OPEN;
import static dop.chapter05.the.existing.world.Entities.InvoiceType.STANDARD;
import static dop.chapter05.the.existing.world.Services.ApprovalsAPI.ApprovalStatus.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.mockito.Mockito.*;

public class Listing6_16 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.16
     * ───────────────────────────────────────────────────────
     * Functions are just tables of data in disguise.
     */
    @Test
    void example() {
        class __{
            // determinism means that every output for this function
            // is already pre-defined.
            static int increment(int x) {
                return x + 1;
            }

            // We could even compute all of its answers ahead of time.
            static Map<Integer, Integer> ANSWERS = new HashMap<>();
            // [NOTE] This different from the book in that we don't actually
            //        crawl over every integer (which would be very slow and
            //        consume a lot of memory).
            static Integer PRETEND_MIN_VALUE = -10;
            static Integer PRETEND_MAX_VALUE = 10;
            static {
                for (int i = PRETEND_MIN_VALUE; i < PRETEND_MAX_VALUE-1; i++) {
                    ANSWERS.put(i, increment(i));
                }
            }
            // We end up with a lookup table that maps inputs to outputs.
            // |  Input   |    Output   |
            // |    1     |      2      |
            // |    2     |      3      |
            // etc..
        }
    }
}
