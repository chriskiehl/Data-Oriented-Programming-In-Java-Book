package dop.chapter05;

public class Listing5_13 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.13
     * ───────────────────────────────────────────────────────
     * A decision and its actions
     * ───────────────────────────────────────────────────────
     */
    void example() {
        if (someCondition) {   //  ◄──┐ The decision and its action
            doSomeAction();    //  ◄──┐ are executed together
        } else {
           doSomeOtherAction();
        }
    }




    // just so the example compiles
    boolean someCondition;
    void doSomeAction() {}
    void doSomeOtherAction() {}
}
