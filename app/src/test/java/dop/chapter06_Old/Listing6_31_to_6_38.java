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

public class Listing6_31_to_6_38 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.31 through 6.38
     * ───────────────────────────────────────────────────────
     * The right type can reveal shortcomings in the design of the system.
     */
    @Test
    void example() {
        class V1 {
            // I'll keep drawing attention to it.
            // Checkout this type signature. It takes a list of invoices and
            // returns a single LateFee draft.
            // Our implementation is forced into being "small." Functions can't go off and do
            // anything they way. They map inputs to outputs.
            static LateFee<Draft> buildDraft(LocalDate today, EnrichedCustomer customer, List<PastDue> invoices) {
                // Which means that this is pretty much the only implementation
                // that's even allowed by our types. It HAS to return this data type.
                return new LateFee<>(  //─┐
                    new Draft(),       // │ And all of this is pre-ordained.
                    customer,          // │
                    null,              // │ The only thing left for us to do is implement the
                    today,             // │ thing the computes the total and the due dates.
                    null,              // │
                    invoices           //─┘
                );
            }
        }
        class V2 {
            // I'll keep drawing attention to it.
            // Checkout this type signature. It takes a list of invoices and
            // returns a single LateFee draft.
            // Our implementation is forced into being "small." Functions can't go off and do
            // anything they way. They map inputs to outputs.
            static LateFee<Draft> buildDraft(LocalDate today, EnrichedCustomer customer, List<PastDue> invoices) {
                // Which means that this is pretty much the only implementation
                // that's even allowed by our types. It HAS to return this data type.
                return new LateFee<>(  //─┐
                        new Draft(),       // │ And all of this is pre-ordained.
                        customer,          // │
                        null,              // │ The only thing left for us to do is implement the
                        today,             // │ thing the computes the total and the due dates.
                        null,              // │
                        invoices           //─┘
                );
            }

            // Implementing the due date is easy. If follows the requirements.
            static LocalDate dueDate(LocalDate today, PaymentTerms terms) {
                // Note that well typed functions are small! Often the first thing
                // we do is start returning data.
                return switch (terms) {
                    case PaymentTerms.NET_30 -> today.plusDays(30);
                    case PaymentTerms.NET_60 -> today.plusDays(60);
                    case PaymentTerms.DUE_ON_RECEIPT -> today;
                    case PaymentTerms.END_OF_MONTH -> today.with(lastDayOfMonth());
                };
            }

            // computing the total is far more interesting, because it holds something
            // that feels gross.

            // The "outside world" speaks BigDecimal and Currency.
            // Our world speaks USD (per the requirements).
            // ┌──────────────────────────────────────────────────────────────────┐
            //            THE FACT THAT THIS FEELS AWFUL IS A FEATURE
            // └──────────────────────────────────────────────────────────────────┘
            //
            // We shouldn't do this conversion in "our world." In fact, we shouldn't
            // "do" it at all. In an ideal world, we'd enforce this USD invariant
            // on data as it enters our system -- a process far removed from our
            // feature.
            //
            // The types are telling us that something is wrong with the design **of the system**.
            //
            // We don't have to fix it now, but we should call it out.
            static USD unsafeGetChargesInUSD(LineItem lineItem) throws IllegalArgumentException {
                if (!lineItem.getCurrency().getCurrencyCode().equals("USD")) {
                    // If this ever throws, the system as a whole is in a bad state.
                    throw new IllegalArgumentException("Big scary message here");
                } else {
                    return new USD(lineItem.getCharges());
                }
            }

            // Putting it all together, we get:
            static USD computeTotal(List<PastDue> invoices) {
                return invoices.stream().map(PastDue::invoice)
                        .flatMap(x -> x.getLineItems().stream())
                        .map(V2::unsafeGetChargesInUSD)
                        .reduce(USD.zero(), USD::add);
            }
        }
    }
}
