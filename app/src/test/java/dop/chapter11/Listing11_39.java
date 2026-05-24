package dop.chapter11;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.lang.String.format;

public class Listing11_39 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.39
     * ───────────────────────────────────────────────────────
     * Generating 50 random Accounts
     * ───────────────────────────────────────────────────────
     */
    Account mkAcnt() { // (The randomized account maker from Listing 11.34)
        List<String> sectors = List.of("Finance", "...");
        return new Account(
            new AccountId(format("%09d", rand.nextLong(999999999))),
            Region.values()[rand.nextInt(Region.values().length)]
            // etc...
        );
    }
    @Test
    void demo() {
        Stream.generate(this::mkAcnt)
//         ┌──────────┐
            .limit(50)
//         └──────────┘
//               ▲
//               └──── The limit is very important! Without it, generate would keep
//                     producing data until the end of time.
            .forEach(System.out::println);
    }
    /*
    ┌─────────────────────────OUTPUT─────────────────────────────────┐
    Account[id=AccountId[value=145468705], region=LA,
            spend=USD[value=0.813082164872091], segment=ENTERPRISE,
            sector=Sector[value=healthcare],
            updatedOn=1994-08-01T08:01:31Z]
    Account[id=AccountId[value=077489624], region=LA,
            spend=USD[value=0.04436969360183218], segment=EXISTING,
            sector=Sector[value=...],
            updatedOn=5066-07-12T05:27:59Z]
    and so on for 48 more Accounts
    └─────────────────────────────────────────────────────────────────┘
     */













    Random rand = new Random();
    enum Region {AMER, LA, EMEA}
    record AccountId(String value){}
    record Account(AccountId id, Region region, Object... rest){}
}
