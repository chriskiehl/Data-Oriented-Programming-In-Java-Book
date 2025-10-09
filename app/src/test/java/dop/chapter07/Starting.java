package dop.chapter07;

import dop.chapter07.Finalizing.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Starting {

    static CustomerImpact policyImpact(Policy stage) {
        return switch (stage) {
            case GRACE_PERIOD, FLEXIBLE, MANUAL_REVIEW -> CustomerImpact.FAVORS;
            default -> CustomerImpact.HARMS;
        };
    }
    static CustomerImpact findingsImpact(AuditFinding stage) {
        return switch (stage) {
            case NO_ISSUE, INACCURATE -> CustomerImpact.FAVORS;
            default -> CustomerImpact.HARMS;
        };
    }

    interface Semigroup<A> extends UnaryOperator<A>{}
    interface Monoid<A> extends Semigroup<A> {
        A empty();
    }

    record Foo(String value) implements Semigroup<Foo> {

        @Override
        public Foo apply(Foo foo) {
            return new Foo(this.value() + foo.value());
        }
    }

    record Details(Optional<Policy> policy,
                   Optional<AuditFinding> findings,
                   Optional<SpecialAccount> isSpecial){
        public Details merge(Details other) {
            return other;
        }

        public static Details empty() {
            return new Details(null, null, null);
        }
    }
    

    static <A extends Semigroup<A>> Optional<A> apply(Optional<A> x, Optional<A> y) {
        Stream.concat(Stream.of(1,2), Stream.of()).toList();

        if (x.isPresent() && y.isPresent()) {
            return Optional.of(x.get().apply(y.get()));
        } else {
            return x.isPresent() ? x : y;
        }
    }

    // or if you prefer the more functional style
    static <A extends Semigroup<A>> Optional<A> apply2(Optional<A> ox, Optional<A> oy) {
        return ox.flatMap(oy::map).or(() -> ox.isPresent() ? ox : oy);
    }

    static <K, V> Map<K,V> merge(Map<K, V> left, Map<K,V> right, BinaryOperator<V> f) {
        return Stream.concat(
                left.entrySet().stream(),
                right.entrySet().stream()
        ).collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, f));
    }


    void asdfasdf(List<Foo> xs) {
        Optional<Foo> result = xs.stream().reduce(Foo::apply);

    }

    static Policy add(Policy x, Policy y) {
        if (policyImpact(x).equals(policyImpact(y))) {
            return x.name().compareTo(y.name()) >= 0 ? x : y;
        } else {
            return policyImpact(x).equals(CustomerImpact.FAVORS) ? x : y;
        }
    }

    static Boolean add(Boolean x, Boolean y) {
        return x || y;
    }

    static AuditFinding add(AuditFinding x, AuditFinding y) {
        if (findingsImpact(x).equals(findingsImpact(y))) {
            return x.name().compareTo(y.name()) >= 0 ? x : y;
        } else {
            return findingsImpact(x).equals(CustomerImpact.FAVORS) ? x : y;
        }
    }

    static AuditFinding add2(AuditFinding x, AuditFinding y) {
        if (findingsImpact(x).equals(findingsImpact(y))) {
            if (x.name().compareTo(y.name()) >= 0) {
                return x;
            } else {
                return y;
            }
        } else {
            return findingsImpact(x).equals(CustomerImpact.FAVORS) ? x : y;
        }
    }

    static AuditFinding add3(AuditFinding x, AuditFinding y) {
        return apply_(x, y, Starting::findingsImpact);
    }

    static Policy add3(Policy x, Policy y) {
        return apply_(x, y, Starting::policyImpact);
    }

    static <A extends Semigroup<A>> Optional<A> apply__(Optional<A> x, Optional<A> y) {
        if (x.isPresent() && y.isPresent()) {
            return Optional.of(x.get().apply(y.get()));
        } else {
            return x.isPresent() ? x : y;
        }
    }

    static <A extends Enum<A>> A apply_(A x, A y, Function<A, CustomerImpact> f) {
        if (f.apply(x).equals(f.apply(y))) {
            return pickByName(x, y);
        } else {
            return f.apply(x).equals(CustomerImpact.FAVORS) ? x : y;
        }
    }

    static <E extends Enum<E>> E add(E x, E y, Function<E, CustomerImpact> impact) {
        if (impact.apply(x).equals(impact.apply(y))) {
            return x.name().compareTo(y.name()) >= 0 ? x : y;
        } else {
            return impact.apply(x).equals(CustomerImpact.FAVORS) ? x : y;
        }
    }

    static <A extends Enum<A>> A pickByName(A x, A y) {
        return x.name().compareTo(y.name()) >= 0 ? x : y;
    }

    @Test
    void asdfasdfasdfasdfasdf() {
        Enum<AuditFinding> res = add(AuditFinding.BILLING_ERROR, AuditFinding.NO_ISSUE);
        Enum<AuditFinding> res2 = add(AuditFinding.BILLING_ERROR, AuditFinding.INACCURATE);
        add(Policy.FLEXIBLE, Policy.IMMEDIATE, Starting::policyImpact);
        apply_(AuditFinding.BILLING_ERROR, AuditFinding.NO_ISSUE, Starting::findingsImpact);

    }


    static <A> BinaryOperator<Optional<A>> lift(BinaryOperator<A> f) {
        return (Optional<A> opt1, Optional<A> opt2) -> opt1.flatMap(x -> opt2.map(y -> f.apply(x, y)));
    }

    static <A extends UnaryOperator<A>> Optional<A> add(Optional<A> opt1, Optional<A> opt2) {
        return opt1.flatMap(x -> opt2.map(y -> x.apply(y))).or(() -> opt1.isPresent() ? opt1 : opt2);
    }

    static <A> Optional<A> add(BinaryOperator<A> f, Optional<A> x, Optional<A> y) {
        return lift(f).apply(x, y).or(() -> x.isPresent() ? x : y);
    }

    static void dooooo(List<RawData> xs) {
        Collection<Optional<RawData>> result = xs.stream()
                .collect(groupingBy(RawData::id, reducing(RawData::merge)))
                .values();
    }


    record NonEmptyList<A>(List<A> items) {
        NonEmptyList {
            if (items.isEmpty()) {
            }
        }
    }

    static RawData merge(NonEmptyList<RawData> dupes) {
        RawData initial = dupes.items().getFirst();
        return dupes.items().stream().skip(1).reduce(initial, RawData::merge);
    }

    static List<RawData> cleanDuplicates(List<RawData> rows) {
        Map<String, List<RawData>> dupesById = rows.stream()
                .collect(groupingBy(RawData::id));

        return dupesById.values().stream()
                .map(NonEmptyList::new)
                .map(Starting::merge)
                .toList();
    }



    static Function<Integer, Integer> addOne = (x) -> x + 1;
    static Function<Integer, Double> divideBy10 = (x) -> x / 10.0;
    static Function<Double, String> show = (x) -> format("$%05.2f", x);

    void asdfasdfasdf() {
//    assertEquals(
//        addOne.andThen(Function.identity()).apply(100),
//        Function.<Integer>identity().andThen(addOne).apply(100)
//    )
    }

    public static void main(String... args) {

    }

}
