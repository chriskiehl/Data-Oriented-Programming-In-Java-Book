package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;

import static dop.chapter11.Listing11_02.CustomerRating.GOOD;
import static dop.chapter11.Listing11_02.InvoiceType.STANDARD;
import static dop.chapter11.Listing11_02.Status.OPEN;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Listing11_02 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.2
     * ───────────────────────────────────────────────────────
     * Tests are structured around inputs and outputs
     * ───────────────────────────────────────────────────────
     */
    class Example {
        @Test
        void goodCustomersGetTheMaximumGracePeriod() {
            // Deterministic functions let us work locally.
            // Everything needed to understand this test is present in the body
            LocalDate dueOn = LocalDate.now();                           //  ┐
            LocalDate currentDate = dueOn.plusDays(MAX_GRACE_PERIOD);    //  │
                                                                         //  │
            boolean result = InvoiceService.isPastDue(                   //  │
                new Invoice(new CustomerId("1"), STANDARD, OPEN, dueOn), //  │◄── The test is simply feeding
                CustomerRating.GOOD,                                     //  │    a function inputs
                currentDate                                              //  │
            );                                                           //  ┘

            //            ┌──── And then asserting against the output
            //            ▼
            assertFalse(result, """                                      
                Customers in good standing are not considered past       
                due as long as they're within the grace period.          
                """);
        }
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
