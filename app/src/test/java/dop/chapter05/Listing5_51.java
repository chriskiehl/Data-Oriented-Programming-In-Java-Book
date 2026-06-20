package dop.chapter05;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import dop.chapter05.the.existing.world.Entities.Address;
import dop.chapter05.the.existing.world.Entities.Customer;
import dop.chapter05.the.existing.world.Repositories.CustomerRepo;
import dop.chapter05.the.existing.world.Repositories.FeesRepo;
import dop.chapter05.the.existing.world.Repositories.InvoiceRepo;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.Approval;
import dop.chapter05.the.existing.world.Services.ContractsAPI;
import dop.chapter05.the.existing.world.Services.ContractsAPI.PaymentTerms;
import dop.chapter05.the.existing.world.Services.RatingsAPI;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;

public class Listing5_51 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.51
   * ───────────────────────────────────────────────────────
   * An improved access layer for retrieving customers
   * ───────────────────────────────────────────────────────
   */
  class StorageFacade {               // ◄── This façade ties together a whole host of storage and service calls
    private CustomerRepo customerRepo;   //  ┐
    private ContractsAPI contractsApi;   //  │
    private RatingsAPI ratingsApi;       //  │◄── All of these service boundaries are arbitrary and things
    private FeesRepo feeRepo;            //  │    our application doesn't care about.
    private ApprovalsAPI approvalsApi;   //  │    We want the data; we don’t care about how.
    private InvoiceRepo invoiceRepo;     //  ┘

    public Stream<EnrichedCustomer> findAll() {
      return customerRepo.findAll().stream().map(this::enrich);
    }

    private EnrichedCustomer enrich(Customer customer) { // ◄── This one method keeps so much noise out of the rest of our application.
      Address address = customer.getAddress();
      BigDecimal feeAmount = feeRepo.get(address.getCountry());
      CustomerRating rating = ratingsApi.getRating(customer.getId());
      PaymentTerms terms = contractsApi.getPaymentTerms(customer.getId());
      String approvalId = customer.getApprovalId();
      Optional<Approval> approval = Optional.ofNullable(approvalId)
          .flatMap(this.approvalsApi::getApproval);

      return new EnrichedCustomer(
          new CustomerId(customer.getId()),
          customer.getAddress(),
          toPercentage(feeAmount),
          terms,
          rating,
          approval
      );
    }
  }


  record EnrichedCustomer(
      CustomerId id,
      Address address,
      Percent feePercentage,
      PaymentTerms terms,
      CustomerRating rating,
      Optional<Approval> approval
  ) {}


  // minimal shims just to enable the main example.
  record CustomerId(String value) {}

  record Percent(double numerator, double denominator) {
    Percent {
      if (numerator > denominator) {
        throw new IllegalArgumentException(
            "Percentages are 0..1 and must be expressed " +
            "as a proper fraction. e.g. 1/100");
      }
    }
  }

  Percent toPercentage(BigDecimal value) {
    // Implementation skipped for brevity.
    // In practice, you would want to do this in your
    // data layer so that it's done once, thoroughly tested,
    // and never thought about again.
    return new Percent(1, 1);
  }

}
