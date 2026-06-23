package dop.chapter02;

import java.time.LocalDate;


public class Listing2_08 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.8
   * ───────────────────────────────────────────────────────
   * Complex information can be modeled as an immutable value
   * ───────────────────────────────────────────────────────
   */
  class Invoice {   // ◄──────┐ Huge classes with dozens of complex attributes can be a value.
    InvoiceNum invoiceNum; // │ There’s no limit.
    Customer customer;     // │
    Address billTo;        // │
    LocalDate invoiceDate; // │
    LocalDate dueDate;     // │
    Period billingPeriod;  // │
    Category category;     // │
    //        |
    //        |
    //        |  (and so on for dozens of attributes)
    //        |
    //        ▼
    //       ...
    // constructor, getters, equals, hashcode
  }

  class InvoiceNum {}
  class Customer {}
  class Address {}
  class Period {}
  class Category {}

}
