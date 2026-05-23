package dop.chapter10;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Listing10_10 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.10
     * ───────────────────────────────────────────────────────
     * The transformer abstraction and implementations
     * ───────────────────────────────────────────────────────
     */
    interface Transformer {
        UpdatesAndInserts transform(CloudObject object);
    }



    class InvoiceTransformer implements Transformer {
        @Override
        public UpdatesAndInserts transform(CloudObject object) {
            return __; // elided
        }
    }
    class DisputesTransformer implements Transformer {
        @Override
        public UpdatesAndInserts transform(CloudObject object) {
            return __; // elided
        }
    }

    class AccountsTransformer implements Transformer {
        private static String TABLE_NAME = "ACCOUNTS";
        private AccountRepository accountRepo;

        @Override
        public UpdatesAndInserts transform(CloudObject object) {
            try (InputStream stream = object.read()) {
                            //                 ▲
                            //                 └──── Each transformer will read the file object
                String raw = new String(
                    stream.readAllBytes(),
                    StandardCharsets.UTF_8);
                // then parse out the relevant data
                List<AccountCsvRow> csvData = SomeCSVLibrary.parse(raw, AccountCsvRow.class);
                List<Insert> inserts = new ArrayList<>();                        //  ┐
                List<Update> updates = new ArrayList<>();                        //  │
                for (AccountCsvRow row : csvData) {                              //  │ And then figure out if the
                    if (accountRepo.exists(row.accountId())) {                   //  │ record needs inserted or
                        updates.add(new Update(TABLE_NAME, toTableRecord(row))); //  │ updated.
                    } else {                                                     //  │
                        inserts.add(new Insert(TABLE_NAME, toTableRecord(row))); //  │
                    }                                                            //  ┘
                }
                return new UpdatesAndInserts(inserts, updates);
            } catch (IOException e) {
                throw new RuntimeException("Error while reading from file!", e);
            }
        }














        TableRecord toTableRecord(AccountCsvRow row) {
            return new TableRecord();
        }
    }

    interface CloudObject {
        InputStream read() throws IOException;
    }
    record UpdatesAndInserts(List<Insert> inserts, List<Update> updates){}
    record Insert(String table, TableRecord record){}
    record Update(String table, TableRecord record){}
    record TableRecord(){}
    record AccountCsvRow(String accountId){}

    static UpdatesAndInserts __;
    interface AccountRepository {
        boolean exists(String id);
    }
    static class SomeCSVLibrary {
        static <A> List<A> parse(String raw, Class<A> type) {
            return List.of();
        }
    }
}
