package dop.chapter05;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import dop.chapter05.the.existing.world.Entities.Address;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.Approval;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;

public class Listing5_49_to_5_50 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.49 through 5.50
   * ───────────────────────────────────────────────────────
   * A big theme that permeates this chapter is that engineering
   * is contextual. We're always making tradeoffs.
   *
   * In this listing we explore a refactoring that we *might* want
   * to do. The "cost" here is that we begin carving into the world
   * of "what's already there" and rebuilding it into a new shape.
   *
   * This is a valuable thing to do in most codebases, but not always
   * the "right" thing to do as part of an individual feature.
   */
  @Test
  public void example() {
    // ┌────────────────────────────────────────────────────────────────────┐
    // │        Decoupling from the Tyranny of What's Already There         │
    // └────────────────────────────────────────────────────────────────────┘
    //


    // We don't have to settle for the world as we found it -- as it was dictated
    // by other people. We can unify disparate services and APIs.
    //
    // Design is about making the world as we want it to be.
    //
    // An obvious problem with the service oriented hellscape in which our feature
    // lives is that the boundaries, which may be totally sane at the system level, do
    // not fit what we're trying to do at the service level. They force our data to be
    // fragmented.
    //
    // We can hide these arbitrary service boundaries behind a new data type.
    record EnrichedCustomer(
        CustomerId id,
        Address address,
        Percent feePercentage,
        PaymentTerms terms,
        CustomerRating rating,
        Optional<Approval> approval
    ) {}
  }






  // minimal shims just to enable the main example.
  record CustomerId() {}
  record Percent() {}

}
