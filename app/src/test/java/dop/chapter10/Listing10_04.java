package dop.chapter10;

import java.util.List;

public class Listing10_04 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 10.4
   * ───────────────────────────────────────────────────────
   * Writing to the database
   * ───────────────────────────────────────────────────────
   */
  void example(Database database) {
    List<Insert> inserts = List.of(
      new Insert("ACCOUNTS", TableRecord.builder().set(/*???*/).build()),
      new Insert("ACCOUNTS", TableRecord.builder().set(/*???*/).build())
      /*???*/
    );
    List<Update> updates = List.of(
      new Update("ACCOUNTS", TableRecord.builder().set(/*???*/).build())
      /*???*/
    );
    database.trasact(inserts, updates);
  }














  record Insert(String table, TableRecord record){}
  record Update(String table, TableRecord record){}

  interface Database {
    void trasact(List<Insert> inserts, List<Update> updates);
  }

  static class TableRecord {
      static TableRecord builder() {
        return new TableRecord();
      }
      TableRecord set() {
        return this;
      }
      TableRecord build() {
        return this;
      }
  }

}
