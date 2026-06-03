package dop.chapter10;

public class Listing10_3 {
  /**
   * ───────────────────────────────────────────────────────
   * Listing 10.3
   * ───────────────────────────────────────────────────────
   * Data types used for making changes
   * ───────────────────────────────────────────────────────
   */
  record Insert(String table, TableRecord record) {}
  record Update(String table, TableRecord record) {}














  record TableRecord() {}
}
