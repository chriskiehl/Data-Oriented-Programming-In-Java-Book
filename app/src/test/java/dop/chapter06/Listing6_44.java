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

public class Listing6_44 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.44 through 6.47
     * ───────────────────────────────────────────────────────
     * It's ok to introduce types! As many as you need!
     *
     * Clarity while reading > enjoyment while writing.
     */
    @Test
    void example() {
        class __ {
            // we being here.
            // This is an OK method, but it's visually assaulting. It's too dense to
            // understand without slowing down to study it.
            public static ReviewedFee assessDraft(Entities.Rules rules, LateFee<Draft> draft) {
                if (draft.total().value().compareTo(rules.getMinimumFeeThreshold()) < 0) {
                    return new NotBillable(draft, new Reason("Below threshold"));
                } else if (draft.total().value().compareTo(rules.getMaximumFeeThreshold()) > 0) {
                    return draft.customer().approval().isEmpty()
                        ? new ReviewedFee.NeedsApproval(draft)
                        : switch (draft.customer().approval().get().status()) {
                            case APPROVED -> new Billable(draft);
                            case PENDING -> new NotBillable(draft, new Reason("Pending decision"));
                            case DENIED -> new NotBillable(draft, new Reason("exempt from large fees"));
                    };
                } else {
                    return new Billable(draft);
                }
            }
        }

        // So what if we did this: separate where we make a decision from what we do with it.
        class V2 {
            // We can introduce our own private enum to explain the semantics behind
            // what the assessments mean.
            private enum Assessment {ABOVE_MAXIMUM, BELOW_MINIMUM, WITHIN_RANGE}

            // and we can use that while figuring out what's up with the total.
            // doing so visually (and cognitively!) simplifies the code. We only
            // worry about one thing at a time.
            static Assessment assessTotal(Entities.Rules rules, USD total) {
                if (total.value().compareTo(rules.getMinimumFeeThreshold()) < 0) {
                    return Assessment.BELOW_MINIMUM;
                } else if (total.value().compareTo(rules.getMaximumFeeThreshold()) > 0) {
                    return Assessment.ABOVE_MAXIMUM;
                } else {
                    return Assessment.WITHIN_RANGE;
                }
            }
            // Which in turn simplifies this method.
            // It's been reduced down to pattern matching. This is quick to skim
            // and quick to understand.
            public static ReviewedFee assessDraft(Entities.Rules rules, LateFee<Draft> draft) {
                return switch (assessTotal(rules, draft.total())) {
                    case Assessment.WITHIN_RANGE -> new Billable(draft);
                    case Assessment.BELOW_MINIMUM -> new NotBillable(draft, new Reason("Below threshold"));
                    case Assessment.ABOVE_MAXIMUM -> draft.customer().approval().isEmpty()
                        ? new ReviewedFee.NeedsApproval(draft)
                        : switch (draft.customer().approval().get().status()) {
                            case APPROVED -> new Billable(draft);
                            case PENDING -> new NotBillable(draft, new Reason("Pending decision"));
                            case DENIED -> new NotBillable(draft, new Reason("exempt from large fees"));
                    };
                };
            }
        }
    }
}
