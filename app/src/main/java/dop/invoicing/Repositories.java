package dop.invoicing;

import dop.invoicing.DataTypes.BillingState;
import dop.invoicing.DataTypes.Latefee;
import dop.invoicing.DataTypes.ReviewedLatefee;
import dop.invoicing.Entities.Config;
import dop.invoicing.Entities.Customer;
import dop.invoicing.Entities.Invoice;
import dop.invoicing.Entities.USD;
import dop.invoicing.Springish.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Locale.IsoCountryCode;

/**
 * These are all defined in the same place just for ease of example.
 */
public class Repositories {

    public static class CustomerRepo implements Repository<Customer, String> {

        List<Customer> findAll() {
            return List.of();
        }
    }


    public static class InvoiceRepo implements Repository<Customer, String> {

        public Invoice get(String invoiceId) {
            return null;
        }

        public List<Invoice> findInvoices(String customerId) {
            return List.of();
        }

        public void save(Invoice invoice){}
        public void save(Latefee<? extends BillingState> invoice){

        }

        public void save(ReviewedLatefee latefee) {
            // mapping here.
        }

        public void save(Collection<ReviewedLatefee> latefees) {
            // mapping here.
        }
    }


    public static class FeeRepo implements Repository<Double, IsoCountryCode> {

        public Double get(IsoCountryCode code) {
            return 0.1;
        }
    }

    public static class RulesRepo implements Repository<Config, Integer> {

        public Config loadDefaults() {
            return new Config(new USD(BigDecimal.ONE), new USD(BigDecimal.ONE));
        }
    }

    public static class ApprovalsRepo implements Repository<String, String> {

    }


}
