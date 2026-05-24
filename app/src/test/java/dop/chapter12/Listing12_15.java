package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class Listing12_15 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.15
     * ───────────────────────────────────────────────────────
     * Randomizing the infrastructure
     * ───────────────────────────────────────────────────────
     */
    @Test
    void myCoolIntegrationTest () {
        // spinning up infrastructure right before we run the test
        Queue inputQueue = cloudSDK.createQueue(UUID.randomUUID().toString());
        Queue outputQueue = cloudSDK.createQueue(UUID.randomUUID().toString());
        Bucket bucket = cloudSDK.createBucket(UUID.randomUUID().toString());
        // rest of test
    }















    CloudSDK cloudSDK;
    interface Queue {}
    interface Bucket {}
    interface CloudSDK {
        Queue createQueue(String name);
        Bucket createBucket(String name);
    }
}

