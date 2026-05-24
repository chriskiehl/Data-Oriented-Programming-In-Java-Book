package dop.chapter11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Listing11_60 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.60
     * ───────────────────────────────────────────────────────
     * Providing a narrative arc to guide the test
     * ───────────────────────────────────────────────────────
     */
    @Test
    void assertClientDoesNotInterruptDuringShutdown() {
        // The auto-generated ServiceClient does not play well with
        // interupts. They’re caught by a generic exception handler
        // that swallows and rethrows them as InternalFailures. This
        // pollutes our metrics with false positives
        //
        // We can demo this by spinning up a pool, starting some work,
        // then immediately halting
        ServiceClient client = MyDependencyInjector.getClient();
        ExecutorService es = Executors.newFixedThreadPool(1);
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(
            client::doSomething, es);
        // this issues interrupts to every thread
        es.shutdownNow();

        // These interupts are not handled gracefully by the client.
        Throwable ex = Assertions.assertThrows(
            ExecutionException.class, future1::get);
        // All we see is an InternalFailure
        // The cause is lost, which means we cannot disambiguate a
        // shutdown (expected) from a failure (unexpected)
        Assertions.assertEquals(
            ex.getCause().getClass(),
            InternalFailure.class);

        // Compare that with shutdown();
        es = Executors.newFixedThreadPool(1);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(
            client::doSomething, es);
        // This does not send interrupts.
        es.shutdown();
        // Clients are now tuned so that their work finishes (or times out)
        // without relying on interrupts.
        // So no more mysterious failures.
        Assertions.assertDoesNotThrow(() -> future2.get());
    }














    interface ServiceClient {
        String doSomething();
    }
    static class MyDependencyInjector {
        static ServiceClient getClient() { return () -> ""; }
    }
    static class InternalFailure extends RuntimeException {}
}
