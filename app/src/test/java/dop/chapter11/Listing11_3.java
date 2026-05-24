package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static dop.chapter11.Listing11_3.CustomerRating.GOOD;
import static dop.chapter11.Listing11_3.InvoiceType.STANDARD;
import static dop.chapter11.Listing11_3.Status.CLOSED;
import static dop.chapter11.Listing11_3.Status.OPEN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Listing11_3 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.3
     * ───────────────────────────────────────────────────────
     * Verifying that “past due” only applies to invoices that are open
     * ───────────────────────────────────────────────────────
     */
    @Test
    void closedInvoicesCannotBePastDue() {
        LocalDate dueOn = LocalDate.now();
        LocalDate currentDate = dueOn.plusDays(MAX_GRACE_PERIOD + 1);
        //                                ▲
        //                                └──── We can build data from data. This computes a current
        //                                      date that’s outside the grace period.

        // That lets us test one of our business rules
        // Rule: A closed invoice cannot be past due regardless of any
        //       other invoice or customer attributes.
        for (CustomerRating rating : CustomerRating.values()) {         //
            for (InvoiceType type : InvoiceType.values()) {             //  ◄── We can build input data dynamically.
                assertFalse(InvoiceService.isPastDue(                   //
                    new Invoice(new CustomerId("1"), type, CLOSED, dueOn),
                    rating,
                    currentDate
                    ), "Closed invoices cannot be past due");
                }
            }

        // Contrast that with an open invoice that’s out of date.
        assertTrue(InvoiceService.isPastDue(                         //  ┐
            new Invoice(new CustomerId("1"), STANDARD, OPEN, dueOn), //  │◄── We paint a story by shaping the
            CustomerRating.GOOD,                                     //  │    data that we feed into the function
            currentDate                                              //  │    under test
        ));                                                          //  ┘

    }














    static int MAX_GRACE_PERIOD = 60;
    enum CustomerRating{GOOD, POOR;}
    enum InvoiceType { STANDARD, LATEFEE;}
    enum Status{ OPEN, CLOSED;}
    record CustomerId(String value){}
    record Invoice(CustomerId customerId, InvoiceType type, Status status, LocalDate dueOn){}
    static class InvoiceService {
        static boolean isPastDue(Invoice invoice, CustomerRating rating, LocalDate currentDate) {
            return false;
        }
    }
}
