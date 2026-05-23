package dop.chapter10;

import org.junit.jupiter.api.Test;

import java.util.List;

public class Listing10_31 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.31
     * ───────────────────────────────────────────────────────
     * Deterministic code is easy to test in isolation
     * ───────────────────────────────────────────────────────
     */
    @Test
    void delightfulTesting() {
        String rawCSV = "(CSV here)";                                    //  ┐
        List<Account> result = FileRepository.parse(rawCSV.getBytes());  //  │◄── No preamble or mocking.
        // assertions here                                               //  ┘    Data in; Data out.
    }














    record Account(String id){}
    static class FileRepository {
        static List<Account> parse(byte[] bytes) {
            return List.of();
        }
    }
}
