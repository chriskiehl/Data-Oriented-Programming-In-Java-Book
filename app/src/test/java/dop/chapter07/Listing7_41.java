package dop.chapter07;

import java.util.Comparator;

import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Listing7_41 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.41
     * ───────────────────────────────────────────────────────
     * Verifying the low-level properties that power Comparator
     * ───────────────────────────────────────────────────────
     */
    Comparator<AuditFinding> comparator =           //  ┐
            comparing(Chapter7::findingsImpact)     //  │◄── We’ll just look at just one of the
                .thenComparing(AuditFinding::name); //  ┘    data types for ease of example.

    void testReflexivity() {
      for (AuditFinding a : AuditFinding.values()) {
        assertTrue(comparator.compare(a, a) <= 0);
//      ▲
//      └──── Reflexivity might seem like a pointless thing to assert,
//            but it’s possible to be violated if your implementation
//            has customized its comparator implementation
      }
    }

    void testAntisymmetry() {
      for (AuditFinding a : AuditFinding.values()) {
        for (AuditFinding b : AuditFinding.values()) {  //
          if (comparator.compare(a, b) <= 0             //  ┐    Verifying antisymmetry holds. This is
              && comparator.compare(b, a) <= 0) {       //  │ ◄─ another one that seems too obvious to
            assertEquals(a, b);                         //  ┘    bother with, but it actually caught a
                                                        //       bug in my original implementation while
                                                        //       working on this chapter!


          }
        }
      }
    }

    void testTransitivity() {
      for (AuditFinding a : AuditFinding.values()) {
        for (AuditFinding b : AuditFinding.values()) {
          for (AuditFinding c : AuditFinding.values()) {
            if (comparator.compare(a, b) <= 0                //  ┐
                  && comparator.compare(b, c) <= 0) {        //  │ ◄── Verifying that
                assertTrue(comparator.compare(a, c) <= 0);   //  ┘     transitivity holds

            }
          }
        }
      }
    }














    enum AuditFinding {
        BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
    }

    enum CustomerImpact { HARMS, FAVORS }

    static class Chapter7 {
        static CustomerImpact findingsImpact(AuditFinding finding) {
            return CustomerImpact.FAVORS;
        }
    }
}
