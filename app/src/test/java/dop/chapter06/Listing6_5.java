package dop.chapter06;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;

@Log4j2
public class Listing6_5 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.5
     * ───────────────────────────────────────────────────────
     * Any interaction with the environment ruins its mathematical nature
     * ───────────────────────────────────────────────────────
     */
    record USD(BigDecimal value) {
        public USD add(USD other) {
            log.info("Adding {} to {}", this.value, other.value); // ◄── This robs the function of global determinism
            return new USD(this.value.add(other.value));
        }
    }

}
