package dop.chapter06;

import java.time.LocalDate;

public class Listing6_26 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.26
     * ───────────────────────────────────────────────────────
     * An alternative implementation
     * ───────────────────────────────────────────────────────
     */
    static LocalDate mustHavePaidBy(
//                   ▲
//                   └──── We have to jiggle the naming a bit, since “grace
//                         period” isn’t really about a specific date, it’s
//                         something added to an existing one.
            Invoice invoice,
            CustomerRating rating) {
        return switch(rating) {
            case CustomerRating.GOOD -> invoice.dueDate().plusDays(60);
                                    //              ▲
                                    //              └──── Applying the grace Period rules directly to the date
            default -> null;

            // etc...
        };
    }
    static boolean isPastDue(/*...*/) {
      return today.isAfter(mustHavePaidBy(invoice, rating));
//           ▲
//           └──── And this is fine, too! This approach is also delightfully clear!
    }















    static LocalDate today;
    static Invoice invoice;
    static CustomerRating rating;

    enum CustomerRating {GOOD, ACCEPTABLE, POOR}

    interface Invoice {
        LocalDate dueDate();
    }
}
