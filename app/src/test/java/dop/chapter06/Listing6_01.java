package dop.chapter06;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class Listing6_01 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.1
     * ───────────────────────────────────────────────────────
     * We might call all of these “functions”
     * ───────────────────────────────────────────────────────
     */
    class Example {
        private int value;

        public static int increment(int x) {
            return x + 1;
        }

        public void setvalue(int value) {
            this.value = value;
        }

        public LocalDate getEndOfMonth() {
            return LocalDate.now().with(lastDayOfMonth());
        }

        public String sayHello(String name) {
            return LocalDateTime.now().getHour() < 12
                ? "Good morning, " + name
                : "Hello, " + name;

        }
    }
}
