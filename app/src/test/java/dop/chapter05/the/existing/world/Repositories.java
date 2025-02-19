package dop.chapter05.the.existing.world;


import dop.chapter05.the.existing.world.Entities.Customer;
import dop.chapter05.the.existing.world.Entities.Invoice;
import dop.chapter05.the.existing.world.Entities.Rules;

import java.math.BigDecimal;
import java.util.List;

// A note on difference between the book and this repo:
// These objects are never explicitly defined in the book.
// We assume they're standard Data Access Objects / Repositories.
// i.e. they have ORM style CRUD methods like findAll() get(), save() etc..
public class Repositories {

    public interface CustomerRepo {
        List<Customer> findAll();
        void save(Customer customer);

    }

    public interface InvoiceRepo {
        void save(Invoice invoice);
        List<Invoice> findInvoices(String customerId);
    }

    public interface RulesRepo {
        Rules loadDefaults();
    }

    public interface FeesRepo {
        BigDecimal get(String countryCode);
    }
}
