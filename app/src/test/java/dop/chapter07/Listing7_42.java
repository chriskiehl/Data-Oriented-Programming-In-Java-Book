package dop.chapter07;

import org.junit.jupiter.api.Test;

import java.util.List;

import static dop.chapter07.Listing7_42.AuditFinding.BILLING_ERROR;
import static dop.chapter07.Listing7_42.AuditFinding.INACCURATE;
import static dop.chapter07.Listing7_42.AuditFinding.NO_ISSUE;
import static dop.chapter07.Listing7_42.AuditFinding.OUT_OF_COMPLIANCE;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Listing7_42 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.42
   * ───────────────────────────────────────────────────────
   * Verifying ordering properties by checking all permutations
   * ───────────────────────────────────────────────────────
   */
  static <A> List<List<A>> permutations(A[] items) {
    // The implementation of this permutation
    // function is left as an exercise!

    // Sure, AI could one-shot it for you, but it's nice
    // to shake of the rust every once in a while. 
    return List.of(); // (so it compiles)
  }

  @Test
  void anExerciseForTheReader() {
    List<AuditFinding> expectedOrder = List.of(
      INACCURATE, NO_ISSUE,
      BILLING_ERROR, OUT_OF_COMPLIANCE);

  assertTrue(
      permutations(AuditFinding.values())
        .stream()
        .map(x -> x.stream().sorted(comparator).toList())
        .allMatch(sorted -> sorted.equals(expectedOrder)));
//      ▲
//      └──── If every possible input permutation produces the same sorted
//            output, we’ve proven our ordering properties hold
  }








  enum AuditFinding {
    BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
  }

  java.util.Comparator<AuditFinding> comparator = java.util.Comparator.comparing(AuditFinding::name);

}
