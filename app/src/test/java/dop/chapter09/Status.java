package dop.chapter09;

import java.util.function.Predicate;

public sealed interface Status<A> {
    A value();


    final class Validated<A> implements Status<A> {
        private final A value;
        private Validated(A value) {
            this.value = value;
        }
        public A value() {
            return this.value;
        }

        public static <A> Status<A> validate(A value, Predicate<A> pred) {
            if (pred.test(value)) {
                return new Validated<>(value);
            } else {
                return new Invalid<>(value);
            }
        }
    }

    record Invalid<A>(A value) implements Status<A> {}

}
