package dop.chapter07;

import java.util.Optional;

public class Listing7_50 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.50
   * ───────────────────────────────────────────────────────
   * The messy reality that every field is potentially null
   * ───────────────────────────────────────────────────────
   */
  record RawData(
      String id,
      Optional<Policy> policy,          //  ┐
      Optional<AuditFindings> findings, //  │◄── The messy reality is that every row is
      Optional<Boolean> premiumStatus   //  ┘    potentially a sea of undefined data
  ) {}








  enum Policy {
    GRACE_PERIOD, FLEXIBLE, IMMEDIATE, STRICT, MANUAL_REVIEW
  }

  enum AuditFindings {
    BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
  }

}
