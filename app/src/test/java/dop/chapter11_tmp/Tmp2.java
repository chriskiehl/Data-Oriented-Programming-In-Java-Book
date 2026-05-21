package dop.chapter11_tmp;

import dop.chapter11.Example2;
import dop.chapter11.MoreExamples;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.String.format;
import static java.util.stream.Stream.generate;

public class Tmp2 {
    record USD(BigDecimal value) implements Comparable<USD> {
        public static USD valueOf(double value) {
            return new USD(BigDecimal.valueOf(value));
        }
        public USD plus(double other) {
            return new USD(this.value().add(BigDecimal.valueOf(other)));
        }
        public USD minus(double other) {
            return new USD(this.value().subtract(BigDecimal.valueOf(other)));
        }
        public USD plus(USD other) {
            return new USD(this.value.add(other.value));
        }

        @Override
        public int compareTo(USD o) {
            return this.value.compareTo(o.value);
        }
    }
    public record AccountId(String value){}
    public enum Region {AMER, LA, EMEA;}
    public enum Segment {ENTERPRISE, STRATEGIC, EXISTING, /*...*/ }
    public record Sector(String value){}
    public record Account(AccountId id,
                          Region region,
                          USD spend,
                          Segment segment,
                          Sector sector,
                          Instant updatedOn
            /*...*/){}
    Random rand = new Random();

    Account mkAcnt() {
        List<String> sectors = List.of(
                "Finance",
                "healthcare",
                "..."
        );
        return new Account(
                new AccountId(format("%09d", rand.nextLong(999999999))),
                Region.values()[rand.nextInt(Region.values().length)],
                new USD(BigDecimal.valueOf(rand.nextDouble())),
                Segment.values()[rand.nextInt(Segment.values().length)],
                new Sector(sectors.get(rand.nextInt(sectors.size()))),
                Instant.ofEpochSecond(rand.nextLong(99999999999L))
        );
    }


    record PutResult(){}
    record Item(Object o){}
    class QueueService {
        public PutResult put(Item item) {
        }
    }


    @Test
    void example() throws InterruptedException {
        QueueService service = new QueueService();
        try(ExecutorService es = Executors.newFixedThreadPool(10)) {
            List<Callable<PutResult>> putItemRequests = generate(this::mkAcnt)
                    .map(Item::new)
                    .<Callable<PutResult>>map((item) -> () -> service.put(item))
                    .limit(10)
                    .toList();
            List<Future<PutResult>> futures = es.invokeAll(putItemRequests);
        }
    }
}
