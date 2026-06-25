package dop.chapter07;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Listing7_26 {
  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.26
   * ───────────────────────────────────────────────────────
   * Every possible state that RawData could represent
   * ───────────────────────────────────────────────────────
   */
  static Set<RawData> everyPossibleRow() {
    Set<RawData> output = new HashSet<>();                               //  ─┐
    for (var policy : everyOptionalValue(Policy.values())) {             //   │
      for (var finding : everyOptionalValue(AuditFinding.values())) {    //   │ This generates every possible state
        for (var premium : everyOptionalValue(true, false)) {            //   │ that our RawData type could be for an
          output.add(new RawData(                                        //   │ individual customer ID.
              "FixedCustomerId",                                         //   │
              policy,                                                    //   │
              finding,                                                   //   │
              premium)                                                   //  ─┘
          );
        }
      }
    }
    return output;
  }

  @SafeVarargs
  static <A> Set<Optional<A>> everyOptionalValue(A... items) {
//                                 ▲
//                                 └──── This helper makes sure every
//                                       Optional value (including empty) gets
//                                       accounted for
    return Stream.concat(
        Stream.of(items).map(Optional::of),
        Stream.of(Optional.<A>empty())
    ).collect(Collectors.toSet());
  }








  public enum Policy {
    GRACE_PERIOD,
    FLEXIBLE,
    IMMEDIATE,
    STRICT,
    MANUAL_REVIEW
  }

  public enum AuditFinding {
    NO_ISSUE,
    INACCURATE,
    BILLING_ERROR,
    OUT_OF_COMPLIANCE
  }

  record RawData(
      String id,
      Optional<Policy> policy,
      Optional<AuditFinding> findings,
      Optional<Boolean> isPremium
  ) {}

}
