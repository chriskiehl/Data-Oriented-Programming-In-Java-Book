package dop.chapter11;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

public class Listing11_07 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.7
     * ───────────────────────────────────────────────────────
     * Downloading CSV files from a Cloud Store
     * ───────────────────────────────────────────────────────
     */
    // A refresher on the data types
    record AccountId(String value) {
        AccountId {
            if (!value.matches("\\d{9}")) {
                throw new IllegalArgumentException("...");
            }
        }
    }
    enum Region {AMER, LA, EMEA /*etc...*/;}
    enum Segment {ENTERPRISE, STRATEGIC, EXISTING, /*etc...*/ }
    record Sector(String value){}
    record Account(
            AccountId id,
            Region region,
            double spend
            // and so on...
    ){}
    

    class FileRepository {
        private CloudStore store;
        // constructor

        // Mappings from CSV column to field
        private static int ROW_NUM = 0;
        private static int ACCOUNT_ID = 1;
        // etc...

        // The functional core, imperative shell model.
        public List<Account> loadAccounts(ResourceName resource) {
            return FileRepository.parse(this.read(resource));
        }


//   ┌───────────────────── DETERMINISTIC CORE ───────────────────────────┐
        static List<Account> parse(byte[] bytes) {
            String rawCSV = new String(bytes, StandardCharsets.UTF_8);
            return SomeCSVLibrary.parse(rawCSV)
                .stream()
                .map((String[] row) -> new Account(
                     new AccountId(row[ACCOUNT_ID]),
                     Region.valueOf(row[REGION]),
                     Double.parseDouble(row[SPEND])
                     // and so on...
                   ))
                .toList();
        }
//   └──────────────────────────────────────────────────────────────────────┘


        byte[] read(ResourceName resource) {
            // This is part of the “shell” that does all the
            // gross stuff we don’t want to deal with
            CloudObject object = store.getObject(resource.value());
            try (InputStream in = object.read()) {
                return in.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }











    static int REGION = 2;
    static int SPEND = 3;
    record ResourceName(String value) {}
    interface CloudStore {
        CloudObject getObject(String key);
    }
    interface CloudObject {
        InputStream read() throws IOException;
    }
    static class StandardCharsets {
        static java.nio.charset.Charset UTF_8 = java.nio.charset.StandardCharsets.UTF_8;
    }
    static class SomeCSVLibrary {
        static List<String[]> parse(String raw) {
            return List.of();
        }
    }
}
