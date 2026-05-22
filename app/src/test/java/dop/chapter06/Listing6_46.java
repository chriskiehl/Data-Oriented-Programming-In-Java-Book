package dop.chapter06;

import java.math.BigDecimal;
import java.util.Optional;

public class Listing6_46 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.46
     * ───────────────────────────────────────────────────────
     * The Enriched Customer model
     * ───────────────────────────────────────────────────────
     */
    record EnrichedCustomer(
       String id,
       Address address,
       Percent feePercentage,
       PaymentTerms terms,
       CustomerRating rating,
       Optional<Approval> approval
//                        ▲
//                        └──── Approval is optional because they’re only
//                              created as needed
    ){}















    record Address(String value) {}
    record Percent(BigDecimal value) {}
    enum PaymentTerms {NET_30}
    enum CustomerRating {GOOD}
    record Approval(String value) {}
}
