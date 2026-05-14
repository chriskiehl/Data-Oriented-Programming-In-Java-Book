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

/**
 * Chapter 6 walks through implementing the domain model
 * we came up with in chapter 5. However, that'd be pretty
 * boring on its own, so that chapter is *really* about
 * the choices we make while designing. It's about functions!
 * And determinism! And testability! And a whole host of other
 * things! It's a fun one.
 */
public class Listing6_39_to_6_43 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.39 through 6.43
     * ───────────────────────────────────────────────────────
     * The Optional holy war
     */
    @Test
    void example() {
        // [note] listings 6.39 and 6.40 are skipped here
        //        since they're covered in the implementation package.
        //        see: dop.chapter06.the.implementation
        //
        // Instead, we'll focus on.... Optional!
        class __{
            // (ignore this. It's just here to power the below examples)
            static Optional<String> tryToFindThing(String whatever) {
                return Optional.empty();
            }

            // Optionals are contentious because we can interact with them
            // both functionally and imperatively.
            //
            // Here's the imperative style
            static String imperativeExample(String thingId) {
                Optional<String> maybeThing = tryToFindThing(thingId);
                return maybeThing.isPresent()
                    ? maybeThing.get().toUpperCase()
                    : "Nothing found!";
            }
            // Here's the functional approach.
            static String functionalExample(String thingId) {
                return tryToFindThing(thingId)
                        .map(String::toUpperCase)
                        .orElse("Nothing Found!");
            }
            // The question is: which is better?

            // Is this:
            static String example1(LateFee<Draft> draft) {
                return draft.customer().approval().map(approval -> switch(approval.status()) {
                    case APPROVED -> "new Billable(draft)"; // (stringified to mirror the shortened book example)
                    case PENDING -> "new NotBillable(draft, ...)";
                    case DENIED -> "new NotBillable(draft, ...)";
                }).orElse("new NeedsApproval(draft)");
            }
            // better than this?
            static String example2(LateFee<Draft> draft) {
                return draft.customer().approval().isEmpty()
                    ? "new NeedsApproval(draft)"
                    :  switch (draft.customer().approval().get().status()) {
                        case APPROVED -> "new Billable(draft)";
                        case PENDING -> "new NotBillable(draft, ...)";
                        case DENIED -> "new NotBillable(draft, ...)";
                };
            }
            // Or are they just different?
            // The book makes an argument for each in different situations.
        }
    }
}
