package dop.chapter11;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static dop.chapter11.Listing11_5.CustomerRating.GOOD;
import static dop.chapter11.Listing11_5.InvoiceType.STANDARD;
import static dop.chapter11.Listing11_5.Status.OPEN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Listing11_5 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.5
     * ───────────────────────────────────────────────────────
     * Testing code that’s not data oriented
     * ───────────────────────────────────────────────────────
     */
    class Example {
        private Clock clock;
        private InvoiceService invoiceService;
        private RatingsAPI ratingsAPI;

        @BeforeEach
        void setup() {                                                //  ┐
            this.clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);  //  │
            this.ratingsAPI = mock(RatingsAPI.class);                 //  │◄── First we’ve got to set
            this.invoiceService = new InvoiceService(                 //  │    up our mocks and wire
                                                                      //  │    everything together
                this.clock, this.ratingsAPI                           //  │
             );                                                       //  ┘
        }

        @Test
        void goodCustomersGetTheMaximumGracePeriod() {
            when(ratingsAPI.getRating(anyString()))  //
                .thenReturn(CustomerRating.GOOD);    // ◄── Then we’ve got to tell the mock
                                                     //     what to do for this test

            assertFalse(this.invoiceService.isPastDue(
                new Invoice(
                    new CustomerId("1"),
                    STANDARD,
                    OPEN,
                    LocalDate.now(this.clock).minusDays(MAX_GRACE_PERIOD)
                    //                            ▲
                    //                            └──── and then craft the rest of our inputs around
                    //                                  this mocked state. This only works because of
            )));    //                                  something we've mocked in setup(); 
        }
    }














    static int MAX_GRACE_PERIOD = 60;
    enum CustomerRating{GOOD, POOR;}
    enum InvoiceType { STANDARD, LATEFEE;}
    enum Status{ OPEN, CLOSED;}
    record CustomerId(String value){}
    record Invoice(CustomerId customerId, InvoiceType type, Status status, LocalDate dueOn){}
    interface RatingsAPI {
        CustomerRating getRating(String id);
    }
    static class InvoiceService {
        InvoiceService(Clock clock, RatingsAPI ratingsAPI) {}
        boolean isPastDue(Invoice invoice) {
            return false;
        }
    }
}
