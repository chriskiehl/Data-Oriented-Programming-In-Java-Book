package dop.chapter06;

import java.math.BigDecimal;

public class Listing6_4 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.4
     * ───────────────────────────────────────────────────────
     * Reading instance state is fine when it’s from an immutable value
     * ───────────────────────────────────────────────────────
     */
    record USD(BigDecimal value) {
        public USD add(USD other) {                       //  ┐
            return new USD(this.value.add(other.value));  //  │◄── This is still a deterministic assignment of inputs
            //                    ▲                       //  ┘    to outputs. It’s just that one input is implicit.
        }   //                    └─ This reads an
    }       //                       *immutable* value
}
