package dop.chapter03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


public class Listing3_3 {


    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.3
     * ───────────────────────────────────────────────────────
     * Taking a closer look at our data. Each of these fields
     * is about something. It has a meaning within its domain.
     * Have we captured that meaning in our code? Let's look
     * at the data again.
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        // {
        //    "sampleId”: "UV-1",
        //    "day": 10,    ◄─── What the heck kind of measurement is "days"?
        //    "contactAngle": 6.28  ◄─── 6.28... *what*? Degrees? Radians? Other?
        // }
    }
}
