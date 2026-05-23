package dop.chapter10;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Listing10_30 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.30
     * ───────────────────────────────────────────────────────
     * Refactored into deterministic versus non-deterministic
     * ───────────────────────────────────────────────────────
     */
    public List<Account> loadAccounts(ResourceName resource) {       //  ┐
        return FileRepository.parse(this.read(resource));            //  │◄── The main method now just
    }                                                                //  ┘    coordinates the other two

    static List<Account> parse(byte[] bytes) {
//                          ▲
//                          └──── The deterministic logic is pulled into its own function.
        String rawCSV = new String(bytes, StandardCharsets.UTF_8);
        return SomeCSVLibrary.parse(rawCSV)
            .stream()
            .map((String[] row) -> new Account(
                 row[ACCOUNT_ID],
                 row[REGION]
                 // and so on...
               ))
            .toList();
    }

    byte[] read(ResourceName resource) {
//         ▲
//         └──── And the messy non-deterministic logic is similarly isolated
        CloudObject object = store.getObject(resource.value());
        try (InputStream in = object.read()) {
            return in.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }














    CloudStore store;
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
    static class FileRepository {
        static List<Account> parse(byte[] bytes) {
            return List.of();
        }
    }
    static class SomeCSVLibrary {
        static List<String[]> parse(String raw) {
            return List.of();
        }
    }
}
