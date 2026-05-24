package dop.chapter11;

import java.math.BigDecimal;

public record USD(BigDecimal value) implements Comparable<USD> {
    static USD valueOf(double value) {
        return new USD(BigDecimal.valueOf(value));
    }
    USD plus(USD other) {
        return new USD(this.value.add(other.value));
    }
    USD minus(USD other) {
        return new USD(this.value.subtract(other.value));
    }
    static USD ONE = new USD(BigDecimal.ONE);

    @Override
    public int compareTo(USD o) {
        return this.value().compareTo(o.value());
    }
}
