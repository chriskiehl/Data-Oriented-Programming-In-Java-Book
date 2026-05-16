package dop.chapter05;

import dop.chapter05.Listing5_14.Decision.Option1;
import dop.chapter05.Listing5_14.Decision.Option2;
import dop.chapter05.Listing5_14.Decision.Option3;

public class Listing5_14 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.14
     * ───────────────────────────────────────────────────────
     * Interpreting the decisions
     * ───────────────────────────────────────────────────────
     */
    String doSomethingWithTheDecision(Decision decision) {     // ◄── we can interpret the decisions however we want.
        return switch(decision) {
            case Option1 op -> "I compute something with option1";  //   ◄──┐ We might compute more data
            case Option2 op -> "I compute something with option2";  //      │
            case Option3 op -> "I compute something with option3";  //      │
        };
    }

    void doSomethingElse(Decision decision) {          // ◄── Or kick off some action.
        switch(decision) {
            case Option1 op -> sendReminderEmail();
            case Option2 op -> startProbation();
            case Option3 op -> suspendAccount();
        };
    }









    sealed interface Decision {
        record Option1() implements Decision {}
        record Option2() implements Decision {}
        record Option3() implements Decision {}
    }

    void sendReminderEmail(){}
    void startProbation(){}
    void suspendAccount(){}
}
