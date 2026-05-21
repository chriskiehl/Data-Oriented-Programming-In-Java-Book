package dop.chapter06;

import java.util.function.Function;

public class Listing6_18 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.18
     * ───────────────────────────────────────────────────────
     * Combining deterministic functions produces new deterministic functions
     * ───────────────────────────────────────────────────────
     */
    void example() {
        var anotherDeterminisitcFunction = collectPastDue.andThen(buildDraft);
                                        //      ▲
                                        //      └──── If collectPastDue and buildDraft are both deterministic functions,
                                        //            then their combination is also a deterministic function!
    }




























    record Latefee(){}
    record PastDue(){}
    record Draft(){}
    static Function<Latefee,PastDue> collectPastDue = (latefee -> new PastDue());
    static Function<PastDue, Draft> buildDraft = (due -> new Draft());
}
