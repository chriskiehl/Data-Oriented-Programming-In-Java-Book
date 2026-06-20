package dop.chapter05;

import com.google.common.base.Strings;
import dop.chapter05.the.existing.world.Entities.*;
import dop.chapter05.the.existing.world.Repositories;
import dop.chapter05.the.existing.world.Repositories.FeesRepo;
import dop.chapter05.the.existing.world.Services;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI;
import dop.chapter05.the.existing.world.Services.ApprovalsAPI.ApprovalStatus;
import dop.chapter05.the.existing.world.Services.ContractsAPI;
import dop.chapter05.the.existing.world.Services.RatingsAPI;
import dop.chapter05.the.existing.world.Services.RatingsAPI.CustomerRating;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class Listing5_5_and_5_6 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.5 through 5.6
   * ───────────────────────────────────────────────────────
   * These listings tackle our first stab at an implementation.
   *
   * Author note:
   * I stressed over this example more than any other
   * in the book. It drives me crazy people use "strawman" examples
   * that are so comically bad that they obviously only exist so
   * that the author can swoop in with their magical and perfect
   * paradigm to fix the day. I wanted something that demonstrated
   * the usual problems we face in software development, but that
   * still felt "fair."
   *
   * The below might not be how you would have done it, but I hope
   * that it feels familiar. I think we've all written at least some
   * code like this. I know I have. It's "I just want to be done"
   * style code.
   *
   * It's of an assembly of what's already there rather than a
   * conscious design.
   */
  @Test
  public void example() {
    // These services are all defined in listing 5.1
    class LateFeeChargingService {             // ◄───────┐ The bet I make in the book is that this general
      private RatingsAPI ratingsApi;                  //  │ setup feels deeply familiar as a starting point.
      private ContractsAPI contractsApi;              //  │ We have an army of Entities, Service Classes, and
      private ApprovalsAPI approvalsApi;              //  │ Repositories (or "Data Access Objects" depending on
      private Services.BillingAPI billingApi;         //  │ your preferred lingo) all crammed into the top of a
      private Repositories.CustomerRepo customerRepo; //  │ a class that's usually called [Thing]Service.
      private Repositories.InvoiceRepo invoiceRepo;
      private Repositories.RulesRepo rulesRepo;
      private FeesRepo feesRepo;

      // It's here that we'll assemble all of this
      // pre-existing *stuff* into something that satisfies
      // our requirements.
      //
      // The below will surely look different that how you
      // might implement the same set of requirements, but
      // I hope that it feels familiar -- like something you
      // could have written (I've written *lots* of code like
      // this over the years). It's not terrible, but it's
      // also not good.
      //
      // Let's walk through it.
      public void processLatefees() {
        LocalDate today = LocalDate.now();                    // ─┐
                                                              //  │ Before we can start doing anything
        Rules rules = rulesRepo.loadDefaults();               //  │ useful, our method starts off doing
        for (Customer customer : customerRepo.findAll()) {    //  │ a bunch of busy work to poking and
          BigDecimal feePercentage = feesRepo.get(            //  │ prodding all of the existing stuff
              customer.getAddress().getCountry()              //  │ to get it into a shape we can use
          );                                                  //──┘
          List<Invoice> pastDueInvoices = getPastDueInvoices(customer); // (We'll look at this below)

          BigDecimal totalPastDue = getTotalPastDue(pastDueInvoices);  // ◄──┐ Here we finally start to write
          BigDecimal latefee = totalPastDue.multiply(feePercentage);   //    │ some business logic.

          Invoice latefeeInvoice = new Invoice();   //◄───────────┐ Now we begin the long, slow process
          latefeeInvoice.setInvoiceType(InvoiceType.LATEFEE);  // │ of building up our result.
          latefeeInvoice.setCustomerId(customer.getId());      // │
          latefeeInvoice.setInvoiceDate(today);                // │
          latefeeInvoice.setDueDate(this.figureOutDueDate());
          latefeeInvoice.setLineItems(List.of(
              new LineItem(
                  null,
                  "Late Fee Charge",
                  latefee,
                  Currency.getInstance("USD")
              ))
          );
          latefeeInvoice.setAuditInfo(new AuditInfo(  // null is a pervasive and familiar element in
              null, pastDueInvoices, null)            // ORM backed identity objects.
          );                                          // Why are these null? Usually the only way to answer
                                                      // is to go study the database itself.

          // We could go line by line with a red pen, but instead, let's ask ourselves a
          // question about what this code is actually doing. It has, what, 8-9 distinct
          // branches? But what is it actually doing? How many of those are just different
          // flavors of the same thing? Inside of this mess of if/else statements is actually
          // a really, really simple domain idea -- it's just hidden by the noise!
          if (latefee.compareTo(rules.getMinimumFeeThreshold()) <= 0) {
            latefeeInvoice.getAuditInfo().setReason("too low to charge!");
            invoiceRepo.save(latefeeInvoice);
          } else {
            if (latefee.compareTo(rules.getMaximumFeeThreshold()) > 0) {
              if (customer.getApprovalId().isEmpty()) {
                this.requestReview(latefeeInvoice, customer);
                latefeeInvoice.getAuditInfo().setReason("Above default threshold");
              } else {
                ApprovalStatus status = getApprovalStatus(customer);
                if (status.equals(ApprovalStatus.PENDING)) {
                  latefeeInvoice.getAuditInfo().setReason("Pending decision");
                } else if (status.equals(ApprovalStatus.DENIED)) {
                  latefeeInvoice.getAuditInfo().setReason("Exempt from large fees");
                }
              }
              invoiceRepo.save(latefeeInvoice);
              continue;
            }

            invoiceRepo.save(latefeeInvoice);
            this.submitBill(latefeeInvoice);
          }
        }
      }

      /**
       * ───────────────────────────────────────────────────────
       * Listing 5.6
       * ───────────────────────────────────────────────────────
       * Code filled with lies
       * ───────────────────────────────────────────────────────
       */
      // This method has extraordinary power!
      // It's the scariest in our entire codebase. There's no "undo" button with
      // this one. Real customers live on the other side of it. A bug here
      // has the ability to cause immense stress due to unexpected bills and time
      // fighting with customer service reps to prove that we're in the wrong.
      //
      //                 ┌─── And yet it begins with a lie.
      //                 ▼
      void submitBill(Invoice invoice) {
        //             ▲
        //             └─── It doesn't take ANY invoice.
        //
        //     ┌─ We have to mount detailed defenses against that lie.
        //     ▼
        if (invoice.getInvoiceType().equals(InvoiceType.LATEFEE)
            // But we can't even express those defenses well due to our modeling.
            // Why are we checking invoiceID is null? What does that *mean*?
            && invoice.getInvoiceId() == null
            // Ditto here. Can you connect these back to any specific
            // requirement? These are mired in the details of how they
            // map into the *database*. You just have to "know" what these
            // cryptic assertions mean.
            && invoice.getAuditInfo() != null
            && Strings.isNullOrEmpty(
                invoice.getAuditInfo().getReason())
        ) {
          // charge the customer
        } else {
          throw new IllegalArgumentException(
              "You're about to charge something you shouldn't!"
          );
        }
      }

      List<Invoice> getPastDueInvoices(Customer customer) {
        CustomerRating rating = this.ratingsApi.getRating(customer.getId());
        return invoiceRepo.findInvoices(customer.getId())
            .stream()
            .filter(invoice -> {
              if (rating.equals(CustomerRating.GOOD)) {
                return invoice.getDueDate()
                    .plusDays(60).isBefore(LocalDate.now());
              }
              else if (rating.equals(CustomerRating.ACCEPTABLE)) {
                return invoice.getDueDate()
                    .plusDays(30).isBefore(LocalDate.now());
              } else {
                return invoice.getDueDate()
                    .with(lastDayOfMonth()).isBefore(LocalDate.now());
              }
            })
            .toList();
      }

      // The below are all left undefined in the book.
      BigDecimal getTotalPastDue(List<Invoice> invoices) {
        return null; // (just to make things compile)
      }
      LocalDate figureOutDueDate() {
        return null; // (just to make things compile)
      }
      void requestReview(Invoice invoice, Customer customer) {}
      ApprovalStatus getApprovalStatus(Customer customer){
        return null; // (just to make things compile)
      }
    }
  }

}
