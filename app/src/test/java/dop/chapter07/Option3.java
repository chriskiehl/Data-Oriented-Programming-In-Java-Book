package dop.chapter07;

import dop.chapter07.Sketch.CaseStage2;
import dop.chapter07.Sketch.CustomerImpact;
import dop.chapter07.Sketch.RootCause;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.stream.Collectors.toMap;

public class Option3 {
    
    static <A> BinaryOperator<Optional<A>> mmerge(BinaryOperator<A> f) {
        return (a, b) -> lift(f).apply(a, b).or(() -> a.isPresent() ? a : b);
    }

    static <A> BinaryOperator<Optional<A>> lift(BinaryOperator<A> f) {
        return (Optional<A> oa, Optional<A> ob) -> oa.flatMap(a -> ob.map(b -> f.apply(a, b)));
    }

    record Row(
            String id,
            Optional<CaseStage2> caseStage,
            Optional<RootCause> rootCause,
            Optional<Boolean> isWhiteGlove
    ){
        public Row merge(Row other) {
            return new Row(
                    id,
                    baaar.apply(caseStage, other.caseStage()),
                    foooo.apply(rootCause, other.rootCause()),
                    baz.apply(isWhiteGlove, other.isWhiteGlove())
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
            case FOO -> CustomerImpact.FAVORS;
            case BAR -> CustomerImpact.HARMS;
        };
    }
    static CustomerImpact impact2(RootCause stage) {
        return switch (stage) {
            case TAX, DISPUTE -> CustomerImpact.FAVORS;
            case SUSPENSION -> CustomerImpact.HARMS;
        };
    }

    static <A> Comparator<Optional<A>> compareOpt(Comparator<A> comparator) {
        return (o1, o2) -> (o1.isPresent() && o2.isPresent())
                ? comparator.compare(o1.get(), o2.get())
                : o1.isPresent() ? 1 : 0;
    }


    static BinaryOperator<Optional<RootCause>> foooo = maxBy(compareOpt(comparing(Option3::impact2).thenComparing(RootCause::name)));
    static BinaryOperator<Optional<CaseStage2>> baaar = maxBy(compareOpt(comparing(Option3::impact1).thenComparing(CaseStage2::name)));
    static BinaryOperator<Optional<Boolean>> baz= maxBy(compareOpt(Boolean::compareTo));
}
