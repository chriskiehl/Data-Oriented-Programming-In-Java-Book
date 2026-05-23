package dop.chapter07;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Listing7_21 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.21
     * ───────────────────────────────────────────────────────
     * Same inputs, different answers
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        int x = 1;
        int y = 2;
        int z = 3;
        // The order in which we apply the operations changes the answer
        assertEquals(x - y, -1);
        assertEquals(y - x, 1);  
        // similarly, different groupings give different answers
        assertEquals(1 - (2 - 3), 2);
        assertEquals((1 - 2) - 3, -4);
    }
}
