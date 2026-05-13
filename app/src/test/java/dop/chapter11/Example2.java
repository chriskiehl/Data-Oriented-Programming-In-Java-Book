package dop.chapter11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.IntStream;

import static dop.chapter11.Example2.InvoiceType.STANDARD;
import static dop.chapter11.Example2.Status.OPEN;
import static java.lang.Character.MAX_CODE_POINT;
import static java.lang.Character.toChars;
import static java.lang.Math.pow;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Stream.generate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class Example2 {

    public record CustomerId(String value){}
    public enum CustomerRating{GOOD, POOR;}
    public interface RatingsAPI {
        CustomerRating getRating(CustomerId id);
    }

    public static enum InvoiceType {STANDARD;}
    public static enum Status {OPEN,CLOSED;}

    public static record Invoice(CustomerId customerId, InvoiceType type, Status status, LocalDate dueDate){}

    public static class InvoiceService {
        Clock clock;
        RatingsAPI ratingsAPI;

        public InvoiceService(Clock clock, RatingsAPI ratingsAPI) {
            this.clock = clock;
            this.ratingsAPI = ratingsAPI;
        }


        public boolean isPastDue(Invoice invoice) {
            LocalDate currentDate = LocalDate.now(this.clock);
            CustomerRating rating = this.ratingsAPI.getRating(invoice.customerId());
            return invoice.type().equals(STANDARD)
                    && invoice.status().equals(OPEN)
                    && currentDate.isAfter(invoice.dueDate().with(gracePeriod(rating)));
        }

        public static TemporalAdjuster gracePeriod(CustomerRating rating) {
            return switch (rating) {
                case CustomerRating.GOOD -> (d) -> d.plus(60, DAYS);
                case CustomerRating.POOR -> TemporalAdjusters.lastDayOfMonth();
            };
        }

        public static boolean isPastDue_DO_(Invoice invoice, CustomerRating rating, LocalDate currentDate) {
            return invoice.type().equals(STANDARD)
                && invoice.status().equals(OPEN)
                && currentDate.isAfter(invoice.dueDate().with(gracePeriod(rating)));
        }
    }

    public record AccountId(String value){
        public AccountId {
            if (!value.matches("\\d{9}")) {
                throw new IllegalArgumentException("...");
            }
        }
    }
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
    public record ResourceName(String value){}
    public interface CloudStore {
        CloudObject getObject(String value);
    }
    public interface CloudObject {
        InputStream read();
    }
    public static int ACCOUNT_ID = 1;
    public static int REGION = 2;
    public static int SPEND = 3;
    public static int SEGMENT = 4;
    public static int SECTOR = 5;
    public static int UPDATED_ON = 6;

    class SomeCSVLibrary {
        public static List<String[]> parse(String rawCSV) {
            return Arrays.stream(rawCSV.split("\n")).map(row -> row.split(",")).toList();
        }
    }

    class FileRepository {
        private CloudStore store;
        public List<Account> loadAccounts(ResourceName resource) {
            return FileRepository.parse(this.read(resource));
        }

        static List<Account> parse(byte[] bytes) {
            String rawCSV = new String(bytes, StandardCharsets.UTF_8);
            return SomeCSVLibrary.parse(rawCSV)
                .stream()
                .map((String[] row) -> new Account(
                        new AccountId(row[ACCOUNT_ID]),
                        Region.valueOf(row[REGION]),
                        new USD(new BigDecimal(row[SPEND])),
                        Segment.valueOf(row[SEGMENT]),
                        new Sector(row[SECTOR]),
                        Instant.parse(row[UPDATED_ON])
                        // and so on...
                ))
                .toList();
        }

        byte[] read(ResourceName resource) {
            CloudObject object = store.getObject(resource.value());
            try (InputStream in = object.read()) {
                return in.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

//    static Account accountA = new Account(
//        new AccountId("1"),
//        Region.AMER,
//        Segment.ENTERPRISE,
//        Instant.now()
//    );


    AccountId randomAccountId() {
        return new AccountId(format("%09d",
            new Random().nextLong(0, 999999999L)
        ));
    }
    Instant randomInstant() {
        return Instant.now();
    }

    static <A extends Enum<A>> A oneOf(Class<A> xs) {
        A[] values = xs.getEnumConstants();
        return values[0];
    }


    static List<String> COMMON_SECTORS = List.of(
        "Finance",
        "Healthcare",
        "Agriculture",
        "..."
    );

    @Test
    void adfasdfasdfasdfsd() {
        System.out.println(Instant.now().plus(99999, DAYS));
        System.out.println(Instant.now().plus(99999, DAYS).toEpochMilli() / 1000);
        System.out.println(Instant.ofEpochSecond(rand.nextLong(99999999999L)));
    }


    public static class Generator {
        private Random random;


        long nextLong(long max) {
            return random.nextLong(max);
        }
        double nextDouble(double min, double max) {
            return random.nextDouble(min, max);
        }

        Instant instant(Instant max) {
            return Instant.ofEpochSecond(this.nextLong(max.getEpochSecond()));
        }
        <A> A oneOf(A[] xs) {
            return xs[random.nextInt(xs.length)];
        }
        <A> A oneOf(List<A> xs) {
            return xs.get(random.nextInt(xs.size()));
        }
        String nextString() {
                return nextString(15);
        }

        String nextString(int maxLength) {
            int length = Math.max(0, this.random.nextInt(maxLength));
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < length; i++) {
                output.append((char) random.nextInt('A', '~'));
            }
            return output.toString();

        }


        BigDecimal nextDecimal() {
            return BigDecimal.ONE;
        }

        boolean nextBoolean() {
            return this.random.nextBoolean();
        }

        <A> Optional<A> maybe(A value) {
            return random.nextBoolean()
                    ? Optional.of(value)
                    : Optional.empty();
        }
    }

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

//    @Test
//    void noDiscountForLargeAccountsInAMER() {
//        double threshold = 1_500_000.00;
//
//        Account aboveLimit = mkAcnt(AMER, threshold + 1.0);   #A
//        Account atLimit = mkAcnt(AMER, threshold);            #A
//        Account belowLimit = mkAcnt(AMER, threshold - 1.0);   #A
//
//        Set<Account> willBeDropped = Set.of(aboveLimit);              #B
//        Set<Account> willBeProcessed = Set.of(atLimit, belowLimit);   #B
//        Set<Account> items = union(willBeDropped, shouldBeProcessed);
//
//        MyService service = new MyService(threshold);
//        Set<Result> result = service.applyDiscounts(items);
//        assertEquals(result.size(), shouldBeProcessed.size());
//    }


    enum MirrorState {STALE, UP_TO_DATE, EMPTY}
    record Case(MirrorState state, File originReturns, File mirrorReturns){}

    Generator gen = new Generator();
//
//
//    Account validAccount() {
//        return new Account(
//            new AccountId(format("%09d", gen.nextLong(999999999))),
//            gen.oneOf(Region.values()),
//            gen.oneOf(Segment.values()),
//            gen.instant(Instant.now().plus(36_500, DAYS))
//            // and so on...
//        );
//    }
//
//    public record Account_(
//        AccountId id,
//        Region region,
//        double spend,
//        Segment segment,
//        Instant updatedOn,
//
//        // many more attribtues...
//    ){}
//
//    public record Result() {}
//    public static class MyService {
//        private double threshold;
//        public MyService(double t) {
//            this.threshold = t;
//        }
//        public Set<Result> applyDiscounts(Set<Account_> accounts) {
//            return Set.of();
//        }
//    }
//
//    Account mkAcnt(Region region, double spend) {
//        return new Account(
//            new AccountId("111111111"),
//            region,
//            spend,
//            Segment.ENTERPRISE,
//            new Sector("finance")
//            // rest of attribtues
//        )
//    }
//
//
//    static <A> Set<A> union(Set<A> left, Set<A> right) {
//        LocalDate.of(2012, 1, 14)
//        HashSet<A> copy = new HashSet<>(left);
//        copy.addAll(right);
//        return copy;
//    }
//
//
//@Test
//void wayTooManyDetails() {
//    Account_ accountA = new Account_(
//        new AccountId("111111111"),
//        Region.AMER,
//        1_000_00.00,
//        Segment.Strategic,
//        Instant.now(this.clock)
//        // ...
//    );
//    Account_ accountB = new Account_(
//        new AccountId("222222222"),
//        Region.AMER,
//        1_600_00.00,
//        Segment.ENTERPRISE,
//        Instant.now(this.clock)
//        // ...
//    );
//    Account_ accountC = new Account_(
//        new AccountId("333333333"),
//        Region.EMEA,
//        1_300_00.00,
//        Segment.Existing,
//        Instant.now(this.clock)
//        // ...
//    );
//    MyService service = new MyService(1_500_000.00);
//    Set<Account_> shouldBeDropped = Set.of(accountB);
//    Set<Account_> shouldBeProcessed = Set.of(accountA, accountC);
//    Set<Account_> items = union(shouldBeDropped, shouldBeProcessed);
//    // rest of test
//
//}
//
//    void asdfasdfsd() {
//        IntStream.iterate(0, x -> x + 1).boxed().map(i -> new AccountId(i.toString()));
//    }
//
//    Stream<Account> generateAccounts() {
//        return Stream.generate(this::validAccount);
//    }
//
//    public record Index<A>(int i, A a){}
//
//    <A> Stream<Index<A>> genRows(Stream<A> stream) {
//        AtomicInteger counter = new AtomicInteger();
//        return stream.map(x -> new Index<>(counter.incrementAndGet(), x));
//    }
//
//    record Row(int rowNum, Account account){}
//
////    @Test
////    void demo() {
////        System.out.println(toCSV(
////            Stream.generate(this::validAccount)
////            .limit(5)
////            .toList()));
////    }
//
//    record CsvAccount(
//        String id,
//        String region,
//        String segment,
//        String updatedOn
//        //...
//    ) {
//        public CsvAccount withId(String id) {
//            return new CsvAccount(id, region, segment, updatedOn);
//        }
//        static CsvAccount fromAccount(Account account) {
//            return new CsvAccount(
//                account.id().value(),
//                account.region().toString(),
//                account.segment().toString(),
//                account.updatedOn().toString()
//                // ...
//            );
//        }
//    }
//
    String toCSV(int rowNumber, Account account) {
        return format(
            "%s,%s,%s,%s,%s,%s,%s,...",
            rowNumber,
            account.id().value(),
            account.region(),
            account.spend().value(),
            account.segment(),
            account.sector().value(),
            account.updatedOn()
        );
    }

    String toCSV(List<Account> accounts) {
        return join("\n", IntStream.range(0, accounts.size())
            .boxed().map(i -> this.toCSV(i + 1, accounts.get(i)))
            .toList());
    }
//
//    @Test
//    void demoPassFail() {
//        Stream<CsvAccount> accounts = Stream.concat(
//            Stream.generate(this::validAccount).map(CsvAccount::fromAccount).limit(10),
//            Stream.of(CsvAccount.fromAccount(validAccount()).withId("0x2v87fh7"))
//        );
//
//        String rawCSV = toCSV(accounts.toList());
//        byte[] bytes = rawCSV.getBytes(StandardCharsets.UTF_8);
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            FileRepository.parse(bytes);
//        });
//    }
//

////
////    @Test
////    void asdfasdfasdfad() {
////        System.out.println(LocalDate.of(1970, 1, 1).toEpochDay());
////        System.out.println();
////    }
////
    @Test
    void weCanParseValidCSV() {
        System.out.println(LocalDateTime.now());
        // rows of accountId, Region, Segment, Last Updated, and so on
        String csv = """
            111111111,AMER,Enterprise,2025-01-01T22:43:41Z,...
            222222222,EMEA,Enterprise,2020-12-26T02:33:02Z,...
            333333333,LA,Strategic,1970-11-11T12:10:13Z,...""";

        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);
        List<Account> result = FileRepository.parse(bytes);
        assertEquals(csv.split("\n").length, result.size());
        assertEquals(Region.AMER, result.getFirst().region);
        // other assertions
    }

    static List<Account> createAccounts() {
        List<Account> accounts = new ArrayList<>();
        int num = 1;
        List<String> sectors = List.of("Healthcare", "Finance", "...");
        for (Region region : Region.values()) {
            for (Segment segment : Segment.values()) {
                for (String sector : sectors) {
                    accounts.add(new Account(
                        new AccountId(format("%09d", num++)),
                        region,
                        new USD(BigDecimal.valueOf(num)),
                        segment,
                        new Sector(sector),
                        Instant.now()
                    ));
                }
            }
        }
        return accounts;
    }

    @Test
    void weCanParseAllValidAccountStates() {
        List<Account> accounts = createAccounts();
        String rawCSV = toCSV(accounts);
        byte[] bytes = rawCSV.getBytes(StandardCharsets.UTF_8);
        List<Account> result = FileRepository.parse(bytes);
        Assertions.assertEquals(accounts, result);
    }



//
//    private Clock clock;
//    private InvoiceService invoiceService;
//    private RatingsAPI ratingsAPI;
//
//    static int MAX_GRACE_PERIOD = 60;
//
//    @BeforeEach
//    void init() {
//        this.clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
//        this.ratingsAPI = mock(RatingsAPI.class);
//        this.invoiceService = new InvoiceService(this.clock, this.ratingsAPI);
//    }
//
//    @Test
//    void goodCustomersGetGoodDeals1() {
//        when(ratingsAPI.getRating(any())).thenReturn(CustomerRating.GOOD);
//
//        assertFalse(this.invoiceService.isPastDue(
//            new Invoice(
//                new CustomerId("1"),
//                STANDARD,
//                OPEN,
//                LocalDate.now(this.clock).minusDays(MAX_GRACE_PERIOD)
//        )));
//    }
//
//    @Test
//    void adasdfasf() {
//        System.out.println("Hello from test");
//    }
//
//    @Test
//    void betterCustomersGetBetterDeals() {
//        LocalDate dueDate = LocalDate.now();
//        LocalDate badCustomersMustPayBy = dueDate.with(gracePeriod(CustomerRating.POOR));
//        LocalDate goodCustomersMustPayBy = dueDate.with(gracePeriod(CustomerRating.GOOD));
//        assertTrue(badCustomersMustPayBy.isBefore(goodCustomersMustPayBy));
//    }
//
//
//    @Test
//    void goodCustomersGetMaximumGracePeriod() {
//        LocalDate wasDueOn = LocalDate.now();
//        LocalDate currentDate = wasDueOn.plusDays(MAX_GRACE_PERIOD);
//
//        boolean result = InvoiceService.isPastDue_DO_(
//                new Invoice(new CustomerId("1"), STANDARD, OPEN, wasDueOn),
//                CustomerRating.GOOD,
//                currentDate
//        );
//
//        assertFalse(result, """
//            Customers in good standard are not considered past due as
//            long as they're within the grace period.
//            """);
//    }
//
//    // listing: We can test multiple scenarios just by tweaking the data
//    // we feed into the function
//    @Test
//    void closedInvoicesCannotBePastDue() {
//        LocalDate wasDueOn = LocalDate.now();
//        LocalDate currentDate = wasDueOn.plusDays(MAX_GRACE_PERIOD + 1);
//        // Even customers in good standing eventually get flagged as past due
//        assertTrue(InvoiceService.isPastDue_DO_(
//            new Invoice(new CustomerId("1"), STANDARD, OPEN, wasDueOn),
//            CustomerRating.GOOD,
//            currentDate
//        ), "Even good customers eventually need to pay.");
//
//        // But a closed invoice cannot be past due regardless of any
//        // other invoice or customer attributes.
//        for (CustomerRating rating : CustomerRating.values()) {
//            for (InvoiceType type : InvoiceType.values()) {
//                assertFalse(InvoiceService.isPastDue_DO_(
//                    new Invoice(new CustomerId("1"), type, CLOSED, wasDueOn),
//                    rating,
//                    currentDate
//                ), "Invariant: closed invoices cannot be past due");
//            }
//        }
//    }
//
//    @Test
//    void goodCustomersGet60DaysPastDueDateBeforeLate2() {
//        Random random = new Random();
//        LocalDate dueDate = LocalDate.ofEpochDay(random.nextLong(
//            LocalDate.of(1970,  1,  1).toEpochDay(),
//            LocalDate.of(9999, 11, 30).toEpochDay()
//        ));
//        IntStream.range(-365, 365).forEach(delta -> {
//            LocalDate currentDate = dueDate.plusDays(delta);
//            boolean isPastDue = InvoiceService.isPastDue_DO_(
//                new Invoice(new CustomerId("1"), STANDARD, OPEN, dueDate),
//                CustomerRating.GOOD,
//                currentDate
//            );
//            if (DAYS.between(dueDate, currentDate) > MAX_GRACE_PERIOD) {
//                assertTrue(isPastDue, "OPEN invoice after grace period is past due");
//            } else {
//                assertFalse(isPastDue, "Open invoice within grace period is not past due");
//            }
//        });
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("_0to60")
//    void customersInGoodStandingCanHaveOpenInvoices60DaysPastDueDate2(TemporalAdjuster adjuster) {
//        LocalDate dueOn = LocalDate.now();
//        LocalDate currentDate = dueOn.with(adjuster);
//
////        assertFalse(InvoiceService.isPastDue_DO_(
////                new Invoice("1234", STANDARD, OPEN, dueOn),
////                CustomerRating.GOOD,
////                currentDate
////        ), "within grace period");
////    }
////
////    void customersInGoodStandingCanHaveOpenInvoices60DaysPastDueDate3() {
////        LocalDate dueOn = LocalDate.now();
////        assertFalse(InvoiceService.isPastDue_DO_(
////                new Invoice("1234", STANDARD, OPEN, dueOn),
////                CustomerRating.GOOD,
////                dueOn
////        ), "within grace period");
////
////        assertFalse(InvoiceService.isPastDue_DO_(
////                new Invoice("1234", STANDARD, OPEN, dueOn),
////                CustomerRating.GOOD,
////                dueOn.plusDays(60)
////        ), "within grace period");
////
////        assertTrue(InvoiceService.isPastDue_DO_(
////                new Invoice("1234", STANDARD, OPEN, dueOn),
////                CustomerRating.GOOD,
////                dueOn.plusDays(61)
////        ), "Exceeded grace per");
//    }
//
//    static Stream<TemporalAdjuster> _0to60() {
//        return IntStream.range(0, 61).boxed().map(offset ->
//            (date) -> date.plus(offset, DAYS)
//        );
//    }
//
//    record TestCase(
//        LocalDate currentDate,
//        LocalDate dueDate,
//        InvoiceType invoiceType,
//        Status status,
//        boolean expected,
//        String why
//    ){}

    record USD(BigDecimal value){
        static USD valueOf(double value) {
            return new USD(BigDecimal.valueOf(value));
        }
    }
    public enum Policy {
        GRACE_PERIOD,
        FLEXIBLE,
        IMMEDIATE,
        STRICT,
        MANUAL_REVIEW
    }
    public enum AuditFinding {
        NO_ISSUE,
        INACCURATE,
        BILLING_ERROR,
        OUT_OF_COMPLIANCE
    }
    public record RawData(
        Optional<Policy> policy,
        Optional<AuditFinding> findings,
        Optional<Boolean> premiumStatus,
        USD total,
        String tag
    ){
        public RawData add(RawData other) {
            // complicated merging logic from chapter 7 elided
            return other;
        }
    }



    <A> Optional<A> maybe(A value) {
        return rand.nextBoolean()
                ? Optional.of(value)
                : Optional.empty();
    }

    String randStr() {
        return join("",
            generate(() -> toChars(rand.nextInt(MAX_CODE_POINT)))
                .map(String::new)
                .limit(rand.nextInt(10))
                .toList());
    }

    RawData randomRawData() {
        Policy[] policies = Policy.values();
        AuditFinding[] audits = AuditFinding.values();
        return new RawData(
            maybe(policies[rand.nextInt(policies.length)]),
            maybe(audits[rand.nextInt(audits.length)]),
            maybe(rand.nextBoolean()),
            USD.valueOf(rand.nextDouble(0.0, pow(10, 5))),
            randStr()
        );
    }

@Test
void associativity() {
    IntStream.range(0, 10000).forEach(__ -> {
        RawData a = randomRawData();
        RawData b = randomRawData();
        RawData c = randomRawData();
        assertEquals(
                add(a, add(b, c)),
                add(add(a, b), c)
        );
    });
}
    public RawData add(RawData a, RawData b) {
        return gen.nextBoolean() ? a : b;
    }


    //                       -> Completed
    // scheduled -> Running
    //                       -> Failed
    //
    //
}
