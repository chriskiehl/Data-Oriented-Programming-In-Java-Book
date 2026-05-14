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
public class Listing5_28_to_5_32 {





    record USD(BigDecimal value){}
    record PastDue(Invoice invoice){}
    record InvoiceId(String value){}
    record Rejection(String why){}
    record Details(
            USD total,
            LocalDate invoiceDate,
            LocalDate dueDate,
            List<PastDue> includedInFee
    ){}

        // Maybe sealing fixes?
        sealed interface LateFee {
        record DraftV2(Details details) implements LateFee {}
        record BilledV2(InvoiceId id, Details details) implements LateFee {}
        record RejectedV2(Rejection reason, Details details) implements LateFee {}

        }

        //
        // Even MORE workarounds?!?
        //
        interface HasDetails {  // This CAN work, but.... should we do this?
        Details details();  //
        }

        interface LateFeeV2 extends HasDetails {
        record DraftV3(Details details) implements LateFeeV2 {}
        record BilledV3(InvoiceId id, Details details) implements LateFeeV2 {}
        record RejectedV3(Rejection reason, Details details) implements LateFeeV2 {}
        }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.28 - 5.32
     * ───────────────────────────────────────────────────────
     * Exploring more representations and their implications on code.
     *
     */
    @Test
    public void example() {
        class ___ {
            record USD(BigDecimal value){}
            record PastDue(Invoice invoice){}
            record InvoiceId(String value){}
            record Rejection(String why){}
            record Details(                     //  ──┐
                                                USD total,                  //    │  Pulling all the common fields out
                                                LocalDate invoiceDate,      //    │  onto their own data type
                                                LocalDate dueDate,          //    │
                                                List<PastDue> includedInFee //  ──┘
            ){}
            record DraftLateFee(
                    Details details   //  ◄── Now the details can be shared
            ){}

            record BilledLatefee(
                    InvoiceId id,
                    Details details   //  ◄── Ditto
            ){}

            record RejectedLatefee(
                    Rejection reason,
                    Details details   //  ◄── Ditto
            ){}


            // Is this better? It depends!
            // As we design the representation, we have to keep an eye
            // towards how these things will actually work in practice.
            //
            // What you might not notice until you start trying to program
            // with these is that they're really inflexible.
            //
            // Can we, say, compute some aggregate stats? Totals by lifecycle?
            /*
            Map<???, USD> totalsByLifecycle(List<???> fees) {
                //  ???                           ▲
            }                                     └── What goes here? Each lifecycle is an isolated type
            */



            // Maybe now..?
            Function<List<LateFee>, Map<LateFee, USD>> totalsByLifecycle = (List<LateFee> fees) -> {
                // fees.stream().map(fee -> fee.details() ???)
                //                                ▲
                //                                └── You might expect this to work since they're
                //                                    all the same data type, but to Java, they're
                //                                    "just" an interface. It has no idea what's inside.

                return null;
            };

            // More workarounds...?
            Function<List<LateFee>, Map<LateFee, USD>> totalsByLifecycleV2 = (List<LateFee> fees) -> {
                //  fees.stream()
                //      .map(fee -> return switch(fee) {
                //          case DraftV2 d -> d.details();     ─┐
                //          case BilledV2 b -> b.details();     │ I think the only appropriate response to
                //          case RejectedV2 r -> r.details();  ─┘ this nonsense is "ugh..."
                //      })
                //      .map(...)

                return null;
            };




            // Let's use this need for sophisticated workarounds as feedback
            // that our modeling isn't working.
        }

    }
}


