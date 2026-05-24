package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing12_12 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.12
     * ───────────────────────────────────────────────────────
     * Sequential integration tests can hide problems in your code
     * ───────────────────────────────────────────────────────
     */
    @Test
    void shortNamesIncrementWithoutGaps() {
        // Shortcodes are a 3 character prefix
        String prefix = datagen.randomStr(3, 3);
        List<Integer> sequence = IntStream.range(0, 1000).boxed().toList();

        // Creates thousands of new items so that we can verify the
        // short names increase in a strict n+1 sequence
        sequence.forEach((__) -> inventory.createItem(prefix));

        List<String> shortCodesCreated = inventory.fetchAll()
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

