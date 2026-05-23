package dop.chapter10;

public class Listing10_2 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.2
     * ───────────────────────────────────────────────────────
     * an example Record
     * ───────────────────────────────────────────────────────
     */
    void example() {
        TableRecord r = TableRecord().builder()
            .set("invoice_id", "0001234")
            .set("account_id", "800879507")
            .set("invoice_date", "2026-01-01")
            .set("due_date", "2026-02-01")
            .set("total", 500.12);
            // and so on...
    }














    static TableRecord TableRecord() {
        return new TableRecord();
    }

    static class TableRecord {
        TableRecord builder() {
            return this;
        }
        TableRecord set(String key, Object value) {
            return this;
        }
    }
}
