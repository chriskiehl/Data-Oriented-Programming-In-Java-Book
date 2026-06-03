package dop.chapter12;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Stream.generate;
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
        String prefix = randPrefix();
        List<Integer> sequence = IntStream.range(0, 1000).boxed().toList();
        // Shortcodes should follow a strictly increasing seq
        // e.g. [n, n+1, n+2, ..., n+n].
        List<String> expected = sequence.stream()
                .map(num -> format("%s-%s", prefix, num))
                .toList();


        // Creates thousands of new items so that we can verify the
        // short names increase in a strict n+1 sequence
        sequence.forEach((__) -> inventory.createItem(prefix));

        List<String> shortCodesCreated = inventory.fetchAll()
                .stream()
                .sorted()
                .map(Thing::shortCode)
                .toList();

        assertEquals(
            expected,
            shortCodesCreated
        );
    }

    static Random rand = new Random();

    String randPrefix() {
        return String.format("%s%s%s",
                (char)rand.nextInt('A', 'Z'),
                (char)rand.nextInt('A', 'Z'),
                (char)rand.nextInt('A', 'Z'));
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

