package dop.chapter01;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;

public class Listing1_05_and_1_06 {

  /**
   * ───────────────────────────────────────────────────────
   * Listings 1.5 & 1.6
   * ───────────────────────────────────────────────────────
   * Representation affects our ability to understand the code
   * as a whole. The class below, ScheduledTask, is an example
   * I stole (after simplifying and anonymizing) from a project
   * I worked on. Without knowing anything other than that fact
   * that it deals with scheduling (which we can tell from its
   * name), the challenge we take on in the chapter is simply
   * trying to understand what the heck the `reschedule() method
   * is trying to do.
   * ───────────────────────────────────────────────────────
   */
  static class ScheduledTask {
    private LocalDateTime scheduledAt;
    private int attempts;

    static int DELAY = 30;


    //────────────────────────────────────────────────────────────┐
    void reschedule() {                                         //│ Checkout this method. What does it do?
      if (this.someSuperComplexCondition()) {                   //│ Or, more specifically, what does it mean?
        this.scheduledAt = now();                               //│ It clearly assigns some values to some
        this.attempts += 1;                                     //│ variables, but... we as newcomers to this
      } else if (this.someOtherComplexCondition()) {            //│ code, what information can we extract from
        this.scheduledAt = now().plusSeconds(DELAY);            //│ just this method?
        this.attempts = 0;                                      //│
      } else {                                                  //│
        this.scheduledAt = null;                                //│
        this.attempts = 0;                                      //│
      }                                                         //│
    }                                                           //│
    // ───────────────────────────────────────────────────────────┘



    // Below is just so the example compiles
    //─────────────────────────────────────────────────┐
    boolean someSuperComplexCondition() {          //  │ Note!
      return false;                                //  │ These are just here so the code will
    }                                              //  │ compile. They return fixed junk values.
    boolean someOtherComplexCondition() {          //  │ They should be ignored
      return false;                                //  │ for the purposes of the exercise.
    }                                              //  │
    int delay() {                                  //  │
      return 0;                                    //  │
    }                                              //  │
                                                   //  │
    private LocalDateTime standardInterval() {     //  │
      return now();                                //  │
    }                                              //  │
    //─────────────────────────────────────────────────┘

    LocalDateTime scheduledAt() {
      return scheduledAt;
    }

    int attempts() {
      return attempts;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
      this.scheduledAt = scheduledAt;
    }

    public void setAttempts(int attempts) {
      this.attempts = attempts;
    }
  }

}
