package dop.chapter11;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.stream.Stream.generate;

public class Listing11_52 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.52
     * ───────────────────────────────────────────────────────
     * Is this fine? Does it crash? Is it slow? Fast?
     * ───────────────────────────────────────────────────────
     */
    void example() {
        String giantCSV = toCSV(generate(this::mkAcnt)  //  
                .limit(1_000_000)                       //  ◄── Dialing up this number lets
                .toList());                             //       explore the extremes of our systems
        byte[] bytes = rawCSV.getBytes(StandardCharsets.UTF_8);
        List<Account> result = FileRepository.parse(bytes);
//                    ▲
//                    └──── We can flush out unexpected performance bugs
//                          before they reach our users
    }














    String rawCSV;
    record Account(String id){}
    Account mkAcnt() { return new Account(""); }
    String toCSV(List<Account> accounts) { return ""; }
    static class FileRepository {
        static List<Account> parse(byte[] bytes) { return List.of(); }
    }
}
