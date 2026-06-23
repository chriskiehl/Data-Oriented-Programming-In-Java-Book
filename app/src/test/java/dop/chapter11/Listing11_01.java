package dop.chapter11;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static dop.chapter11.Listing11_01.InvoiceType.STANDARD;
import static dop.chapter11.Listing11_01.Status.OPEN;

public class Listing11_01 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.1
     * ───────────────────────────────────────────────────────
     * Pure deterministic functions from Chapter 6
     * ───────────────────────────────────────────────────────
     */
    enum CustomerRating{GOOD, POOR;}             //  ┐
    enum InvoiceType { STANDARD, LATEFEE;}       //  │
    enum Status{ OPEN, CLOSED;}                  //  │◄── As a refresher, here are the
    record Invoice(                              //  │    simplified data types involved
        CustmerId customerId,                    //  │
        InvoiceType type,                        //  │
        Status status,                           //  │
        LocalDate dueDate                        //  │
        // (other fields elided)                 //  │
    ){}                                          //  ┘

    // Pure, deterministic functions make testing a delight!
    static boolean isPastDue(
        Invoice invoice,              //  ┐
        CustomerRating rating,        //  │◄── Everything the function depends
        LocalDate currentDate         //  ┘    on is taken as an input
    ) {
        return invoice.type().equals(STANDARD)                //  ┐
            && invoice.status().equals(OPEN)                  //  │◄── And all it just returns new data
            && currentDate.isAfter(                           //  │    as output
                invoice.dueDate().with(gracePeriod(rating))); //  ┘
    }

    static TemporalAdjuster gracePeriod(CustomerRating rating) {
//                          ▲
//                          └──── Same for this one
        return switch (rating) {
            case POOR  -> TemporalAdjusters.lastDayOfMonth();
            case GOOD -> (d) -> d.plus(60, ChronoUnit.DAYS);
        };
    }














    record CustmerId(String value){}
}
