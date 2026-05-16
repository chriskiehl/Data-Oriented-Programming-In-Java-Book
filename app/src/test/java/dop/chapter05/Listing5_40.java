package dop.chapter05;

public class Listing5_40 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.40
     * ───────────────────────────────────────────────────────
     * Representing the decision in our system
     * ───────────────────────────────────────────────────────
     */
    sealed interface ReviewedFee {
        record Billable(Latefee<Draft> fee)                      //  ┐
            implements ReviewedFee{}                             //  │
        record NeedsReview(Latefee<Draft> fee)                   //  │◄── Each of these data types specifies the
            implements ReviewedFee{}                             //  │    exact life cycle that’s relevant. No
        record NotBillable(Latefee<Draft> fee, Reason reason)    //  ┘    run time checks needed!
            implements ReviewedFee{}
    }





    record Draft() {}
    record Latefee<State>(State state) {}
    record Reason(String value) {}
}
