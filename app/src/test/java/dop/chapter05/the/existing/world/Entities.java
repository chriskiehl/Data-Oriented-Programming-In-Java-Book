package dop.chapter05.the.existing.world;

import dop.chapter05.the.existing.world.Annotations.Entity;
import dop.chapter05.the.existing.world.Annotations.Id;
import dop.chapter05.the.existing.world.Annotations.ManyToMany;
import dop.chapter05.the.existing.world.Annotations.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

/**
 * In chapter 5, we explore what data oriented programming looks
 * like inside a messy, real-world app with lots of prior decisions.
 *
 * These entities are some of those "prior decisions" we explore
 * in the book. They're meant to capture a familiar style of modeling
 * that we might find in frameworks like Spring or Hibernate.
 *
 * The chapter explores their implication, and how we can be data-oriented
 * even if the starting point is less than ideal.
 *
 * :::Implementation Note:::
 * Lombok is used throughout to avoid cluttering up everything
 * with manual getters / setters.
 */
public class Entities {


    @Entity
    @lombok.Getter
    @lombok.Setter
    public static class Invoice {
        @Id
        String invoiceId;
        String customerId;
        @Annotations.OneToMany
        List<LineItem> lineItems;
        InvoiceStatus status;
        LocalDate invoiceDate;
        LocalDate dueDate;
        InvoiceType invoiceType;    // ◄───┐ When this is set to LATEFEE, it's expected
        @Nullable                   //     │ that the audit info is populated.
        AuditInfo auditInfo;        // ◄───┘ Otherwise, it should be null.
    }


    public enum InvoiceType {LATEFEE, STANDARD};


    // Invoices have a very simple lifecycle on the surface.
    // They start as OPEN, then become CLOSED once paid.
    public enum InvoiceStatus {OPEN, CLOSED}

    @Entity
    @AllArgsConstructor
    public static class LineItem {
        @Id
        @Nullable
        String id;
        String description;
        BigDecimal charges;  // ◄──┐ Keeping with the theme of "modeling based on whatever
        Currency currency;   //    │ the ORM makes easiest, the Entities use built in
    }                        //    │ data types rather than more data-oriented ones (like USD or Money)



    @Entity
    @Data
    @AllArgsConstructor
    public static class AuditInfo {
        @Id
        @OneToOne
        String invoiceId;
        @ManyToMany
        List<Invoice> includedInFee;
        @Nullable
        String reason;  // ◄───┐ This will only be defined when a Fee cannot
                        //     │ be billed for some reason.
    }

    @Entity
    @lombok.Getter
    @lombok.Setter
    public static class Customer {
        @Id
        String id;
        Address address;
        @Nullable
        String approvalId;   // ◄────┐ Approvals are managed in another system, but
    }                        //        we track that state on the customer Entity.


                             //        Doing so is "free" with our magical ORM.


    @lombok.Getter
    public static class Address {
        String line1;
        String city;
        String country;
        // and so on
    }

    // Below here are the "minor" entities in our scenario.
    // They're things that are Entities only in that they're
    // saved and loaded from the (pretend) database and thus
    // designed around the ORM's convenience.
    @Data
    public static class Rules {
        BigDecimal minimumFeeThreshold;
        BigDecimal maximumFeeThreshold;

    }




}
