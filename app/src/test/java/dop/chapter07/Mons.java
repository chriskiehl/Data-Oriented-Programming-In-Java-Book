package dop.chapter07;

import java.util.List;
import java.util.function.BinaryOperator;

public class Mons {

    interface Monoid<A> extends BinaryOperator<A> {
        A empty();
    }

    record Monoidal<A>(BinaryOperator<A> op, A empty){}

    record Money(int cents){

        static Monoid<Money> moneyMonoid = new Monoid<Money>() {
            @Override
            public Money empty() {
                return new Money(0);
            }

            @Override
            public Money apply(Money money, Money money2) {
                return new Money(money.cents() + money2.cents());
            }
        };
    }



    void asdf(List<Money> xs) {
        Money result = xs.stream().reduce(Money.moneyMonoid.empty(), Money.moneyMonoid);
    }
}
