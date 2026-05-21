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

public class Listing6_26_to_6_29 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.26 through 6.29
     * ───────────────────────────────────────────────────────
     * Implementation begins!
     * This is where we'll start to see our modeling efforts begin
     * to pay us back. Most of the functions will just follow the
     * types we designed.
     */
    @Test
    void example() {
       class V1 {
           // Here's where we left on in Chapter 5.
           public static List<PastDue> collectPastDue(
                   EnrichedCustomer customer,
                   LocalDate today,
                   List<Invoice> invoices) {
               // Implement me!
               return null;
           }
       }

       class V2 {
            static boolean TODO = true;
            // This is where all the function stuff we talked about comes into play.
            // Deterministic functions can only do what their types say.
            // That's all they can do.
            // Which means that our implementation just follows the types we designed.
            public static List<PastDue> collectPastDue(
                    EnrichedCustomer customer,
                    LocalDate today,
                    List<Invoice> invoices) {
                // everything other than the filter is just doing what the type
                // signature says.
                return invoices.stream()
//                        .filter(invoice -> ???)  ◄─── We just have to decide what goes here.
                        .map(PastDue::new)
                        .toList();
           }
       }
       class V3 {
           public static List<PastDue> collectPastDue(
                   EnrichedCustomer customer,
                   LocalDate today,
                   List<Invoice> invoices) {
               return invoices.stream()
                       //                    ┌──── Adding in the filter implementation
                       //                    ▼
                       .filter(invoice -> isPastDue(invoice, customer.rating(), today))
                       .map(PastDue::new)
                       .toList();
           }

           static boolean isPastDue(Invoice invoice, CustomerRating rating, LocalDate today) {
               return invoice.getInvoiceType().equals(STANDARD)
                       && invoice.getStatus().equals(OPEN)
                       && today.isAfter(invoice.getDueDate().with(gracePeriod(rating)));
               //         └────────────────────────────────────────────────────────────┘
               //                               │
               //                               └─ Note how much this reads like the requirement! Neat!
           }

           // (We defined this one a few listings ago.)
           static TemporalAdjuster gracePeriod(CustomerRating rating) {
               return switch(rating) {
                   case CustomerRating.GOOD -> date -> date.plus(60, DAYS);
                   case CustomerRating.ACCEPTABLE -> date -> date.plus(30, DAYS);
                   case CustomerRating.POOR -> lastDayOfMonth();
               };
           }
       }
    }
}
