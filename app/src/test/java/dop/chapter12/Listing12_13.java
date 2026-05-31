package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing12_13 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.13
     * ───────────────────────────────────────────────────────
     * Hammering the item creation logic with a pool of threads
     * ───────────────────────────────────────────────────────
     */
    @Test
    void shortNamesIncrementWithoutGaps() throws Exception {
        long end = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);
//           ^ These tests are less "arrange, act, assert" and more "run for a while
//             and see what happens"
        String prefix = datagen.randomStr(3, 3);
        Runnable requestSpammer = () -> {
            while (System.currentTimeMillis() < end) {    //  Rather than us telling Java how
                inventory.createItem(prefix);             //  to sequence the calls, we just let
            }                                             //  it hammer away in a loop
        };                                                //

        ExecutorService es = Executors.newFixedThreadPool(5);
        CompletableFuture.allOf(
            CompletableFuture.runAsync(requestSpammer, es),  //  ┐
            CompletableFuture.runAsync(requestSpammer, es),  //  │
            CompletableFuture.runAsync(requestSpammer, es),  //  │◄── and then run a bunch of those
            CompletableFuture.runAsync(requestSpammer, es),  //  │    loops in parallel
            CompletableFuture.runAsync(requestSpammer, es)   //  ┘
        ).get();

        List<String> shortCodesCreated = inventory.fetchAll()  //  ┐
            .stream()                                          //  │
            .sorted()                                          //  │
            .map(Thing::shortCode)                             //  │
            .toList();                                         //  │

        // Shortcodes should follow a strictly increasing seq  //  │
        // e.g. [n, n+1, n+2, ..., n+n].                       //  │◄── The rest of the code is
        List<String> shortCodesExpected = IntStream            //  │    the same, but it's now making
            .range(0, shortCodesCreated.size())                //  │    a much more confident assertion
            .boxed()                                           //  │
            .map(num -> format("%s-%s", prefix, num))          //  │
            .toList();                                         //  │

        assertEquals(                                          //  │
            shortCodesExpected,                                //  │
            shortCodesCreated                                  //  │
        );                                                     //  ┘
    }















    DataGen datagen;
    Inventory inventory;
    record Thing(String shortCode){}
    interface DataGen {
        String randomStr(int min, int max);
    }
    interface Inventory {
        void createItem(String prefix);
        List<Thing> fetchAll();
    }
}

