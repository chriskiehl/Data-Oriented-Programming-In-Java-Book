package dop.chapter05;

import dop.chapter05.the.existing.world.Entities;

import java.math.BigDecimal;
import java.util.List;

/**
 * ───────────────────────────────────────────────────────
 * Listing 5.3
 * ───────────────────────────────────────────────────────
 * In the imagined system, every Entity has an associated
 * "repository" or "Data Access Object."
 *
 * NOTE: The listing in the book shows classes with all of
 * their implementations abbreviated with "...". That doesn't
 * work in Java obviously, thus we use interfaces here.
 */
public class Listing5_3 {

  public interface CustomerRepo {
    List<Entities.Customer> findAll();
    void save(Entities.Customer customer);
  }

  public interface InvoiceRepo {
    void save(Entities.Invoice invoice);
    List<Entities.Invoice> findInvoices(String customerId);
  }

  public interface RulesRepo {
    Entities.Rules loadDefaults();
  }

  public interface FeesRepo {
    BigDecimal get(String countryCode);
  }

}
