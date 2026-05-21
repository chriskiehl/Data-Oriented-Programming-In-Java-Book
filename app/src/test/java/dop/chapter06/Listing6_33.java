package dop.chapter06;

import java.time.LocalDate;
import java.util.List;

public class Listing6_33 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.33
     * ───────────────────────────────────────────────────────
     * Following the types
     * ───────────────────────────────────────────────────────
     */
    public static List<PastDue> collectPastDue(
            EnrichedCustomer customer,
            LocalDate today,
            List<Invoice> invoices) {
        //           ▲
        //           └──── The shape of the implementation follows what the types say
        return invoices.stream()
                //                    ┌───── This is the only question no answered by our types
                //                    ▼
                .filter(invoice -> /*???*/ __)
                .map(PastDue::new)
                //      ▲
                //      └─ But everything else is! The function MUST produce this type
                .toList();
    }


    record EnrichedCustomer() {
    }

    record Invoice() {
    }

    record PastDue(Invoice invoice) {
    }

    static boolean __ = false;
}
