package dop.chapter05;

public class Listing5_38 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.38
     * ───────────────────────────────────────────────────────
     * behaviors can communicate and enforce their own invariants
     * ───────────────────────────────────────────────────────
     */
    void doSomething1(LateFee<Draft> fee) { /*...*/}               //  ┐
    void doSomething2(LateFee<Billed> fee) { /*...*/}              //  │
    void doSomething3(LateFee<Rejected> fee) { /*...*/}            //  │◄── Methods can demand data in whatever life cycle state they need
    void doSomething4(LateFee<InReview> fee) { /*...*/}            //  ┘
    void doSomething5(LateFee<? extends Lifecycle> fee) { /*...*/} // ◄── Or leave it completely unstated if they don’t care
    void doSomething6(LateFee fee) { /*...*/}
    //                   ▲
    //                   └───  You can even leave off the generic entirely,
    //                         which will produce a useful warning in Java. (This can be a
    //                         great technique for slowly adding pockets of type safety to
    //                         legacy code without needing buy in on a full refactor)





    sealed interface Lifecycle {}
    record Draft() implements Lifecycle {}
    record Billed() implements Lifecycle {}
    record Rejected() implements Lifecycle {}
    record InReview() implements Lifecycle {}
    record LateFee<State extends Lifecycle>(State state) {}
}
