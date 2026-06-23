package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing12_04 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.4
     * ───────────────────────────────────────────────────────
     * Driving the test with data
     * ───────────────────────────────────────────────────────
     */
    @Test
    void extractsReportDataAndWritesToDB() throws IOException {
        // [REMOVED] Path path = Path.of("path/to/report.csv");
        //    ▲
        //    No more mysterious files of unknown origin or meaning

        // We can build the file's contents directly as data!
        //                  ┌──────────────┐
        Set<Row> rows = Stream.generate(this::randRow)
        //                  └──────────────┘
        //                          ▲
        //                          └ We covered this in the previous chapter
            .limit(100)
            .collect(Collectors.toSet());

        // And use that data to show exactly what we care about
        Set<Row> usaRelated = rows.stream()
            .filter(x -> x.country().equals(CountryCode.US))
            .collect(Collectors.toSet());

        // No mysterious provenance questions.
        // The file is constructed right in front of everyone's eyes.
        String fileContents = someCsvLibrary.serialize(rows);

        // We put the system into a known initial state
        reportsRepo.upload("report.csv", fileContents);
        reportsQueue.put("report.csv");
        // And then run our service
        myService.run();
        Set<Account> results = myService.listAccounts();

        Set<AccountId> ingested = results.stream().map(Account::id).collect(Collectors.toSet());
        Set<AccountId> expected = usaRelated.stream().map(Row::id).collect(Collectors.toSet());
        assertEquals(expected, ingested);
        //             ^ The assertions we make are now directly tied to the data we create
        //               as part of the test.
    }


    record Row(
        AccountId id,
        Region region,
        CountryCode country
        // etc...
    ){}

    Random rand = new Random();
    Row randRow() {
        return new Row(
            new AccountId(UUID.randomUUID()),
            Region.values()[rand.nextInt(Region.values().length)],
            CountryCode.values()[rand.nextInt(CountryCode.values().length)]
            // and so on...
        );
    }


    SomeCsvLibrary someCsvLibrary;
    ReportsRepo reportsRepo;
    ReportsQueue reportsQueue;
    MyService myService;
    record Account(AccountId id, CountryCode country){}
    record AccountId(UUID value){}
    enum Region {AMER}
    enum CountryCode {US}
    interface ReportsRepo {
        void upload(String name, String contents);
    }
    interface ReportsQueue {
        void put(String name);
    }
    interface MyService {
        void run();
        Set<Account> listAccounts();
    }
    interface SomeCsvLibrary {
        String serialize(Object o);
    }
}

