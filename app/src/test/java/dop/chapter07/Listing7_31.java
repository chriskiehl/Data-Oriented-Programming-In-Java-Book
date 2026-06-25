package dop.chapter07;

import java.util.Optional;

public class Listing7_31 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.31
   * ───────────────────────────────────────────────────────
   * A quick reminder of the main data types
   * ───────────────────────────────────────────────────────
   */
  public enum Policy {
    GRACE_PERIOD, FLEXIBLE, IMMEDIATE, STRICT, MANUAL_REVIEW
  }

  public enum AuditFinding {
    BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
  }

  record RawData(
      String id,
      Optional<Policy> policy,
      Optional<AuditFinding> findings,
      Optional<Boolean> isPremium
  ) {}

}
