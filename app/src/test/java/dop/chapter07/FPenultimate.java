package dop.chapter07;

import dop.chapter07.Finalizing.RawData;

import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class FPenultimate {

    static <A extends UnaryOperator<A>> Optional<A> doo(Optional<A> opt1, Optional<A> opt2) {
        return opt1.flatMap(a -> opt2.map(a::apply)).or(() -> opt1.isPresent() ? opt1 : opt2);
    }

    static <A> Optional<A> add(BinaryOperator<A> addFunction, Optional<A> opt1, Optional<A> opt2) {
        return opt1.flatMap(a -> opt2.map(b -> addFunction.apply(a, b))).or(() -> opt1.isPresent() ? opt1 : opt2);
    }

//    static RawData merge(RawData x, RawData y) {
//        if (!x.id().equals(y.id())) {
//            throw new IllegalArgumentException("Bah!");
//        }
//        if x.id().
//    }
}
