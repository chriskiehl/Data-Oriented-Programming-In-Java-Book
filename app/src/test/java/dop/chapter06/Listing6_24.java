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

public class Listing6_24 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.24
     * ───────────────────────────────────────────────────────
     * Don't drive yourself crazy over purity and referential
     * transparency. Close enough is good enough.
     */
    @Test
    void example() {
        class __ {
            //
            record PastDue(Invoice invoice) {}
            //               ▲
            //               └── We depend on a mutable identity object. We can never
            //                   truly be referential transparent because the "same" object
            //                   could lead to different results.
            //
            // But this is just being needlessly pedantic 99.999999999999% of the time.
            // As long as you're not sharing references around or performing mutation
            // the risk here is low enough to ignore.
        }
    }
}
