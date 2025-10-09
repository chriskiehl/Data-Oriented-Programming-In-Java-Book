package dop.chapter07;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.concat;

public class Intermediate {
    public enum Policy {
        GRACE_PERIOD,
        FLEXIBLE,
        IMMEDIATE,
        STRICT,
        MANUAL_REVIEW
    }

    public enum AuditFinding {
        BILLING_ERROR,
        OUT_OF_COMPLIANCE,
        INACCURATE,
        NO_ISSUE
    }

    record MonoidM<A>(BinaryOperator<A> op, A identity){}

    record MyThing(String value) {

        static MyThing empty() {return new MyThing("");}
    }

    public enum SpecialAccount { Y, N }

    record Row(
            String id,
            Details details
    ) implements Starting.Semigroup<Row> {
        public static Row empty() {
            return new Row("", new Details(Optional.empty(),Optional.empty(),Optional.empty()));
        }

        public Row apply(Row row) {
            return row;
        }
    }

    record Details(Optional<Finalizing.Policy> policy,
               Optional<Finalizing.AuditFinding> findings,
               Optional<Finalizing.SpecialAccount> isSpecial){
        public Details merge(Details other) {
            return other;
        }

        public static Details empty() {
            return new Details(null, null, null);
        }
    }

    interface AssociativeBinaryOp<A> extends BinaryOperator<A> {
        default void assertInvariant(A other) {
        }
    }

    static void foo(List<Row> rows) {
//        Map<String, Details> result = rows.stream().collect(
//                gBy(Row::id,
//                        mapping(Row::details,
//                                reducing(Details.empty(), Details::merge))));
//
//        List<Row> finalized = result.entrySet().stream()
//                .map(entry -> new Row(entry.getKey(), entry.getValue()))
//                .toList();
//
//        Collection<Row> result2 = rows.stream().collect(
//                        groupingBy(Row::id, reducing(Row.empty(), Row::apply)))
//                .values();

    }


    static <A> List<A> add(List<A> xs, List<A> ys) {
        return Stream.concat(xs.stream(), ys.stream()).toList();
    }

    static <A> List<A> reverse(List<A> xs) {
        return List.copyOf(xs.reversed());
    }

    public static void main(String... args) {

        Stream<String> a = Stream.of("a");
        Stream<String> b = Stream.of("b");
        Stream<String> c = Stream.of("c");
        a.equals(b);

//        System.out.println(concat(concat(a, b), c).reduce("", String::concat));
//        System.out.println(concat(a, concat(b, c)).reduce("", String::concat));
//        Stream.of(1,2,3,4).map().reduce(Money.empty(), Money::add)

        System.out.println(List.of(1,2,3,4,5,6,7,8,9,10)
                .stream().parallel().reduce(0, (x, y) -> x-y));
        System.out.println(List.of(1,2,3,4,5,6,7,8,9,10)
                .stream().reduce((x, y) -> x-y));
        System.out.println(1 - 2 - 3 - 4 - 5 - 6 - 7 - 8 - 9 - 10);
//        System.out.println(List.of(1,2,3,4,1,2,3,4)
//                .stream().parallel().reduce(0, (x, y) -> x-y));
//        System.out.println(List.of(1,2,3,4,1,2,3,4)
//                .stream().parallel().reduce(0, (x, y) -> x-y));
        List<Integer> xs = List.of(1,2,3);
        List<Integer> ys = List.of(4,3,2);

        System.out.println(reverse(add(xs, ys)));
        System.out.println(add(reverse(ys), reverse(xs)));
    }
}
