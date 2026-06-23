package dop.chapter02;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class Listing2_02 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.2
   * ───────────────────────────────────────────────────────
   * Each instance creates a unique object identity
   * ───────────────────────────────────────────────────────
   */
  class Customer {
    UUID accountId;
    Instant updatedOn;
    String email;
    boolean isPremium;

    // (constructor, getters, and setters elided in the book copy)
    public Customer(UUID accountId, Instant updatedOn, String email, boolean isPremium) {
      this.accountId = accountId;
      this.updatedOn = updatedOn;
      this.email = email;
      this.isPremium = isPremium;
    }

    public Customer setAccountId(UUID accountId) {
      this.accountId = accountId;
      return this;
    }

    public Customer setUpdatedOn(Instant updatedOn) {
      this.updatedOn = updatedOn;
      return this;
    }

    public Customer setEmail(String email) {
      this.email = email;
      return this;
    }

    public Customer setPremium(boolean premium) {
      isPremium = premium;
      return this;
    }

    public UUID accountId() {
      return accountId;
    }

    public Instant updatedOn() {
      return updatedOn;
    }

    public String email() {
      return email;
    }

    public boolean isPremium() {
      return isPremium;
    }
  }

  @Test
  void example() {
    Customer customer = new Customer(
        UUID.randomUUID(),
        Instant.now(),
        "customer@example.com",
        false
    );
    customer.setEmail("new-email@example.com");
    customer.setPremium(true);
    customer.setUpdatedOn(Instant.now());

    Customer perfectCopy = new Customer(
        customer.accountId(),
        customer.updatedOn(),
        customer.email(),
        customer.isPremium()
    );
    assert customer != perfectCopy;
  }

}
