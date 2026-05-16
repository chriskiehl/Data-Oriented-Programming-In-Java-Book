package dop.chapter05;

import java.time.LocalDate;
import java.util.List;

import static dop.chapter05.the.existing.world.Entities.Invoice;

public class Listing5_26 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.26
     * ───────────────────────────────────────────────────────
     * What different input types communicate
     * ───────────────────────────────────────────────────────
     */
    // LateFeeDraft createLateFeeDraft(List<Invoice> invoices) {...}
    // LateFeeDraft createLateFeeDraft(List<PastDue> invoices) {...} // ◄─── Only one of these tells the truth
}
