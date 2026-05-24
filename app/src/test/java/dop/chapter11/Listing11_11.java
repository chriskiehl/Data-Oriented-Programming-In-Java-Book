package dop.chapter11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Listing11_11 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.11
     * ───────────────────────────────────────────────────────
     * Deeply verifying that we can parse valid accounts
     * ───────────────────────────────────────────────────────
     */
    @Test
    void weCanParseValidAccounts() {
        // First we generate a huge set of accounts
        List<Account> accounts = createAccounts();
        // Then we use those to generate our input data we’ll use for the test
        String rawCSV = toCSV(accounts);
        byte[] bytes = rawCSV.getBytes(StandardCharsets.UTF_8);
        List<Account> result = FileRepository.parse(bytes);
//                             ┌─────────────────┐
        Assertions.assertEquals(accounts, result);
//                             └─────────────────┘
//                                    ▲      Our assertion is able to perform deep equality checks
//                                    └────  because everything is built around data
    }














    record Account(String id){}
    static List<Account> createAccounts() {
        return List.of();
    }
    static String toCSV(List<Account> accounts) {
        return "";
    }
    static class FileRepository {
        static List<Account> parse(byte[] bytes) {
            return List.of();
        }
    }
}
