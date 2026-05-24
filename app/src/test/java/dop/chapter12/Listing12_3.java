package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static dop.chapter12.Listing12_3.Region.AMER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing12_3 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.3
     * ───────────────────────────────────────────────────────
     * A bad integration test
     * ───────────────────────────────────────────────────────
     */
    @Test
    void extractsReportDataAndWritesToDB() throws IOException {
        Path path = Path.of("path/to/report.csv");
        reportsRepo.upload(path.toFile());
        reportsQueue.put("report.csv");

        myService.run();  // it rattles around
        List<Account> results = myService.listAccounts();

        assertEquals(4, results.size());
        Account account = results.getFirst();
        assertEquals(Segment.ENTERPRISE, account.segment());
        assertEquals(SalesChannel.Internal, account.channel());
        assertEquals(AMER, account.region());
    }















    ReportsRepo reportsRepo;
    ReportsQueue reportsQueue;
    MyService myService;
    record Account(Segment segment, SalesChannel channel, Region region){}
    enum Segment {ENTERPRISE}
    enum SalesChannel {Internal}
    enum Region {AMER}
    interface ReportsRepo {
        void upload(java.io.File file);
    }
    interface ReportsQueue {
        void put(String name);
    }
    interface MyService {
        void run();
        List<Account> listAccounts();
    }
}

