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

public class Listing6_11_to_6_14 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.11 through 6.14
     * ───────────────────────────────────────────────────────
     * An interesting question: how many possible implementations
     * could we write for a given type signature?
     *
     * This will feel weird if you've never thought about it
     * before. Learning to think about what our code really
     * says -- or, more importantly, **enables** -- is a valuable
     * design skill.
     */
    @Test
    void example() {

        enum People {Bob, Mary}
        enum Jobs {Chef, Engineer}

        class SomeClass {
            // [Hidden]  // ◄──── Assume all kinds of instance state here

            // How many different implementations could we write
            // inside of this method?
            People someMethod(Jobs job) {
                // this.setCombobulator("large");
                // universe.runSimulation(job);
                // collapseSingularity(this)
                return null; // ◄── This null isn't in the book. It's only
            //                      here so the code will compile. Pretend
            //      ▲               the entire implementation is hidden.
            //      │
            //      │
            }//     The answer is infinite.
            //      Methods are allowed to do anything.
        }

        class __ {
            //            ┌───── But what if we make that same method static?
//            ▼
            static People someMethod(Jobs job) {
                // [hidden]
                return null; // (again, ignore this null.)
            }
            // This won't seem important at first, but its implications
            // are far-reaching.
            // There is now a finite number of ways this method
            // could be implemented. And it's entirely determined
            // by the types we choose.
            //
            // We can enumerate them all!
            static People optionOne(Jobs job) {
                return switch (job) {
                    case Chef -> People.Bob;
                    case Engineer -> People.Bob;
                };
            }

            static People optionTwo(Jobs job) {
                return switch (job) {
                    case Chef -> People.Mary;
                    case Engineer -> People.Mary;
                };
            }

            static People optionThree(Jobs job) {
                return switch (job) {
                    case Chef -> People.Bob;
                    case Engineer -> People.Mary;
                };
            }

            static People optionFour(Jobs job) {
                return switch (job) {
                    case Chef -> People.Mary;
                    case Engineer -> People.Bob;
                };
            }

            // For some fun background that's not in the book
            // The number of possible implementations, or, in
            // math speak, the number of ways of mapping from one
            // set to another, is computed by
            //     |InputType|^|OutputType|
            // i.e. the cardinality of the set of values in the input
            // type raised to the cardinality of the output type.
            //
            // You can see this in the example above. 2^2 = 4 possible
            // implementations this function can have.
        }
    }
}
