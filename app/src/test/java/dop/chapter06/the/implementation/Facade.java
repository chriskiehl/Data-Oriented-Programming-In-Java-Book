package dop.chapter06.the.implementation;

import dop.chapter05.the.existing.world.Entities;
import dop.chapter05.the.existing.world.Repositories;
import dop.chapter05.the.existing.world.Services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


public class Facade {
    private Services.RatingsAPI ratingsApi;
    private Services.ContractsAPI contractsApi;
    private Services.ApprovalsAPI approvalsApi;
    private Services.BillingAPI billingApi;
    private Repositories.CustomerRepo customerRepo;
    private Repositories.InvoiceRepo invoiceRepo;
    private Repositories.RulesRepo rulesRepo;
    private Repositories.FeesRepo feesRepo;

    public Stream<Types.EnrichedCustomer> findAll() {
        return customerRepo.findAll().stream().map(this::enrich);
    }

    private Types.EnrichedCustomer enrich(Entities.Customer customer) {
        String country = customer.getAddress().getCountry();
        Types.Percent feePercentage = new Types.Percent(feesRepo.get(country).doubleValue(), 1.0);
        Services.RatingsAPI.CustomerRating rating = ratingsApi.getRating(customer.getId());
        Services.ContractsAPI.PaymentTerms terms = contractsApi.getPaymentTerms(customer.getId());
        String approvalId = customer.getApprovalId();
        Optional<Services.ApprovalsAPI.Approval> status = Objects.isNull(approvalId)
                ? Optional.empty()
                : this.approvalsApi.getApproval(approvalId);

        return new Types.EnrichedCustomer(
                new Types.CustomerId(customer.getId()),
                customer.getAddress(),
                feePercentage,
                terms,
                rating,
                status
        );
    }
}
