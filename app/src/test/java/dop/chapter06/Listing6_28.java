package dop.chapter06;

public class Listing6_28 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.28
     * ───────────────────────────────────────────────────────
     * The simplified view of how data flows through our program
     * ───────────────────────────────────────────────────────
     */
    /*
                                NOTES.md
    -------------------------------------------------------------------
    // step 1: collect the past due
    List<Invoice> -> List<PastDue>

    // Step 2: use those to build the draft
    List<PastDue> -> LateFee<Draft>

    // Step 3: decide what to do with the draft
    LateFee<Draft> -> (Billable OR NotBillable OR NeedsReview

    // then depending on what we decided, either:
    // Step 4.1: submit billable items
    Billable -> (LateFee<Billed> OR LateFee<Rejected>)

    // OR Step 4.2: Start the approval process for those that need it
    NeedsReviewed -> LateFee<InReview>

    // OR Step 4.3: keep the non-billable data for posterity
    NotBillable -> LateFee<Rejected>
    */
}
