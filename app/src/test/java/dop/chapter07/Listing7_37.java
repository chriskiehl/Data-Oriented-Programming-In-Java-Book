package dop.chapter07;

public class Listing7_37 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.37
   * ───────────────────────────────────────────────────────
   * Implementing binary operations for each individual type
   * ───────────────────────────────────────────────────────
   */
  static RawData add(RawData x, RawData y) {
//               ▲
//               └──── Here’s the main binary operation that combines our data
    if (!x.id().equals(y.id())) {               //  ┐
      throw new IllegalArgumentException(       //  │◄── The algebra is only defined for records
          "Hey! Only conflicting rows allowed!" //  │    which conflict, thus the defense here
      );                                        //  │
    }                                           //  ┘
    return new RawData(
        x.id(),
        add(x.policy(), y.policy()),              //  ┐
        add(x.findings(), y.findings()),          //  │◄── Algebraic approaches produce these
        add(x.isPremium(), y.isPremium())         //  ┘    pleasing, uniform top-level APIs
    );
  }


  static Policy add(Policy x, Policy y) {
    // First we check to see if they have the same customer impact (req. A2)
    if (policyImpact(x).equals(policyImpact(y))) {
      // If they do, we resolve by an arbitrary lexicographical sort (req. A3)
      return x.name().compareTo(y.name()) >= 0 ? x : y;
    } else {
      // Otherwise, we take whichever one is more favorable (req. A1)
      return policyImpact(x).equals(CustomerImpact.FAVORS) ? x : y;
    }
  }
  static AuditFinding add(AuditFinding x, AuditFinding y) {
    // Here we perform the exact same logic.
    if (findingsImpact(x).equals(findingsImpact(y))) {
      return x.name().compareTo(y.name()) >= 0 ? x : y;
    } else {
      return findingsImpact(x).equals(CustomerImpact.FAVORS) ? x : y;
    }
  }
  static Boolean add(Boolean x, Boolean y) {
    return x || y;
//         ▲
//         └──── Booleans are easy as pie. Take whichever is true
//               (i.e. favors the customer by making them a
//               “premium” account)
  }








  public enum Policy {
    GRACE_PERIOD, FLEXIBLE, IMMEDIATE, STRICT, MANUAL_REVIEW
  }

  public enum AuditFinding {
    BILLING_ERROR, OUT_OF_COMPLIANCE, INACCURATE, NO_ISSUE
  }

  enum CustomerImpact { HARMS, FAVORS }

  record RawData(
      String id,
      Policy policy,
      AuditFinding findings,
      Boolean isPremium
  ) {}

  static CustomerImpact policyImpact(Policy stage) {
    return CustomerImpact.FAVORS;
  }

  static CustomerImpact findingsImpact(AuditFinding stage) {
    return CustomerImpact.FAVORS;
  }

}
