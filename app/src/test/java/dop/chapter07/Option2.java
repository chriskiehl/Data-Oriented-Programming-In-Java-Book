package dop.chapter07;

import dop.chapter07.Sketch.CaseStage2;
import dop.chapter07.Sketch.CustomerImpact;
import dop.chapter07.Sketch.RootCause;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;

public class Option2 {

    static <A> BinaryOperator<Optional<A>> mmerge(BinaryOperator<A> f) {
        return (a, b) -> lift(f).apply(a, b).or(() -> a.isPresent() ? a : b);
    }

    static <A> BinaryOperator<Optional<A>> lift(BinaryOperator<A> f) {
        return (Optional<A> oa, Optional<A> ob) -> oa.flatMap(a -> ob.map(b -> f.apply(a, b)));
    }

    record MessyData(
            String id,
            Optional<CaseStage2> caseStage,
            Optional<RootCause> rootCause,
            Optional<Boolean> isWhiteGlove
    ){
        public MessyData merge(MessyData other) {
            return new MessyData(
                    id,
                    mergeCaseCause.apply(caseStage, other.caseStage()),
                    mergeRootCause.apply(rootCause, other.rootCause()),
                    mmerge(Option2::mmm).apply(isWhiteGlove, other.isWhiteGlove())
            );
        }
    }

    void canonicalExample() {
        List<MessyData> ys = List.of(
                new MessyData("1", Optional.of(CaseStage2.FOO), Optional.of(RootCause.DISPUTE), Optional.of(false)),
                new MessyData("1", Optional.of(CaseStage2.FOO), Optional.of(RootCause.DISPUTE), Optional.of(false))
        );

        List<MessyData> canonicalExample2 = List.copyOf(ys.stream()
                .collect(toMap(MessyData::id, Function.identity(), MessyData::merge))
                .values());
    }

    static Boolean mmm(Boolean a, Boolean b) {
        return a || b;
    }

    static CustomerImpact impact(CaseStage2 stage) {
        return switch (stage) {
            case FOO -> CustomerImpact.FAVORS;
            case BAR -> CustomerImpact.HARMS;
        };
    }
    static CustomerImpact impact(RootCause stage) {
        return switch (stage) {
            case TAX, DISPUTE -> CustomerImpact.FAVORS;
            case SUSPENSION -> CustomerImpact.HARMS;
        };
    }

    static <A> A max(Comparator<A> comp, A a, A b) {
        return comp.compare(a, b) >= 0 ? a : b;
    }

    static <A> BinaryOperator<A> hmm(Function<A, CustomerImpact> f, Comparator<A> comparator) {
        return (a,b) -> {
            if (f.apply(a).equals(f.apply(b))) {
                return max(comparator, a, b);
            } else {
                return f.apply(a).equals(CustomerImpact.FAVORS) ? a : b;
            }
        };
    }

    static BinaryOperator<Optional<RootCause>> mergeRootCause = mmerge(hmm(Option2::impact, comparing(RootCause::name)));
    static BinaryOperator<Optional<CaseStage2>> mergeCaseCause = mmerge(hmm(Option2::impact, comparing(CaseStage2::name)));


}
