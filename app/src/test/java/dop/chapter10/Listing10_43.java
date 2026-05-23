package dop.chapter10;

import java.util.List;
import java.util.stream.Stream;

public class Listing10_43 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.43
     * ───────────────────────────────────────────────────────
     * Loading data from the Approval Service
     * ───────────────────────────────────────────────────────
     */
    void example() {
        List<Refund> refunds = db.fetchPending();
        List<Approval> approvals = refunds.stream()   //  ┐
            .map(Refund::approval)                    //  │◄── Streams make it feel like you’re
            .map(approvalService::getApproval)        //  │    not missing anything
            .toList();                                //  ┘
        // rest of the logic
    }

    void andThenSomewhereElseInTheCode() {
        // we do it again
        List<Approval> approvals = refunds.stream()    //  ┐
            .map(Refund::approval)                     //  │
            .map(approvalService::getApproval)         //  │◄── It’s so easy to do that it spreads.
            .toList();                                 //  │    People sprinkle it around wherever
    }                                                  //  │    they need it.


    void andAnotherPlace() {
        // It ends up ad-hoc all over the project
        /*...*/stuff.map(approvalService::getApproval).toList();
    }














    Db db;
    List<Refund> refunds;
    ApprovalService approvalService;
    static Stream<ApprovalId> stuff;

    record ApprovalId(String value){}
    record Approval(String value){}
    record Refund(ApprovalId approval){}
    interface Db {
        List<Refund> fetchPending();
    }
    interface ApprovalService {
        Approval getApproval(ApprovalId id);
    }
}
