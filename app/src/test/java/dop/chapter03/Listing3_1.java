package dop.chapter03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;


/**
 * Chapter 3 is about starting to explore the semantics
 * that govern the data within a domain. It looks at the
 * gaps that usually exist between what we "know" in our
 * heads about the things we're modeling, versus how much
 * of that knowledge actually ends up in the code (very
 * little).
 *
 * This chapter will give you the tools to see "through"
 * your programs into the underlying sets of values that
 * it denotes.
 */
public class Listing3_1 {




    /**
     * ───────────────────────────────────────────────────────
     * Listings 3.1
     * ───────────────────────────────────────────────────────
     * We begin where all good software books begin: woodworking.
     * The best part of woodworking is the last step of applying the
     * finish. One of my favorite finishes is a type of drying oil
     * called Tung oil. I geek out over this stuff and have been
     * slowly collecting data around its drying behavior.
     *
     * In real life, nobody lets me talk about this because it's too
     * boring. But you're stuck reading my book, so I'm not going to
     * waste my one chance to drone on about my data on oil curing rates.
     *
     * It's interesting. I swear.
     * ───────────────────────────────────────────────────────
     */
    @Test
    void example() {
        // Here's our data in Json form. We're going to turn it
        // into Java and see what we can learn along the way.
        // {
        //    "sampleId”: "UV-1",
        //    "day": 3,             ◄─── Tung oil cures painfully slow. It's measured in full days.
        //    "contactAngle": 17.4  ◄──┐ We can't see it curing, but we can measure it! This is
        // }                           │ the angle that a droplet of water forms on the surface of the wood
    }
}
