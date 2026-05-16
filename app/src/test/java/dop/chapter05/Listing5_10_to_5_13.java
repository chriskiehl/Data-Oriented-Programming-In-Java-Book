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

public class Listing5_10_to_5_13 {




        // This same information can be expressed as a Sum Type!
        //
        //  ┌─ (The sealed part is commented out because we're defining it
        //  │   inside a method)
        //  ▼
        sealed interface Decision {
        record Option1() implements Decision {}  // ─┐
        record Option2() implements Decision {}  //  │ This captures the same if/else information above
        record Option3() implements Decision {}  // ─┘ as a piece of data!

        // This is one of the things that makes data-oriented programming
        // so powerful. We can *decouple* decision from action.
        // This lets us decide what to do with the decisions our application
        // makes and when (if ever!) we take action.
        //
        // We can build interpreters for those decisions!
        Function<Decision, String> doSomethingWithTheDecision = (Decision decision) -> {
            return switch(decision) {
            case Option1 op -> "I do something with option1";
            case Option2 op -> "I do something with option2";
            case Option3 op -> "I do something with option3";
            // Note! This is only here because we didn't seal the type
            // above (due to limitations of what we can define in a method)
            default -> throw new IllegalStateException("Unexpected value: " + decision);
            };
        };
        }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.10 through 5.13
     * ───────────────────────────────────────────────────────
     * We can express branching / choice in our "story" by moving stuff that
     * used to be complicated if/else statements at runtime into
     * algebraic Sum Types expressed at compile time.
     */
    @Test
    public void example() {
        // Here's part of the original implementation
        // It can be very hard to see inside all this sea of nested if/else
        // statements, but we're actually only doing three distinct actions.
        // These three things are hidden because of all the other stuff going on.
        class HereJustForTheExample{
            public ApprovalStatus getApprovalStatus(String approvalId) {
                return ApprovalStatus.APPROVED;
            }
        }
        Runnable originalImplementation = () -> {
            HereJustForTheExample api = new HereJustForTheExample();  //─┐
            Rules rules = new Rules();                                // │ here just to make things compile
            BigDecimal latefee = BigDecimal.ONE;                      // │
            Customer customer = new Customer();                       //─┘
            if (latefee.compareTo(rules.getMaximumFeeThreshold()) <= 0) {
                // [logic omitted]
            } else {
                if (latefee.compareTo(rules.getMaximumFeeThreshold()) > 0) {
                    if (Objects.isNull(customer.getApprovalId())) {
                        // [logic omitted]
                    } else {
                        ApprovalStatus status = api.getApprovalStatus(customer.getApprovalId());
                        if (status.equals(ApprovalStatus.PENDING)) {
                            // [logic omitted]
                        } else if (status.equals(ApprovalStatus.DENIED)) {
                            // [logic omitted]
                        }
                    }
                    // [logic omitted]
                }
                // [logic omitted]
            }
        };

        // Powerful modeling idea:
        // Decisions at runtime can be expressed at compile
        // time using algebraic data types.
        //
        // for example:
        //     if (option1) {          ──┐
        //          // ...               │ Note: This is left as a comment rather than
        //     } else if (option2) {     │ code just because the spirit of it gets lost
        //          // ...               │ in the busy work of adding everything needed
        //     } else {                  │ to make the example compilable.
        //          // ...               │
        //     }                       ──┘
        //
        class ____ {

        }
    }
}


