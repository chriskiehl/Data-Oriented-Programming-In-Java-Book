package dop.chapter11;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_12 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing[EH6.1] 11.12
     * ───────────────────────────────────────────────────────
     * A typical Java test
     * ───────────────────────────────────────────────────────
     */
    class Example {
        MyService service;
        
        @BeforeEach
        void setup() {/*...*/}

        static Account accountA = new Account(/*...*/);
        static Account accountB = new Account(/*...*/);
        static Account accountC = new Account(/*...*/);


        @Test
        void noDiscountForLargeAccounts() {
            //         ▲
            //         └──── If we’re lucky, we get a breadcrumb explaining what the
            //               test is about in the method name
            Set<Account> items = Set.of(accountA, accountB, accountC);
            //                        └──────────────────────────────┘
            //                                       ▲
            //                                       └──── But everything else is opaque. What’s special
            //                                             about these accounts?
            Set<Result> result = this.service.applyDiscounts(items);
            //            ▲
            //            └──── Why do we expect this result?
            //          ┌─┐
            assertEquals(2, result.size());
            //          └─┘
            //           ▲
            //           └──── There is no connection between this assertion and the rest
            //                 of the code. Why 2!? What does it mean!?
        }
    }














    MyService service;
    record Account(){}
    record Result(String id){}
    interface MyService {
        Set<Result> applyDiscounts(Set<Account> accounts);
    }
}
