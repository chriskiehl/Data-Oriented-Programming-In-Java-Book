package dop.chapter11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Listing11_59 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 11.59
     * ───────────────────────────────────────────────────────
     * Example of a test without a narrative
     * ───────────────────────────────────────────────────────
     */
    @Test
    void assertClientDoesNotInterruptDuringShutdown() {
        ServiceClient client = MyDependencyInjector.getClient();
        ExecutorService es = Executors.newFixedThreadPool(1);
        CompletableFuture<String> example1 = CompletableFuture.supplyAsync(
            client::doSomething, es);
        es.shutdownNow();
        Throwable ex = Assertions.assertThrows(
            ExecutionException.class, example1::get);
        Assertions.assertEquals(
            ex.getCause().getClass(), InternalFailure.class);

        es = Executors.newFixedThreadPool(1);
        CompletableFuture<String> example2 = CompletableFuture.supplyAsync(
            client::doSomething, es);
        es.shutdown();
        Assertions.assertDoesNotThrow(() -> example2.get());
    }














    interface ServiceClient {
        String doSomething();
    }
    static class MyDependencyInjector {
        static ServiceClient getClient() { return () -> ""; }
    }
    static class InternalFailure extends RuntimeException {}
}
