package dop.chapter07;

import dop.chapter07.Sketch.CustomerImpact;

import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;





public class Option1 {

    static <A> Optional<A> merge6(BinaryOperator<A> f, Optional<A> a, Optional<A> b) {
        return lift(f).apply(a, b).or(() -> a.isPresent() ? a : b);
    }

    static <A> BinaryOperator<Optional<A>> lift(BinaryOperator<A> f) {
        return (Optional<A> oa, Optional<A> ob) -> oa.flatMap(a -> ob.map(b -> f.apply(a, b)));
    }

    record MessyData(
            String id,
            Optional<Sketch.CaseStage2> caseStage,
            Optional<Sketch.RootCause> rootCause,
            Optional<Boolean> isWhiteGlove
    ){
        public MessyData merge(MessyData other) {
            return new MessyData(
                    id,
                    merge6(Option1::mmmmm, caseStage, other.caseStage()),
                    merge6(Option1::mmmm, rootCause, other.rootCause()),
                    merge6(Option1::mmm, isWhiteGlove, other.isWhiteGlove())
            );
        }
    }

    void canonicalExample() {
        List<MessyData> ys = List.of(
                new MessyData("1", Optional.of(Sketch.CaseStage2.FOO), Optional.of(Sketch.RootCause.DISPUTE), Optional.of(false)),
                new MessyData("1", Optional.of(Sketch.CaseStage2.FOO), Optional.of(Sketch.RootCause.DISPUTE), Optional.of(false))
        );

        List<MessyData> canonicalExample2 = List.copyOf(ys.stream()
                .collect(toMap(MessyData::id, Function.identity(), MessyData::merge))
                .values());
    }

    static Boolean mmm(Boolean a, Boolean b) {
        return a || b;
    }

    static CustomerImpact impact(Sketch.CaseStage2 stage) {
        return switch (stage) {
            case FOO -> CustomerImpact.FAVORS;
            case BAR -> CustomerImpact.HARMS;
        };
    }
    static CustomerImpact impact(Sketch.RootCause stage) {
        return switch (stage) {
            case TAX, DISPUTE -> CustomerImpact.FAVORS;
            case SUSPENSION -> CustomerImpact.HARMS;
        };
    }

    static String max(String a, String b) {
        return a.compareTo(b) >= 0 ? a : b;
    }


    static Sketch.CaseStage2 mmmmm(Sketch.CaseStage2 a, Sketch.CaseStage2 b) {
        if (a.impact.equals(b.impact)) {
            return Sketch.CaseStage2.valueOf(max(a.name(), b.name()));
        } else {
            return a.impact.equals(CustomerImpact.FAVORS) ? a : b;
        }
    }


    static Sketch.RootCause mmmm(Sketch.RootCause a, Sketch.RootCause b) {
        if (impact(a).equals(impact(b))) {
            return Sketch.RootCause.valueOf(max(a.name(), b.name()));
        } else {
            return impact(a).equals(CustomerImpact.FAVORS) ? a : b;
        }
    }
}
