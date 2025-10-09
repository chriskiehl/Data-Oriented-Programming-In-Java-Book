package dop.chapter11;

import dop.chapter05.the.existing.world.Entities;
import dop.chapter05.the.existing.world.Entities.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.DoubleRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.time.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dop.chapter11.Sketch.Expected.of;
import static dop.chapter11.Sketch.Region.AMER;
import static dop.chapter11.Sketch.Region.LATAM;
import static dop.chapter11.Sketch.Status.Finished;
import static dop.chapter11.Sketch.Status.Pending;
import static java.lang.String.format;
import static java.lang.String.join;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Sketch {
    private MyService3 service;
    private MyService5 service5;
    private ToyDataGenerator datagen;

    @BeforeEach
    void setup() {
        this.service = new MyService3(1_500_000.00);
        this.datagen = new ToyDataGenerator();
        this.service5 = new MyService5();
    }

    class SomeDependency {
        String doSomething() {
            return "";
        }
    }

    class MyService {
        private SomeDependency dependency;

        public MyService(SomeDependency dep) {
            this.dependency = dep;
        }

        String someMethod() {
            return this.dependency.doSomething();
        }
    }

    @Test
    void test() {
        String expected = "Some Result";
        SomeDependency mock = Mockito.mock(SomeDependency.class);
        when(mock.doSomething()).thenReturn(expected);

        MyService service = new MyService(mock);
        assertEquals(
                service.someMethod(),
                expected
        );
    }

    static class InvalidAuthenticationType extends Exception {
        public InvalidAuthenticationType() {

        }

    }

    record SSHBanner(Set<String> authModes, String version) {}

    SSHBanner grab(String host, int port) throws IOException {
        try(Socket socket = new Socket()) {
            SocketAddress endpoint = new InetSocketAddress(host, port);
            socket.connect(endpoint);

            BannerGrabber grabber = new BannerGrabber(socket);
            try {
                grabber.authNone("");
            } catch (InvalidAuthenticationType e) {
                return new SSHBanner(Set.of("none"), grabber.remoteVersion());
            }
            return new SSHBanner(Set.of("none"), grabber.remoteVersion());
        }
    }

    class BannerGrabber {
        private Socket socket;

        public BannerGrabber(Socket socket) {
            this.socket = socket;
        }

        void authNone(String username) throws InvalidAuthenticationType {

        }

        String remoteVersion() {
            return "";
        }
    }

//    @Test
//    void test2() throws IOException {
//        Socket socket = Mockito.mock(Socket.class);
//        when(socket.getInputStream()).thenReturn()
//
//        BannerGrabber grabber = new BannerGrabber(socket);
//
//    }


    static class InternalFailure extends RuntimeException {}

    static class ServiceClient {
        public String doSomething() {
            try {
                Thread.sleep(1000);
                return "I did some work!";
            } catch (InterruptedException e) {
                throw new InternalFailure();
            }
        }
    }

    static class MyDependencyInjector {
        public static ServiceClient getClient(){
            return new ServiceClient();
        };
    }

    @Test
    void assertClientDoesNotInterruptDuringShutdown() throws ExecutionException, InterruptedException {
        ServiceClient client = MyDependencyInjector.getClient();
        ExecutorService es = Executors.newFixedThreadPool(1);
        CompletableFuture<String> example1 = CompletableFuture.supplyAsync(client::doSomething, es);
        es.shutdownNow();
        Throwable ex = Assertions.assertThrows(ExecutionException.class, example1::get);
        assertEquals(ex.getCause().getClass(), InternalFailure.class);

        es = Executors.newFixedThreadPool(1);
        CompletableFuture<String> example2 = CompletableFuture.supplyAsync(client::doSomething, es);
        es.shutdown();
        Assertions.assertDoesNotThrow(() -> example2.get());
    }

    record Network(String value) {
        static Network of(String value){return new Network(value);};
    }
    record Discount(Account account) {}

    static List<Discount> someComplicatedFunction(List<String> labels, List<Network> networks) {
        return List.of();
    }


    List<String> methodThatDoesSomething(List<String> xs) {
        return List.of();
    }

    static class MyService2 {

        public MyService2(double threshold) {

        }

        List<String> methodThatDoesSomething(List<String> items) {
            return List.of();
        }
    }

//    static {
//        MyService2 inst = new MyService2();
//    }

    @Test
    void mostBasicHardcoding() {
        List<String> items = List.of("foo", "bar", "baz");
        List<String> result = methodThatDoesSomething(items);
        assertEquals(2, result.size());
    }

    static <A> List<A> concat(List<A> a, List<A> b) {
        return Stream.concat(a.stream(), b.stream()).toList();
    }

    // better
    @Test
    void mostBasicHardcodingFixed() {
        List<String> shouldBeDropped = List.of("baz");
        List<String> shouldBeKept = List.of("foo", "bar");
        List<String> allItems = concat(shouldBeKept, shouldBeDropped);

        List<String> result = methodThatDoesSomething(allItems);
        assertEquals(shouldBeKept.size(), result.size());
    }
    // this is great because it shows us what we expect to happen.

    // but... why SHOULD those be dropped? It doesn't tell us what
    // the test data means.
    @Test
    void mostBasicHardcodingFixed1() {
        // let's say that it only processes X=whatever transactions.
        List<String> shouldBeDropped = List.of("baz"); // baz is not x=whatever
        List<String> shouldBeKept = List.of("foo", "bar");
        List<String> allItems = concat(shouldBeKept, shouldBeDropped);
        //                         When this fails, we have to dig around inside
        //                         the implementation to understand what's actually
        //                         going on.
        List<String> result = methodThatDoesSomething(allItems);
        assertEquals(result.size(), shouldBeKept.size());
    }

    enum SalesChannel {Internal,External}
    enum Segment {Enterprise, Strategic, Existing }
    enum CountryCode {AC, AD, AE, /*...*/ US}
    enum Region {AMER,EMEA,LATAM,NA}
    record Sector(String value){}
    static enum Labels {FOO, BAR, BAZ}
    record AccountId(String value){}


    record Account(
            AccountId id,
            SalesChannel channel,
            Region region,
            Segment segment,
            CountryCode code,
            Sector sector,
            double spend
    ){
        Account withSegment(Segment segment) {
            return new Account(id,channel,region,segment,code,sector,spend);
        }
        Account withRegion(Region region) {
            return new Account(id,channel,region,segment,code,sector,spend);
        }
        Account withSpend(double spend) {
            return new Account(id,channel,region,segment,code,sector,spend);
        }

    }


    static <A,B,C> Set<C> product(A[] xs, Collection<B> ys, BiFunction<A,B,C> f) {
        return Arrays.stream(xs).flatMap(x -> ys.stream().map(y -> f.apply(x, y))).collect(Collectors.toSet());
    }

    static <A,B,C> Set<C> product(A[] xs, B[] ys, BiFunction<A,B,C> f) {
        return Arrays.stream(xs).flatMap(x -> Arrays.stream(ys).map(y -> f.apply(x, y))).collect(Collectors.toSet());
    }

    static <A,B,C> Set<C> product(Collection<A> xs, Collection<B> ys, BiFunction<A,B,C> f) {
        return xs.stream().flatMap(x -> ys.stream().map(y -> f.apply(x, y))).collect(Collectors.toSet());
    }

    static <A> Set<A> diff(Set<A> left, Set<A> right) {
        HashSet<A> copy = new HashSet<>(left);
        copy.removeAll(right);
        return copy;
    }

    static <A> Set<A> intersect(Set<A> left, Set<A> right) {
        HashSet<A> copy = new HashSet<>(left);
        copy.retainAll(right);
        return copy;
    }

    static <A> Set<A> union(Set<A> left, Set<A> right) {
        HashSet<A> copy = new HashSet<>(left);
        copy.addAll(right);
        return copy;
    }

    static class MyService3 {
        private double threshold;
        public MyService3(double threshold) {
            this.threshold = threshold;
        }
        public Set<Discount> applyDiscounts(Set<Account> things) {
            return things.stream().filter(x -> !(x.region().equals(AMER) && x.spend() > threshold))
                    .map(Discount::new)
                    .collect(Collectors.toSet());
        }
    }
    Account accountA() {
        return new Account(
                new AccountId("1111"),
                SalesChannel.External,
                Region.NA,
                Segment.Enterprise,
                CountryCode.AE,
                new Sector("Finance"),
                1_500.0
        );
    }
    Account accountB() {
        return new Account(
                new AccountId("2222"),
                SalesChannel.External,
                Region.AMER,
                Segment.Enterprise,
                CountryCode.AE,
                new Sector("Finance"),
                1_500.0);
    }
    Account accountC() {
        return new Account(
            new AccountId("3333"),
            SalesChannel.External,
            Region.EMEA,
            Segment.Enterprise,
            CountryCode.AE,
            new Sector("Finance"),
            1_500.0
        );
    }

    @Property
    void canFoo(@ForAll Region region, @ForAll @DoubleRange(min=0, max=233333) Double thresholds) {

        System.out.println(region);
        System.out.println(thresholds);
    }
    @Test
    void log2() {
        System.out.println(Math.log(744_073_709_551_616.00) / Math.log(2));
    }

    record Thing(String shortCode){}
    class MyService5 {
        private int x;
        public MyService5() {
            this.x = 0;
        }
        void create() {
            try {

                System.out.print("Hello! " + x + "\n");
                x = x + 1;
                Thread.sleep(x % 10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        List<Thing> fetchAll() {
            return List.of();
        }

    }


    void nukeFromOrbit() {

    }

    record Bucket(){}
    record Queue(){}
    record KeyValueStore(){}

    class OnDemandInfra {

    }

    void withTestInfa(Runnable runnable) {
        try {


            runnable.run();
        } catch (Exception e) {

        }
    }

    @interface Transactional {}

    @Data
    class CustomerPreferences {
        String userId;
        String organization;
    }
    @Data
    static class Customer {
        String accountId;
        String email;
    }
    @Data
    static class License {
        String key;
    }
    @Data
    static class Part {
        String id;
        String name;
    }
    @Data
    static class PartDetails {
        Part part;
        String serial;
        String controlNum;
    }
    interface CustomerPrefsRepo {
        void save(CustomerPreferences prefs);
    }

    @Test
    void uuiids() {
        System.out.println(UUID.randomUUID().toString().substring(0, 8));
        System.out.println(UUID.randomUUID().toString().substring(0, 8));
        System.out.println(UUID.randomUUID().toString().substring(0, 8));
        System.out.println(UUID.randomUUID().toString().substring(0, 8));
        System.out.println(UUID.randomUUID().toString().substring(0, 8));
        System.out.println(UUID.randomUUID().toString().substring(0, 8));
        System.out.println(UUID.randomUUID().toString().substring(0, 8));
        System.out.println(UUID.randomUUID().toString().substring(0, 8));
    }


    interface CustomerRepo {
        void save(Customer c);
    }
    interface LicenseRepo {
        void save(License c);
    }
    interface PartRepo {
        void save(Part c);
    }
    interface PartDetailsRepo {
        void save(PartDetails c);
    }

    @Data
    static class DoSomethingResult {
        String partId;
    }

    interface PartHandler {
        DoSomethingResult doSomething(Part c);
    }

    private CustomerPrefsRepo preferencesRepo;
    private CustomerRepo customerRepo;
    private LicenseRepo licenseRepo;
    private PartRepo partRepo;
    private PartDetailsRepo detailsRepo;
    private PartHandler partHandler;


    @Test
    @Transactional
    void creatingPartsAndInstances() {
        // Arrange!!!
        Customer user = new Customer();
//        user.setAccountId("test-account-id");
        user.setEmail("test@example.com");
        customerRepo.save(user);

        CustomerPreferences prefs = new CustomerPreferences();
        prefs.setOrganization("test-org-id");
        preferencesRepo.save(prefs);

        License license = new License();
        license.setKey("LICENSE-123");
        licenseRepo.save(license);


        Part part = new Part();
        part.setName("Test Part");
        partRepo.save(part);

        PartDetails partInstance = new PartDetails();
        partInstance.setPart(part);
        partInstance.setSerial("PN-001");
        partInstance.setControlNum("000ABC");
        detailsRepo.save(partInstance);

        // Act!!!
        DoSomethingResult result = partHandler.doSomething(part);

        // Assert!!!!
        assertNotNull(result);
        assertEquals(part.getId(), result.getPartId());
    }

    @Test
    @Transactional
    void creatingPartsAndInstances2() {
        // Arrange!!!
        Customer customer = new Customer();
        customer.setAccountId("id-that-I-control-in-memory!");
        customer.setAccountId("test-account-id");
        customer.setEmail("test@example.com");

        CustomerPreferences prefs = new CustomerPreferences();
//        prefs.setUserId();
        prefs.setOrganization("test-org-id");


        License license = new License();
        license.setKey("LICENSE-123");

        Part part = new Part();
        part.setName("Test Part");

        PartDetails partInstance = new PartDetails();
        partInstance.setPart(part);
        partInstance.setSerial("PN-001");
        partInstance.setControlNum("000ABC");

        customerRepo.save(customer);
        preferencesRepo.save(prefs);
        licenseRepo.save(license);
        partRepo.save(part);
        detailsRepo.save(partInstance);

        // Act!!!
        DoSomethingResult result = partHandler.doSomething(part);

        // Assert!!!!
        assertNotNull(result);
        assertEquals(part.getId(), result.getPartId());
    }


    record Environment(String whoami) {}
    interface MyDependencyInjector2 {
        TotallyNotDynamoDB keyValueStore();
        String environment();

    }
    record TaskGroup(String value){}
    interface TotallyNotDynamoDB {
        Task save(Task task);
        Optional<Task> findActive(TaskGroup job);
    }
    private MyDependencyInjector2 myDependencyInjector2;

    record Task(UUID id, TaskGroup taskGroup, Instant startedOn, Instant mustCompleteBy) {
        public Task withId(UUID id) {
            return new Task(id, taskGroup, startedOn, mustCompleteBy);
        }
    }

    class ConflictException extends RuntimeException {

    }

    @Test
    void taskGroupsAreLeasedForTheirPlannedDuration() throws InterruptedException {
        TotallyNotDynamoDB storage = myDependencyInjector2.keyValueStore();
        Task task = new Task(
                UUID.randomUUID(),
                new TaskGroup("test-" + UUID.randomUUID()),
                Instant.now(),
                Instant.now().plusSeconds(2)
        );
        storage.save(task);

        assertEquals(
            storage.findActive(task.taskGroup()),
            Optional.of(task)
        );

        Task duplicateTask = task.withId(UUID.randomUUID());

        assertThrows(ConflictException.class, () -> {
            storage.save(duplicateTask);
        });
        assertEquals(
            storage.findActive(duplicateTask.taskGroup()),
            Optional.of(task)
        );

        Thread.sleep(TimeUnit.SECONDS.toMillis(2));

        assertEquals(
            storage.findActive(task.taskGroup()),
            Optional.empty()
        );
    }



    /**
     * We might make an integration test like this.
     */
    @Test
    void shortNamesIncrementWithoutGaps() {
        String prefix = datagen.randomStr(3, 3);
        List<Integer> sequence = IntStream.range(0, 1000).boxed().toList();

        // Creates thousands of new things so that we can verify the
        // short names increase in a strict n+1 sequence
        sequence.forEach((__) -> service5.create());

        List<String> shortCodesCreated = service5.fetchAll()
                .stream()
                .sorted()
                .map(Thing::shortCode)
                .toList();

        // Shortcodes should follow a strictly increasing seq
        // e.g. [n, n+1, n+2, ..., n+n].
        List<String> shortCodesExpected = sequence.stream()
                .map(num -> format("%s-%s", prefix, num))
                .toList();

        assertEquals(
            shortCodesExpected,
            shortCodesCreated
        );
    }

    /*
    // Check me out! Now my test setup is much more aggressive.
      const duration = new Date().getTime() + seconds(20);
      // creates a worker that will repeatedly spam our
      // database as fast as it can over the course of 10s.  
      const spammer = async () => {
        while (new Date().getTime() < duration) {
           await prisma.myThing.create({...SomePayload})
        }
      })
      // And now, rather than one single writer, we create a whole
      // pool of writers all concurrently modifying the database. They'll
      // hammer away as fast as they can for as long as we've configured.
      const await Promise.all(range(1000).map(spammer));
     
      // rest of the test is the same.
      // now we grab all the shortcodes that got created.
      const shortcodes = prisma.myThing.findMany({}).map(_.shortCode)
      // The expectation is that shortcuts increase strictly
      //     e.g. [n, n+1, n+2, ..., n+(seq.length-1)]
      // Which should be no different from any other arbitray range  
      const start = shortCodes[0]
      const end = shortCodes.length;
      const sequenceWithNoGaps = Array.range(start, end);
     
      expect(shortcodes).toEqual(sequenceWithNoGaps);
     */
@Test
void shortNamesIncrementWithoutGaps2() throws InterruptedException, ExecutionException {
    long end = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);
    String prefix = datagen.randomStr(3, 3);
    ExecutorService es = Executors.newFixedThreadPool(100);
    Runnable requestSpammer = () -> {
        while (System.currentTimeMillis() < end) {
            service5.create();
        }
    };

    CompletableFuture.allOf(
            CompletableFuture.runAsync(requestSpammer, es),
            CompletableFuture.runAsync(requestSpammer, es),
            CompletableFuture.runAsync(requestSpammer, es),
            CompletableFuture.runAsync(requestSpammer, es),
            CompletableFuture.runAsync(requestSpammer, es)
    ).get();

    List<String> shortCodesCreated = service5.fetchAll()
            .stream()
            .sorted()
            .map(Thing::shortCode)
            .toList();

    // Shortcodes should follow a strictly increasing seq
    // e.g. [n, n+1, n+2, ..., n+n].
    List<String> shortCodesExpected =
            IntStream.range(0, shortCodesCreated.size())
            .boxed()
            .map(num -> format("%s-%s", prefix, num))
            .toList();

    assertEquals(
        shortCodesExpected,
        shortCodesCreated
    );
}



    @Test
    void asdfasdfasd() {
//        System.out.println(randomString());
//        System.out.println(randomString());
//        System.out.println(randomString());
//        System.out.println(randomString());
//        System.out.println(new String(Character.toChars(0x110FF)));
        System.out.println(new ToyDataGenerator().randomStr(0, 50));
        System.out.println(new ToyDataGenerator().randomStr(0, 50));
        System.out.println(new ToyDataGenerator().randomStr(0, 50));
        System.out.println(new ToyDataGenerator().randomStr(0, 50));
        System.out.println(new ToyDataGenerator().randomStr(0, 50));
        ToyDataGenerator datagen = new ToyDataGenerator();
        System.out.println(datagen.randList(() -> datagen.randomStr(0, 50), 0, 5));
        System.out.println(datagen.randList(() -> datagen.randomStr(0, 50), 0, 5));
        System.out.println(datagen.randList(() -> datagen.randomStr(0, 50), 0, 5));
        System.out.println(datagen.randList(() -> datagen.randomStr(0, 50), 0, 5));
    }

    class ToyDataGenerator {
        private final Random random;

        public ToyDataGenerator() {
            this.random = new Random();
        }

        public <A> A oneOf(A[] values) {
            return values[random.nextInt(values.length)];
        }

        public String randomStr(int minLength, int maxLength) {
            int length = Math.max(minLength, this.random.nextInt(maxLength));
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < length; i++) {
                output.append((char) random.nextInt('A', '~'));
            }
            return output.toString();
        }

        <A> List<A> randList(Supplier<A> supplier, int minLength, int maxLength) {
            int length = Math.max(minLength, this.random.nextInt(maxLength));

            return IntStream.range(0, length).boxed().map(__ -> supplier.get()).toList();
        }
    }





    @Test
    void ignoresIrrelevantInputsVariant1() {
        Set<Account> items = Set.of(accountA(), accountB(), accountC());
        Set<Discount> discounts = this.service.applyDiscounts(items);
        assertEquals(2, discounts.size());
    }


    @Test
    void ignoresIrrelevantInputsVariant2() {
        Set<Account> shouldBeDropped = Set.of(accountB());
        Set<Account> shouldBeProcessed = Set.of(accountA(), accountB());
        Set<Account> allItems = union(shouldBeProcessed, shouldBeDropped);

        Set<Discount> discounts = this.service.applyDiscounts(allItems);
        assertEquals(shouldBeProcessed.size(), discounts.size());
    }

    @Test
    void ignoresIrrelevantInputsVariant3() {
        Set<Account> shouldBeDropped = Set.of(mkAcnt(AMER, 1_500.01));
        Set<Account> shouldBeProcessed = Set.of(accountA(), accountB());
        Set<Account> allItems = union(shouldBeProcessed, shouldBeDropped);

        MyService3 service = new MyService3(1_500.00);
        Set<Discount> discounts = service.applyDiscounts(allItems);
        assertEquals(shouldBeProcessed.size(), discounts.size());
    }

    static Account mkAcnt(Region region, double spend) {
        return new Account(
                new AccountId(UUID.randomUUID().toString()),
                SalesChannel.External,
                Region.NA,
                Segment.Enterprise,
                CountryCode.AE,
                new Sector("Finance"),
                spend);
    }

    Account mkAcnt2() {
        return new Account(
            new AccountId(UUID.randomUUID().toString()),
            this.datagen.oneOf(SalesChannel.values()),
            this.datagen.oneOf(Region.values()),
            this.datagen.oneOf(Segment.values()),
            this.datagen.oneOf(CountryCode.values()),
            new Sector(this.datagen.randomStr(2, 10)),
            this.datagen.random.nextDouble(0, Math.pow(2, 32))
        );
    }

    Account withSegment(Account account, Segment segment) {
        return new Account(
            account.id,
            account.channel,
            account.region,
            segment,
            account.code,
            account.sector,
            account.spend
        );
    }



    @Test
    void showingWhatYouNeedInTheTest() {
        Account enterpriseAccount = mkAcnt2().withSegment(Segment.Enterprise);
    }

    @Test
    void ignoresIrrelevantInputsVariant4() {
        double threshold = 1_500_000.00;

        Account aboveLimit = mkAcnt(AMER, threshold + 1.0);
        Account atLimit = mkAcnt(AMER, threshold);
        Account belowLimit = mkAcnt(AMER, threshold - 1.0);

        Set<Account> willBeDropped = Set.of(aboveLimit);
        Set<Account> shouldBeProcessed = Set.of(atLimit, belowLimit);
        Set<Account> accounts = union(willBeDropped, shouldBeProcessed);

        MyService3 service = new MyService3(threshold);
        Set<Discount> discounts = service.applyDiscounts(accounts);
        assertEquals(discounts.size(), shouldBeProcessed.size());
    }



    // but what about all the other states?
    // \forall x \in labels,
    @Test
    void ignoresIrrelevantInputsVariant5() {
        double threshold = 1_500_000.00;
        double aboveLimit = threshold + 1;
        double belowLimit = threshold + 1;

        Set<Account> allPossibleStates = new HashSet<>();
        for (Region region : Region.values()) {
            for (double spend : List.of(belowLimit, threshold, aboveLimit)) {
                allPossibleStates.add(mkAcnt(region, spend));
            }
        }

        Set<Account> noDiscountAllowed = Set.of(mkAcnt(AMER, aboveLimit));
        Set<Account> discountsAllowed = diff(allPossibleStates, noDiscountAllowed);

        MyService3 service = new MyService3(threshold);
        Set<Discount> discounts = service.applyDiscounts(allPossibleStates);
        assertEquals(discounts.size(), discountsAllowed.size());
    }





    // but what about all the other states?
    // \forall x \in labels,
    @Test
    void ignoresIrrelevantInputsVariant6() {
        double threshold = Double.MAX_VALUE; // new Random().nextDouble(10000);

        double aboveLimit = threshold + 1;
        double belowLimit = threshold - 1;

        Set<Account> allPossibleStates = product(
                Region.values(),
                List.of(belowLimit, threshold, aboveLimit),
                Sketch::mkAcnt);

        Set<Account> discountsNotAllowed = Set.of(mkAcnt(AMER, aboveLimit));
        Set<Account> discountsAllowed = diff(allPossibleStates, discountsNotAllowed);

        MyService3 service = new MyService3(threshold);
        Set<Discount> result = service.applyDiscounts(allPossibleStates);
        Set<Account> discountsApplied = result.stream().map(Discount::account).collect(Collectors.toSet());

        assertEquals(Set.of(), intersect(discountsApplied, discountsNotAllowed));
        assertEquals(discountsAllowed, discountsApplied);
    }

    class MyStorage {
        public void save(Account account) {}
    }
    @AllArgsConstructor
    class MyService4 {
        private double threshold;
        private MyStorage storage;

        public void computeAndSaveDiscounts(Set<Account> accounts) {
            for (Account account : accounts) {
                this.storage.save(account);
            }
        }
    }

@Test
void ignoresIrrelevantInputsVariant7() {
    double threshold = Double.MAX_VALUE; //new Random().nextDouble(Double.MAX_VALUE);
    double aboveLimit = threshold + 1;
    double belowLimit = threshold + 1;

    Set<Account> allPossibleStates = product(
            Region.values(),
            List.of(belowLimit, threshold, aboveLimit),
            Sketch::mkAcnt);

    Account amerAndAboveLimit = mkAcnt(AMER, aboveLimit);
    Set<Account> discountsNotAllowed = Set.of(amerAndAboveLimit);
    Set<Account> discountsAllowed = diff(allPossibleStates, discountsNotAllowed);

    MyStorage storage = Mockito.mock(MyStorage.class);
    MyService4 service = new MyService4(threshold, storage);
    service.computeAndSaveDiscounts(allPossibleStates);

    verify(storage, never()).save(amerAndAboveLimit);
    for (Account a : discountsAllowed) {
        verify(storage).save(a);
    }
}



    @ParameterizedTest
    @MethodSource("regionAndThresholds")
    void noDiscountForLargeAccountsInAMERVariant5(double threshold, Account account) {
        System.out.printf("threshold=%s, account=%s\n", threshold, account);
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> regionAndThresholds() {
        double threshold = new Random().nextDouble() + new Random().nextInt(10_000);
        double above = threshold + 1.0;
        double below = threshold - 1.0;
        return Arrays.stream(Region.values()).flatMap(region -> Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(threshold, mkAcnt(region, below)),
                org.junit.jupiter.params.provider.Arguments.of(threshold, mkAcnt(region, threshold)),
                org.junit.jupiter.params.provider.Arguments.of(threshold, mkAcnt(region, above))
        ));
    }

    static <A> A oneOf(A[] xs) {
        return xs[new Random().nextInt(xs.length)];
    }

    @Test
    void asdfasdfsd() {
        Random r = new Random();
        Stream.of(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1).forEach(__ -> {
            System.out.println(r.nextGaussian(10, 1));
        });
    }

    @ParameterizedTest
    @MethodSource("anyArbitraryThreshold")
    void noDisc(double threshold) {
        System.out.println(threshold);
        System.out.println(Double.MAX_VALUE);
        System.out.println(Double.MAX_VALUE - 1);
    }

    static Stream<Arguments> anyArbitraryThreshold() {
        Random r = new Random();
        return Stream.of(Arguments.of(r.nextDouble(Double.MAX_VALUE) - 1.0));
    }







//    void setup() {
//
//    }


    @Test
    void testMyThing() {
        List<String> labels = List.of("A", "B", "C", "D");
        List<Network> networks = List.of(
            new Network("1.0.0.0/8"),
            new Network("172.16.0.0/12"),
            new Network("192.168.0.1/16")
        );

        List<Discount> discount = someComplicatedFunction(labels, networks);
        assertEquals(
                discount.size(),
                12
        );
    }


    @Test
    void testMyThing2() {
        List<String> labels = List.of("A", "B", "C", "D");
        List<Network> networks = List.of(
                new Network("1.0.0.0/8"),
                new Network("172.16.0.0/12"),
                new Network("192.168.0.1/16")
        );

        List<Discount> discount = someComplicatedFunction(labels, networks);
        assertEquals(
                discount.size(),
                labels.size() * networks.size()
        );
    }

    List<String> randomListOfLabels() {
        return List.of();
    }

    List<Network> randomListOfNetworks() {
        Random random = new Random();
        random.nextInt(4);
        return List.of();
    }

    @Test
    void testMyThing3() {
        List<String> labels = randomListOfLabels();
        List<Network> networks = randomListOfNetworks();

        List<Discount> discount = someComplicatedFunction(labels, networks);
        assertEquals(
                discount.size(),
                labels.size() * networks.size()
        );
    }

    enum Status {Pending, Finished};
    record ScheduledAction(UUID id, Instant mustCompleteBy, Status status){}

    record Expected(ScheduledAction action, Status status) {
        public static Expected of(ScheduledAction a, Status b) {
            return new Expected(a, b);
        }
    }
    @Builder
    record Scenario(
        Instant simulatedCurrentTime,
        List<Expected> stateBefore,
        List<Expected> stateAfter){}


    /**
     *
     * Finds any actions past their SLA and marks them as Abandoned
     *
     * Legend:
     *  * | = Unit of time
     *  * v = Arrival Time of the Action
     *  * C = Expected completion time
     *  * N = Current clock time
     *  * < = Clock time less its grace period
     *             (b)
     *     (a)     <<<N  (c)
     *    <<<N        <<<N
     *  v-----------C
     *  |   |   |   |   |   |
     *
     *  (a) Within SLA; no action.
     *  (b) Clock time is stateAfter SLA, but within Grace Period; No Action
     *  (c) Out of SLA: abandoned.
     *
     *  Fundamentally, this is saying
     *  NoPendingExistOutsideSLA ==
     *     \A a \in actions, a.willCompleteBy < (currentTime - buffer) => action.status = Finished
     */
    @Test
    void testSchedulerEnforcesSLA() {

        Instant now = Instant.now();
        ScheduledAction action1 = new ScheduledAction(UUID.randomUUID(), now, Pending);
        ScheduledAction action2 = new ScheduledAction(UUID.randomUUID(), now.plusSeconds(60), Pending);

        Scenario.builder()
            .simulatedCurrentTime(action1.mustCompleteBy().minusSeconds(1))
            .stateBefore(List.of(of(action1, Pending), of(action2, Pending)))
            .stateAfter(List.of(of(action1, Pending), of(action2, Pending)))
            .build();

        List<Scenario> testCases = List.of(
            new Scenario(
                action1.mustCompleteBy().minusSeconds(1),
                List.of(of(action1, Pending), of(action2, Pending)),
                List.of(of(action1, Pending), of(action2, Pending))
            ),
            new Scenario(
                action1.mustCompleteBy().plusSeconds(1),
                List.of(of(action1, Pending), of(action2, Pending)),
                List.of(of(action1, Finished), of(action2, Pending))
            ),
            new Scenario(
                action2.mustCompleteBy().plusSeconds(1),
                List.of(of(action1, Finished), of(action2, Pending)),
                List.of(of(action1, Finished), of(action2, Finished))
            )
        );

        Storage storage = new Storage();
        testCases.forEach(testcase -> {
            testcase.stateBefore().forEach(expected -> {
                assertEquals(
                    storage.load(expected.action().id()).status(),
                    expected.status()
                );
            });

            Clock simulatedClock = Clock.fixed(testcase.simulatedCurrentTime(), ZoneOffset.UTC);
            Scheduler scheduler = new Scheduler(simulatedClock);
            scheduler.markAgingAsAbandoned();

            testcase.stateAfter().forEach(expected -> {
                assertEquals(
                    storage.load(expected.action().id()).status(),
                    expected.status()
                );
            });
        });
    }

    record FileResult(String foo, List<String> stuff){}
    class FileProcessor {
        public FileResult process(Path path) {
            return new FileResult("", List.of(""));
        }
    }

    @Test
    void fileExample() throws IOException {
        var result = new FileProcessor().process(Path.of("path/to/my/test-file.whatever"));
        assertEquals("bar", result.foo());  // Good luck
        assertEquals(3, result.stuff().size());
    }


    @Test
    void testSchedulerEnforcesSLA2() {

        Instant now = Instant.now();
        ScheduledAction action1 = new ScheduledAction(UUID.randomUUID(), now, Pending);
        ScheduledAction action2 = new ScheduledAction(UUID.randomUUID(), now.plusSeconds(60), Pending);

        assertEquals(action1.status(), Pending);
        assertEquals(action2.status(), Pending);
        Clock simulatedClock = Clock.fixed(action1.mustCompleteBy().minusSeconds(1), ZoneOffset.UTC);
        Scheduler scheduler = new Scheduler(simulatedClock);
        scheduler.markAgingAsAbandoned();
        assertEquals(action1.status(), Pending);
        assertEquals(action2.status(), Pending);

        assertEquals(action1.status(), Pending);
        assertEquals(action2.status(), Pending);
        simulatedClock = Clock.fixed(action1.mustCompleteBy().plusSeconds(1), ZoneOffset.UTC);
        scheduler = new Scheduler(simulatedClock);
        scheduler.markAgingAsAbandoned();
        assertEquals(action1.status(), Finished);
        assertEquals(action2.status(), Pending);


        assertEquals(action1.status(), Finished);
        assertEquals(action2.status(), Pending);
        simulatedClock = Clock.fixed(action1.mustCompleteBy().plusSeconds(1), ZoneOffset.UTC);
        scheduler = new Scheduler(simulatedClock);
        scheduler.markAgingAsAbandoned();
        assertEquals(action1.status(), Finished);
        assertEquals(action2.status(), Finished);




        List<Scenario> testCases = List.of(
                new Scenario(
                        action1.mustCompleteBy().minusSeconds(1),
                        List.of(of(action1, Pending), of(action2, Pending)),
                        List.of(of(action1, Pending), of(action2, Pending))
                ),
                new Scenario(
                        action1.mustCompleteBy().plusSeconds(1),
                        List.of(of(action1, Pending), of(action2, Pending)),
                        List.of(of(action1, Finished), of(action2, Pending))
                ),
                new Scenario(
                        action2.mustCompleteBy().plusSeconds(1),
                        List.of(of(action1, Finished), of(action2, Pending)),
                        List.of(of(action1, Finished), of(action2, Finished))
                )
        );

        Storage storage = new Storage();
        testCases.forEach(testcase -> {
            testcase.stateBefore().forEach(expected -> {
                assertEquals(
                        storage.load(expected.action().id()).status(),
                        expected.status()
                );
            });




            testcase.stateAfter().forEach(expected -> {
                assertEquals(
                        storage.load(expected.action().id()).status(),
                        expected.status()
                );
            });
        });
    }


    static class Storage {
        ScheduledAction load(UUID id) {
            return new ScheduledAction(id, Instant.now(), Pending);
        }
    }

    static class Scheduler {

        public Scheduler(Clock clock) {
        }

        void markAgingAsAbandoned() {

        }
    }


    interface CloudSDK {
        Queue createQueue(String name);
        void deleteQueue(String name);
        List<String> listQueues();
        Bucket createBucket(String name);
        void deleteBucket(String name);
    }

    private CloudSDK cloudSDK;


record Namespace(String owner, String suite, String testName){
    static String ROOT = "integ";
}

class Scope {
    public static String everything(Namespace ns) {
        return Namespace.ROOT;
    }
    public static String ownedBy(Namespace ns) {
        return join("-", Namespace.ROOT, ns.owner);
    }
    public static String thisSuite(Namespace ns) {
        return join("-", Namespace.ROOT, ns.owner, ns.suite);
    }
    public static String thisTest(Namespace ns) {
        return join("-", Namespace.ROOT, ns.owner, ns.suite, ns.testName);
    }
}






String uniqueName(Namespace namespace) {
    return format(
        "%s-%s",
        Scope.thisTest(namespace),
        UUID.randomUUID().toString().substring(0, 8)
    );
}

    interface ReportsRepo {
        void upload(File file);
        void upload(String filename, String body);
    }
    interface ReportsQueue {
        void put(String message);
    }
    interface MyService6 {
        void run();
        List<Account> listAccounts();
    }
    private MyService6 myService6;
    private ReportsRepo reportsRepo;
    private ReportsQueue reportsQueue;

    @Test
    void extractsReportDataAndWritesToDB() throws IOException {
        Path path = Path.of("path/to/report.csv");
        reportsRepo.upload(path.toFile());
        reportsQueue.put("report.csv");

        myService6.run();
        List<Account> results = myService6.listAccounts();

        assertEquals(4, results.size());
        Account account = results.getFirst();
        assertEquals(Segment.Enterprise, account.segment());
        assertEquals(SalesChannel.Internal, account.channel());
        assertEquals(AMER, account.region());
    }

@Test
void extractsReportDataAndWritesToDB2() throws IOException {
    Set<Account> accounts = IntStream.range(0, 100)
            .boxed()
            .map(__ -> mkAcnt2())
            .collect(Collectors.toSet());

    Set<Account> usAccounts = accounts.stream()
            .filter(x -> x.code().equals(CountryCode.US))
            .collect(Collectors.toSet());

    String fileContents = serialize(accounts);
    // Now we have file we can do anything with that we constructed
    // right here in the code. No hidden provenance.

    reportsRepo.upload("report.csv", fileContents);
    reportsQueue.put("report.csv");

    myService6.run();
    List<Account> results = myService6.listAccounts();

    assertEquals(usAccounts.size(), results.size());
    assertEquals(usAccounts, Set.copyOf(results));
}

    String serialize(Collection<Account> accounts) {
        return accounts.stream()
            .map(account -> sjoin(",", account.id(), account.region /*...*/))
            .reduce("", (acc, val) -> join("\n", acc, val));
    }

    static String sjoin(String on, Object... values) {
        return join(on, Arrays.stream(values).map(Object::toString).toArray(String[]::new));
    }



@AllArgsConstructor
class InfraTooling {
    private CloudSDK cloudSDK;
    private Namespace namespace;

    Queue createQueue() {
        return cloudSDK.createQueue(uniqueName(namespace));
    }

    void cleanup(Function<Namespace, String> prefix) {
        deleteQueues(prefix);
//        deleteBuckets(prefix);
//        deleteWhatever(prefix);
    }

    void deleteQueues(Function<Namespace, String> strat) {
        for (String name : cloudSDK.listQueues()) {
            if (name.startsWith(strat.apply(namespace))) {
                cloudSDK.deleteQueue(name);
            }
        }
    }
}

    @Test
    void myCoolIntegrationTest() {
        String owner = myDependencyInjector2.environment();
        Namespace namespace = new Namespace(owner, "chapter07", "testMyThing");
        InfraTooling infra = new InfraTooling(cloudSDK, namespace);

        Queue inputQueue = infra.createQueue();
        Queue outputQueue = infra.createQueue();
    //    Bucket bucket = infra.createBucket();

        // test code here

        infra.cleanup(Scope::thisTest);
    }

    record Credit(){}
    public interface BillingAPI {
        Invoice createInvoice(/*...*/);
        Optional<Invoice> getInvoice(/*...*/);
        Invoice addLineItems(/*...*/);
        Invoice computeTax(/*...*/);
        Invoice submitInvoice(/*...*/);
        Invoice applyPayment(/*...*/);
        Invoice stopPayment(/*...*/);
        List<Invoice> find(/*...*/);
        Credit createRefund(/*...*/);
        Optional<Credit> getRefund(/*...*/);
        Invoice applyCredits(/*...*/);
    }
}