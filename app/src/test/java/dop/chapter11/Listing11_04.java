package dop.chapter11;

import java.time.Clock;
import java.time.LocalDate;

public class Listing11_04 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.4
     * ───────────────────────────────────────────────────────
     * Bad implementation: commingling how we get data and what we do with it
     * ───────────────────────────────────────────────────────
     */
    class InvoiceService {
        Clock clock;
        RatingsAPI ratingsAPI;

        public InvoiceService(Clock clock, RatingsAPI ratingsAPI) {
            this.clock = clock;
            this.ratingsAPI = ratingsAPI;
        }

        public boolean isPastDue(Invoice invoice) {
            //                     ▲
            //                     └──── The function no longer takes everything it needs as input
            LocalDate currentDate = LocalDate.now(this.clock);    //  ┐
            CustomerRating rating = this.ratingsAPI.getRating(    //  │ Instead, it relies on instance state
                invoice.customerId()                              //  │ and external side effects :(
            );                                                    //  ┘
            return invoice.type().equals(InvoiceType.STANDARD)
                && invoice.status().equals(Status.OPEN)
                && currentDate.isAfter(invoice.dueDate()          //
                   .with(gracePeriod(rating)));                   //  ◄── Our business logic is robbed of determinism
        }
    }














    enum CustomerRating{GOOD, POOR;}
    enum InvoiceType { STANDARD, LATEFEE;}
    enum Status{ OPEN, CLOSED;}
    record CustomerId(String value){}
    record Invoice(CustomerId customerId, InvoiceType type, Status status, LocalDate dueDate){}
    interface RatingsAPI {
        CustomerRating getRating(CustomerId id);
    }
    static java.time.temporal.TemporalAdjuster gracePeriod(CustomerRating rating) {
        return d -> d;
    }
}
