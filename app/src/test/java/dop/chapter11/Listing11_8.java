package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing11_8 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.8
     * ───────────────────────────────────────────────────────
     * A very basic deserialization test
     * ───────────────────────────────────────────────────────
     */
    @Test
    void weCanParseValidCSV() {
        // row num, accountId, Region, Spend, Segment, and so on
        String csv = """
            1,111111111,AMER,142100.10,ENTERPRISE,Finance,2025...
            2,222222222,EMEA,43066.14,ENTERPRISE,Healthcare,20...
            3,333333333,LA,32099.58,Strategic,Education.1970-1...""";
//     └─────────────────────────────────────────────────────────────┘
//          ▲
//          └──── We can model the schema from this other system as plain
//                data (truncated only to fit on the page)

        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);
        List<Account> result = FileRepository.parse(bytes);
        // Verifying that we produce an Account for every row in the CSV file
        assertEquals(csv.split("\n").length, result.size());
    }














    record Account(String id){}
    static class FileRepository {
        static List<Account> parse(byte[] bytes) {
            return List.of();
        }
    }
}
