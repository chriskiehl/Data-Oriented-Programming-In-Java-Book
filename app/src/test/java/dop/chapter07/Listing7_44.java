package dop.chapter07;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Listing7_44 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.44
     * ───────────────────────────────────────────────────────
     * Translating the monotonic law into Java
     * ───────────────────────────────────────────────────────
     */
    Comparator<AuditFinding> comp =
        comparing(Listings::findingsImpact)
          .thenComparing(AuditFinding::name);

    @Test
    void testMonotonicity() {
      for (AuditFinding a1 : AuditFinding.values()) {        //  ┐
        for (AuditFinding a2 : AuditFinding.values()) {      //  │◄── These variables could optionally be named
          for (AuditFinding b1 : AuditFinding.values()) {    //  │    a,b,c,d like previous examples, but this
            for (AuditFinding b2 : AuditFinding.values()) {  //  ┘    labeling tends to make the relationships
                                                             //       a bit clearer
              if (comp.compare(a1, b1) <= 0 && comp.compare(a2, b2) <= 0) {
                assertTrue(
                  comp.compare(add(a1, a2), add(b1, b2)) <= 0
                );
//              ▲
//              └──── The comparison between these operations is key. It
//                    verifies that no matter what we feed in as arguments,
//                    we only move “forward” in terms of favorability.
              }
            }
          }
        }
      }
    }














    enum AuditFinding {
        BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
    }

    enum CustomerImpact { HARMS, FAVORS }

    static class Listings {
        static CustomerImpact findingsImpact(AuditFinding finding) {
            return CustomerImpact.FAVORS;
        }
    }

    static AuditFinding add(AuditFinding x, AuditFinding y) {
        return x;
    }
}
