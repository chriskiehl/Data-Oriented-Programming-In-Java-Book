package dop.chapter01;

import dop.chapter01.Listings.RetryDecision.ReattemptLater;
import dop.chapter01.Listings.RetryDecision.RetryImmediately;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static dop.chapter01.Listings.RetryDecision.*;
import static java.time.LocalDateTime.now;

/**
 * Chapter 01 is a whirlwind tour of the main ideas of data-oriented
 * programming. It can all be summed up in a single phrase from Fred
 * Brooks: "representation is the essence of programming."
 */
public class Listings {




















    /**
     * (This modeling is not shown in the book for brevity)
     * We're creating a family of related data types. The mechanics of this
     * construct will be covered in Chapters 3 and 4.
     */
    sealed interface RetryDecision {
        record RetryImmediately(LocalDateTime next, int attemptsSoFar) implements RetryDecision {
        }

        record ReattemptLater(LocalDateTime next) implements RetryDecision {
        }

        record Abandoned() implements RetryDecision {
        }
    }



















    static class FailedTask {
        // blank. Here just to enable
        // compilation of listing 1.14 above
        // The fact that it tells us quite a bit without
        // us implementing anything is pretty nice feature!
    }







}
