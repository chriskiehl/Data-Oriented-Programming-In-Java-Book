package dop.chapter02;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class Listing2_22 {

  interface Temperature {}

  /**
   * ───────────────────────────────────────────────────────
   * Listings 2.22
   * ───────────────────────────────────────────────────────
   * Data has a lot of familiar shapes. Stuff we might just sum
   * up as "information" about the world. Sales reports, daily weather,
   * etc. The kind of stuff we'd read out of a database.
   * ───────────────────────────────────────────────────────
   */
  @Test
  public void example() {
    // Note! Here just to enable compilation.

    // These value classes are examples of pretty standard stuff
    // that we'd all probably agree "looks like data"
    class DailyElectricityUsage {
      LocalDate date;
      Temperature high;
      Temperature low;
      Double kWh;
    }

    class Sale {
      String productName;
      LocalDate soldOn;
      int quantity;
      BigDecimal totalPrice;
    }

  }

}
