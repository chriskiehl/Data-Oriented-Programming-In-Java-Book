package dop.chapter05;

import dop.chapter05.the.existing.world.Services.*;
import dop.chapter05.the.existing.world.Repositories.*;

public class Listing5_04 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.4
   * ───────────────────────────────────────────────────────
   * Where most features begin: a small army of services and
   * repositories.
   * ───────────────────────────────────────────────────────
   */
  class LateFeeChargingService {             //  ◄── The class where we write our business logic. We call it a “service” because that’s what most frameworks have us call these things.
    private RatingsAPI ratingsApi;           //  ◄──┐ The external services on which we depend
    private ContractsAPI contractsApi;       //     │
    private ApprovalsAPI approvalsApi;       //     │
    private BillingAPI billingApi;           //     │
    CustomerRepo customerRepo;               //  ◄──┐ The repositories (Data Access Objects) for all of our Entities
    InvoiceRepo invoiceRepo;                 //     │
    RulesRepo rulesRepo;                     //     │
    FeesRepo feesRepo;                       //     │

    public void processLateFees() {   // ◄── Now we're ready to start!
      // So we begin!
    }
  }

}
