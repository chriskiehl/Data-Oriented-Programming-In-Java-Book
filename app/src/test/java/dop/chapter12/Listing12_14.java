package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Listing12_14 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.14
     * ───────────────────────────────────────────────────────
     * Creating isolated dependencies for a test
     * ───────────────────────────────────────────────────────
     */
    @Test
    void myCoolIntegrationTest () {
        // spinning up infrastructure right before we run the test
        Queue inputQueue = cloudSDK.createQueue("my-own-input-queue");
        Queue outputQueue = cloudSDK.createQueue("my-own-output-queue");
        Bucket bucket = cloudSDK.createBucket("bucket-for-my-test");

        // We construct our service using the placeholder infrastructure.
        // (This, of course, depends on us using good Dependency
        //  Injection practices)
        MyService service = new MyService(inputQueue, outputQueue, bucket);
        // now it's pretty standard arrange, act, assert.
        // just that the "arrange" is done against service infrastructure
        // that's completely isolated to our test
        inputQueue.put(makeInputMessage());

        service.run();

        // then we make sure everything behaved the way we expect.
        // for instance, the item in the input queue should have been consumed
        assertEquals(inputQueue.poll(), Optional.empty());
        // we want something in the output queue
        assertNotEquals(outputQueue.poll(), Optional.empty());
        // and something written to our bucket
        assertEquals(__/* lookup item in bucket here */, __);

        // then blow everything away
        cloudCDK.deleteQueue(inputQueue.name());
        cloudCDK.deleteQueue(outputQueue.name());
        cloudCDK.deleteBucket(bucket.name());
    }














    boolean __;
    boolean ___;
    CloudSDK cloudSDK;
    CloudCDK cloudCDK;
    record Message(){}
    Message makeInputMessage() { return null; }
    interface Queue {
        void put(Message message);
        Optional<Message> poll();
        String name();
    }
    interface Bucket {
        String name();
    }
    interface CloudSDK {
        Queue createQueue(String name);
        Bucket createBucket(String name);
    }
    interface CloudCDK {
        void deleteQueue(String name);
        void deleteBucket(String name);
    }
    static class MyService {
        MyService(Queue inputQueue, Queue outputQueue, Bucket bucket) {}
        void run() {}
    }
}

