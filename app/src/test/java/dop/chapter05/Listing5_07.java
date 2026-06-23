package dop.chapter05;

import org.junit.jupiter.api.Test;

public class Listing5_07 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 5.7
   * ───────────────────────────────────────────────────────
   * These listings explore one of my favorite design tools for
   * behaviors: thinking in transforms between data.
   *
   * We represent a transformation with an arrow (->).
   * Data goes in one side, new data comes out the other.
   *
   * This is about as lightweight as design processes get. We
   * invent data on the fly and give it 'movement' through our
   * system via the arrows. From this movement comes continuity.
   * The data types moving through our system begin to tell a
   * story -- if we do it well, this story reads almost like
   * a plain english version of our requirements.
   */
  @Test
  public void example() {
    // We represent transforms with the ascii arrow (->)
    //
    //      ┌─────────────────────────────┐  This transform says that
    //      ▼                             │  a List of Invoices is used to
    // List<Invoice> -> LateFeeInvoice  ◄─┘  produce a new LateFeeInvoice
  }

}
