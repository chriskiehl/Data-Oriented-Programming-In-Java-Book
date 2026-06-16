package dop.chapter05;

import dop.chapter05.the.existing.world.Entities.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class Listing5_21_to_5_24 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.21 through 5.26
   * ───────────────────────────────────────────────────────
   * Now we begin the data modeling!
   *
   * We invented 7 new data types. These all need designed and
   * modeled. We begin with the "easy" one: PastDue
   */
  @Test
  public void example() {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.21
     * ───────────────────────────────────────────────────────
     * Creating the PastDue type
     * ───────────────────────────────────────────────────────
     */
    // One way of capturing "something we know about a state" is
    // through wrapper types. It could be as simple as this.
    record PastDue(Invoice invoice) {
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.22
     * ───────────────────────────────────────────────────────
     * A closer look
     * ───────────────────────────────────────────────────────
     */
    // However, if we ask the "semantic" questions we learned about
    // in chapters 3 & 4, we'll find that this doesn't quite meet
    // the standards we've been chasing.
    //
    // "What does it mean to be past due?"
    //
    // The entire notion of "past due" depends on *time*
    /*
    record PastDue(Invoice invoice) {
      PastDue {
        if (invoice.dueDate().isBefore(???)) {
                                        ▲
        }                               └──── What goes here??
    }
     */

    // We're in new modeling territory. We cannot defend what this data
    // type "is" via the constructor, because its meaning is contextual
    // and comes from "outside" of it

    // outside context is what makes it different from what we looked
    // at before. Types like NonNegativeInt have everything they need
    // to enforce their semantics "inside" of the constructor.
    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.23
     * ───────────────────────────────────────────────────────
     * Revisiting NonNegativeInt
     * ───────────────────────────────────────────────────────
     */
    record NonNegativeInt(int value) {
      NonNegativeInt {
        if (value < 0) {
          throw new IllegalArgumentException("Nope");
        }
      }
    }

    /**
     * ───────────────────────────────────────────────────────
     * Listing 5.24
     * ───────────────────────────────────────────────────────
     * The meaning of “past due” is contextual and depends on outside info
     * ───────────────────────────────────────────────────────
     */
    // You might try to make the wrapper type carry this outside context.
    record PastDueV2 (
        Invoice invoice,
        LocalDate lateAsOf   // ◄── We could use this to defend the semantics
    ) {/*...*/}              //     during construction. But should we...?

    // The real world is messy. The bulk of the PastDue data type's value is what it
    // communicates *about* the requirements. It's still useful if it's squishy!

    // These two transforms communicate very different things.
    // List<Invoice> -> LateFee
    // List<PastDue> -> LateFee
  }

}
