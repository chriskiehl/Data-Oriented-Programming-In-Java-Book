package dop.chapter06;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class Listing6_10 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.10
     * ───────────────────────────────────────────────────────
     * Functions make tests simple
     * ───────────────────────────────────────────────────────
     */
    @Test
    void testFigureOutDueDate() {
        // ◄── No more mocks or test doubles.
        LocalDate result = FeeService.figureOutDueDate(
            //   ┌─── Plain data!
            //   ▼
            LocalDate.of(2024, 11, 17),
            PaymentTerms.NET_30
            //             ▲
            //             └─ Plain data!
        );
        Assertions.assertEquals("...", "...");
    }















    enum PaymentTerms {NET_30}

    static class FeeService {
        static LocalDate figureOutDueDate(LocalDate date, PaymentTerms terms) {
            return null;
        }
    }
}
