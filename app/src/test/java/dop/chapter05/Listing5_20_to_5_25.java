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
public class Listing5_20_to_5_25 {




    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.20 through 5.25
     * ───────────────────────────────────────────────────────
     * Now we begin the data modeling!
     *
     * We invented 7 new data types. These all need designed and
     * modeled. We begin with the "easy" one: PastDue
     */
    @Test
    public void example() {
        // One way of capturing "something we know about a state" is
        // through wrapper types. It could be as simple as this.
        record PastDue(Invoice invoice) {
        }

        // However, if we ask the "semantic" questions we learned about
        // in chapters 3 & 4, we'll find that this doesn't quite meet
        // the standards we've been chasing.
        //
        // "What does it mean to be past due?"
        //
        // The entire notion of "past due" depends on *time*
        /*
        record PastDue(Invoice invoice) {
          PastDue {
            if (invoice.dueDate().isBefore(???)) {
                                            ▲
            }                               └──── What goes here??
        }
         */

        // We're in new modeling territory. We cannot defend what this data
        // type "is" via the constructor, because its meaning is contextual
        // and comes from "outside" of it

        // outside context is what makes it different from what we looked
        // at before. Types like NonNegativeInt have everything they need
        // to enforce their semantics "inside" of the constructor.
        record NonNegativeInt(int value) {
            NonNegativeInt {
                if (value < 0) {
                    throw new IllegalArgumentException("Nope");
                }
            }
        }

        // You might try to make the wrapper type carry this outside context.
        record PastDueV2 (
                Invoice invoice,
                LocalDate lateAsOf   // ◄── We could use this to defend the semantics
        ) {/*...*/}                  //     during construction. But should we...?

        // The real world is messy. The bulk of the PastDue data type's value is what it
        // communicates *about* the requirements. It's still useful if it's squishy!

        // These two transforms communicate very different things.
        // List<Invoice> -> LateFee
        // List<PastDue> -> LateFee
    }
}


