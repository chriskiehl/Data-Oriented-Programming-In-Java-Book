package dop.chapter10;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Listing10_28 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.28
     * ───────────────────────────────────────────────────────
     * A potential implementation of the FileRepository
     * ───────────────────────────────────────────────────────
     */
    public class FileRepository {
        private CloudStore store;

        public List<Account> loadAccounts(ResourceName resource) {
            CloudObject object = store.getObject(resource.value());
//                                          ▲
//                                          └──── First we fetch the object identified by the resource
            try (InputStream in = object.read()) {
                // Then we consume its input stream
                String raw = new String(in.readAllBytes(), UTF_8);
                // Parse it into CSV
                return SomeCSVLibrary.parse(raw)
                    .stream()
                    // Then finally convert it into our domain type
                    .map((String[] row) -> new Account(
                       row[ACCOUNT_ID],
                       row[REGION]
                       // and so on...
                     ))
                    .toList();
            } catch (IOException e) {
                throw new RuntimeException("uh oh!", e);
            }
        }
    }














    static int ACCOUNT_ID = 0;
    static int REGION = 1;
    record ResourceName(String value) {}
    record Account(String id, String region){}
    interface CloudStore {
        CloudObject getObject(String key);
    }
    interface CloudObject {
        InputStream read() throws IOException;
    }
    static class SomeCSVLibrary {
        static List<String[]> parse(String raw) {
            return List.of();
        }
    }
}
