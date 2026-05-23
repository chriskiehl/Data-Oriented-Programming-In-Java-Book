package dop.chapter07;

import java.util.Optional;
import java.util.function.BinaryOperator;

public class Listing7_57 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.57
     * ───────────────────────────────────────────────────────
     * An implementation of lift
     * ───────────────────────────────────────────────────────
     */
    static <A> BinaryOperator<Optional<A>> lift(BinaryOperator<A> f) {
//                    ▲            ▲                    ▲
//                    │            │                    │
//                    └──── Check out our type signature.
//                          We take a BinaryOperator from the normal universe and return
//                          a BinaryOperator in the Optional one
        return (opt1, opt2) -> {
//               ▲      ▲
//               └──── This is where we “moved” the arguments. They’re now returns
//                     as part of the new BinaryOperator we’re creating with a lambda
            if (opt1.isPresent() && opt2.isPresent()) {              //  ┐
                return Optional.of(f.apply(opt1.get(), opt2.get())); //  │◄── The rest of the implementation
            } else {                                                 //  │    is business as usual
                return Optional.empty();                             //  │
            }                                                        //  ┘
        };
    }

    // ALTERNATIVE IMPLEMENTATION: 
    class Example2 {
        static <A> BinaryOperator<Optional<A>> lift(BinaryOperator<A> f) {
            // If you prefer a more functional style, here’s the same thing using map and flatMap
            return (opt1, opt2) -> opt1.flatMap(x -> opt2.map(y -> f.apply(x, y)));
        }
    }
}
