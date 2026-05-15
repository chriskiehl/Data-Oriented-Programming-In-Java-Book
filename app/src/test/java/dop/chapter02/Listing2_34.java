package dop.chapter02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Listing2_34 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.34
     * ───────────────────────────────────────────────────────
     * Transforming arguments during construction
     * ───────────────────────────────────────────────────────
     */
    record Rational(int num, int denom) {
        Rational {
            if (denom == 0) {
                throw new IllegalArgumentException(
                        "Hey! That’s not how math works!"
                );
            }
            int gcd = gcd(num, denom);
            num /= gcd;
            denom /= gcd;
            //    ▲
            //    └───── While we’re inside the constructor, we can transform
            //           the incoming argument as much as we want.
            //           Here we’re normalizing them before assignment.
        }
    }




    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            int tmp = b;
            b = a % b;
            a = tmp;
        }

        return a;
    }
}
