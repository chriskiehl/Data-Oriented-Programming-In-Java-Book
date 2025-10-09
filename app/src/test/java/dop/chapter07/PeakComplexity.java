package dop.chapter07;

import dop.chapter07.PeakComplexity.Result.Tie;
import dop.chapter07.PeakComplexity.Result.Winner;
import dop.chapter07.Sketch.CaseStage2;
import dop.chapter07.Sketch.CustomerImpact;
import dop.chapter07.Sketch.RootCause;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;

public class PeakComplexity {

    record Row(
            String id,
            Optional<CaseStage2> caseStage,
            Optional<RootCause> rootCause,
            Optional<Boolean> isWhiteGlove
    ){
        public Row merge(Row other) {
            return new Row(
                    id,
                    merge.choose(caseStage, other.caseStage()).value(),
                    merge2.choose(rootCause, other.rootCause()).value(),
                    merge3.choose(isWhiteGlove, other.isWhiteGlove()).value()
            );
        }
    }

    void canonicalExample() {
        List<Row> ys = List.of(
                new Row("1", Optional.of(CaseStage2.FOO), Optional.of(RootCause.DISPUTE), Optional.of(false)),
                new Row("1", Optional.of(CaseStage2.FOO), Optional.of(RootCause.DISPUTE), Optional.of(false))
        );

        List<Row> canonicalExample2 = List.copyOf(ys.stream()
                .collect(toMap(Row::id, Function.identity(), Row::merge))
                .values());
    }

    static CustomerImpact impact1(CaseStage2 stage) {
        return switch (stage) {
            case FOO -> CustomerImpact.HARMS;
            case BAR -> CustomerImpact.FAVORS;
        };
    }
    static CustomerImpact impact2(RootCause stage) {
        return switch (stage) {
            case TAX, DISPUTE -> CustomerImpact.FAVORS;
            case SUSPENSION -> CustomerImpact.HARMS;
        };
    }

    public sealed interface Result<A> {
        public record Winner<A>(A value) implements Result<A> {}
        public record Tie<A>(A opt1, A opt2) implements Result<A> {}
    }

    static <A> Choice<A> considering(Predicate<A> p) {
        return (o1, o2) -> p.test(o1) == p.test(o2)
                    ? new Tie<>(o1, o2)
                    : new Winner<>(p.test(o1) ? o1 : o2);
    }

    static <A, B extends Comparable<B>> Decision<A> decidingBy(Function<A,B> f) {
        return (o1, o2) -> f.apply(o1).compareTo(f.apply(o2)) >= 0
                ? new Winner<>(o1)
                : new Winner<>(o2);
    }

    static <A> Decision<Optional<A>> withOptional(Decision<A> comparator) {
        return (o1, o2) -> (o1.isPresent() && o2.isPresent())
                ? new Winner<>(Optional.of(comparator.choose(o1.get(), o2.get()).value()))
                : o1.isPresent()
                    ? new Winner<>(o1)
                    : new Winner<>(o2);
    }

    @FunctionalInterface
    interface Decision<A> {
        Winner<A> choose(A x, A y);
    }

    @FunctionalInterface
    interface Choice<A> {
        Result<A> choose(A x, A b);

        default Choice<A> andThen(Choice<A> choice) {
            return (o1, o2) -> switch (this.choose(o1, o2)) {
                    case Result.Tie(var a, var b) -> choice.choose(a, b);
                    case Result.Winner<A> v -> v;
            };
        }

        default Decision<A> andFinally(Decision<A> choice) {
            return (o1, o2) -> switch (this.choose(o1, o2)) {
                    case Result.Tie(var a, var b) -> choice.choose(a, b);
                    case Result.Winner<A> v -> v;
            };
        }
    }
    static boolean noCustomerImpact(CaseStage2 stage) {
        return impact1(stage).equals(CustomerImpact.FAVORS);
    }
    static boolean noCustomerImpact2(RootCause stage) {
        return impact2(stage).equals(CustomerImpact.FAVORS);
    }

    static Decision<Optional<CaseStage2>> merge = withOptional(considering(PeakComplexity::noCustomerImpact)
            .andThen(considering(PeakComplexity::noCustomerImpact))
            .andFinally(decidingBy(CaseStage2::name)));
    static Decision<Optional<RootCause>> merge2 = withOptional(considering(PeakComplexity::noCustomerImpact2)
            .andFinally(decidingBy(RootCause::name)));
    static Decision<Optional<Boolean>> merge3 = withOptional(decidingBy(Boolean::valueOf));


}
