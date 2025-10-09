package dop.chapter07;

//import dop.chapter07.Sketch.PartialOrder.TotalOrder.Tie;
//import dop.chapter07.Sketch.PartialOrder.TotalOrder.Winner;
import dop.chapter07.Finalizing.RawData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Sketch {


    static <A> BinaryOperator<Optional<A>> mergeInv(BinaryOperator<A> f) {
        return (a, b) -> {
            if (a.isPresent() && b.isPresent()) {
                return Optional.of(f.apply(a.get(), b.get()));
            }
            return a.isPresent() ? a : b;
        };
    }

    BinaryOperator<CaseStage2> doing(BinaryOperator<CaseStage2> f) {
        return (a,b) -> {
            if (impact(a).equals(impact(b))) {
                return f.apply(a, b);
            } else {
                return impact(a).equals(CustomerImpact.FAVORS) ? a : b;
            }
        };
    }


    static <A> Optional<A> merge(Optional<A> a, Optional<A> b, BinaryOperator<A> f) {
        if (a.isPresent() && b.isPresent()) {
            return Optional.of(f.apply(a.get(), b.get()));
        }
        return a.isPresent() ? a : b;
    }

    static <A> Optional<A> merge3(Optional<A> a, Optional<A> b, BinaryOperator<A> f) {
        return a.flatMap(
                aa -> b.map(
                bb -> f.apply(aa, bb)
                )).or(() -> a.isPresent() ? a : b);
    }

    static <A> Optional<A> merge4(Optional<A> a, Optional<A> b, BinaryOperator<A> f) {
        try {
            A v1 = a.get();
            A v2 = b.get();
            return Optional.of(f.apply(v1, v2));
        } catch (NoSuchElementException e) {
            return a.isPresent() ? a : b;
        }
    }

    static <A> Optional<A> merge5(Optional<A> a, Optional<A> b, BinaryOperator<A> f) {
        return lift(f).apply(a, b).or(() -> a.isPresent() ? a : b);
    }

    static <A> Optional<A> merge6(BinaryOperator<A> f, Optional<A> a, Optional<A> b) {
        return lift(f).apply(a, b).or(() -> a.isPresent() ? a : b);
    }

    static <A> BinaryOperator<Optional<A>> lift(BinaryOperator<A> f) {
        return (Optional<A> oa, Optional<A> ob) -> oa.flatMap(a -> ob.map(b -> f.apply(a, b)));
    }

    static BinaryOperator<CaseStage2> lexi() {
        return (a, b) -> a.name().compareTo(b.name()) >= 0 ? a : b;
    }

    static BinaryOperator<CaseStage2> byImpact() {
        return (a, b) -> impact(a).equals(impact(b))
                ? lexi().apply(a, b)
                : impact(a).equals(CustomerImpact.FAVORS) ? a : b;
    }

    static void asdfasdfasdfasdfasdf() {
        Optional<CaseStage2> a = Optional.empty();
        Optional<CaseStage2> b = Optional.empty();
        Optional<CaseStage2> x = lift(byImpact()).apply(a, b).or(() -> a.isPresent() ? a : b);

//        lift(sortLexi.combine(compareImpact)).apply(a, b).orElse(() -> a.isPresent() ? a : b);
//        takePresent(onConflict=compareImpact(onConflict=SortLexigographically))).apply(of(FOO), of(BAR))
    }


    record Bar(String value) {}

    static Bar mergeBar(Bar a, Bar b) {
        return a;
    }

    record Data(String id, Optional<String> attr1, Optional<Integer> attr2){

        public Data mappend(Data x) {
            return this;
        }
        public Squishy row() {
            return new Squishy(Optional.of(id), attr1, attr2);
        }
    }

    // NOT A MONOID, so this doesn't work. We'd need an identity
    // Which Java is already providing
    public static Optional<Data> mconcat(List<Data> xs) {
        return xs.stream().reduce(Data::mappend);
    }

    record Squishy(Optional<String> id, Optional<String> attr1, Optional<Integer> attr2) {
        public static Squishy empty() {
            return new Squishy(Optional.empty(), Optional.empty(), Optional.empty());
        }
    }

    public static Squishy combine(Squishy a, Squishy b) {
        return new Squishy(Optional.of(""), Optional.empty(), Optional.empty());
    }

//    public static Map<String, Object> add(Map<String, Object> a, Map<String, Object> b) {
//        return a;
//    }

    record Foo(String id, Squishy row){}

    static <K,V> Map<K, V> mergeLM(List<Map<K, V>> m) {
        return m.stream().reduce(new HashMap<>(), mergeM((a,b) -> a));
    }

    static <K,V> BinaryOperator<Map<K,V>> mergeM(BiFunction<V,V,V> f) {
        return (a, b) -> {
            HashMap<K, V> combined = new HashMap<>(a);
            for (Map.Entry<K,V> entry : b.entrySet()) {
                combined.merge(entry.getKey(), entry.getValue(), f);
            }
            return combined;
        };
    }

    static <K,V> Map<K,V> mergeM(Map<K,V> a, Map<K,V> b, BiFunction<V,V,V> f) {
        HashMap<K, V> combined = new HashMap<>(a);
        for (Map.Entry<K,V> entry : b.entrySet()) {
            combined.merge(entry.getKey(), entry.getValue(), f);
        }
        return combined;
    }

    static class DataRow {
        Map<String, Data> m;

        public DataRow() {
            this.m = Map.of();
        }

        private DataRow(Map<String, Data> m) {
            this.m = m;
        }

        public DataRow(Data c) {
            this.m = Map.of(c.id(), c);
        }

        public static DataRow empty() {
            return new DataRow();
        }

        public DataRow mappend(DataRow other) {
            return new DataRow(mergeM(this.m, other.m, Data::mappend));
        }
        public List<Data> values() {
            return List.copyOf(this.m.values());
        }
    }


    record ExampleData(
            String id,
            Optional<Boolean> isSuspended
    ){}


    record MessyData(
            String id,
            Optional<CaseStage2> caseStage,
            Optional<RootCause> rootCause,
            Optional<Boolean> isWhiteGlove
    ){
        public MessyData merge(MessyData other) {
            return new MessyData(
                    id,
                    merge6(Sketch::mmmmm, caseStage, other.caseStage),
                    merge6(Sketch::mmmm, rootCause, other.rootCause),
                    merge6(Sketch::mmm, isWhiteGlove, other.isWhiteGlove)
            );
        }
    }


    static Boolean mmm(Boolean a, Boolean b) {
        return a || b;
    }

    enum CaseStage implements Comparable<CaseStage> {
        FOO,BAR,BAZ
    }
    enum RootCause {TAX, DISPUTE, SUSPENSION}
    enum CustomerImpact {FAVORS, HARMS}








//    static CustomerImpact impact(CaseStage stage) {
//        return switch (stage) {
//            case FOO, BAR -> CustomerImpact.FAVORS;
//            case BAZ -> CustomerImpact.HARMS;
//        };
//    }
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

//    @FunctionalInterface
//    interface Max<A> {
//
//        A max(A a, A b);
//
//        <T, U extends Max<? super U>> Max<A> comparing(Function<? super T, ? extends U> keyExtractor) {
//            return (a, b) -> a.compareTo(b) ?
//        }
//    }


//    static void asdfasdfasdfa() {
//        max(comparing(Sketch::impact).thenComparing(CaseStage2::name), CaseStage2.FOO, CaseStage2.BAR);
//    }

    public enum CaseStage2 {
        FOO(CustomerImpact.FAVORS),
        BAR(CustomerImpact.HARMS);

        final CustomerImpact impact;

        CaseStage2(CustomerImpact impact) {
            this.impact = impact;
        }

        public CustomerImpact getImpact() {
            return this.impact;
        }
    }

    /*
    if (are the same) {
        handle conflict
    } else {
        pick the higher ranked one
    }

    choose()
      .andThen(refine)
      .andFinally(pick)

     */

    static CaseStage2 mmmmm(CaseStage2 a, CaseStage2 b) {
        // Favors, favors -> max(a, b);
        // Harms, harms -> max(a, b);
        // favors, harms -> a
        // harms, favors -> b
        if (a.impact.equals(b.impact)) {
            return CaseStage2.valueOf(max(a.name(), b.name()));
        } else {
            return a.impact.equals(CustomerImpact.FAVORS) ? a : b;
        }
    }




    static RootCause mmmm(RootCause a, RootCause b) {
        // Favors, favors -> max(a, b);
        // Harms, harms -> max(a, b);
        // favors, harms -> a
        // harms, favors -> b
        if (impact(a).equals(impact(b))) {
            return RootCause.valueOf(max(a.name(), b.name()));
        } else {
            return impact(a).equals(CustomerImpact.FAVORS)
                    ? a
                    : b;
        }
    }




    static RootCause mmmmmmm(Function<RootCause, CustomerImpact> f, RootCause a, RootCause b) {
        // Favors, favors -> max(a, b);
        // Harms, harms -> max(a, b);
        // favors, harms -> a
        // harms, favors -> b


//        Comparator<CaseStage2> foooo = comparing(Sketch::<CaseStage2>impact).thenComparing(CaseStage2::name);
        Comparator.
        comparing((CaseStage2 x) -> impact(x))
                .thenComparing(CaseStage2::name)
                .compare(CaseStage2.BAR, CaseStage2.FOO);
        if (f.apply(a).equals(f.apply(b))) {
            return max(a, b);
        } else {
            return f.apply(a).equals(CustomerImpact.FAVORS) ? a : b;
        }
    }

    public sealed interface PartialOrder<A> permits Winner, Tie {}
//    sealed interface TotalOrder<A> permits Winner {}

    public record Winner<A>(A value) implements PartialOrder<A> {}
    public record Tie<A>(A opt1, A opt2) implements PartialOrder<A> {}


//    @FunctionalInterface
//    public interface TieBreaker2<A> extends BinaryOperator<A> {
//        A apply(A x, A y);
//
//        static <T, B extends Comparable<B>> TieBreaker2<Winner<T>> choosing(Function<T, B> f) {
////            return (c1, c2) -> f.apply(c1).compareTo(f.apply(c2)) >= 0
////                    ? new Winner<>(c1)
////                    : new Winner<>(c2);
//            return (c1, c2) -> {
//                boolean res = f.apply(c1).compareTo(f.apply(c2)) >= 0;
//            }
//        }
//    }





    // THIS IS NOT CLOSED YOU LITERAL RETARD
    @FunctionalInterface
    public interface TieBreaker<A> {
        Winner<A> choose(A x, A y);
    }

    @FunctionalInterface
    public interface Choose<A> {
        PartialOrder<A> choose(A x, A y);


        public static <T> Choose<T> choosing(Predicate<T> p) {
            return (c1, c2) -> p.test(c1) == p.test(c2)
                    ? new Tie<>(c1, c2)
                    : new Winner<>(p.test(c1) ? c1 : c2);
        }

        public static <T, B extends Comparable<B>> TieBreaker<T> choosing(Function<T, B> f) {
            return (c1, c2) -> f.apply(c1).compareTo(f.apply(c2)) >= 0
                    ? new Winner<>(c1)
                    : new Winner<>(c2);
        }

        default Choose<A> andThen(Predicate<A> p) {
            return (c1 ,c2) -> {
                return switch (this.choose(c1, c2)) {
                    case Tie(A opt1, A opt2) -> choosing(p).choose(opt1, opt2);
                    case Winner(var x) -> new Winner<>(x);
                };
            };
        }

        default <B extends Comparable<B>> Choose<A> andThen(Function<A,B> f) {
            return (c1 ,c2) -> {
                return switch (this.choose(c1, c2)) {
                    case Tie(A opt1, A opt2) -> choosing(f).choose(opt1, opt2);
                    case Winner(var x) -> new Winner<>(x);
                };
            };
        }

//        default <B extends Comparable<B>> TieBreaker<A> andFinally(Function<A,B> f) {
//            return (c1 ,c2) -> {
//                return switch (this.choose(c1, c2)) {
//                    case Tie(A opt1, A opt2) -> choosing(f).choose(opt1, opt2);
//                    case Winner(var x) -> new Winner<>(x);
//                };
//            };
//        }
    }





    static <A> PartialOrder<Optional<A>> comp(Optional<A> x, Optional<A> y) {
        return x.isPresent() && y.isPresent()
                ? new Tie<>(x, y)
                : new Winner<>(x.isPresent() ? x : y);
    }





//    static <A,B> PartialOrder<A> compImpact(A x, A y, Function<A,B> keyGetter) {
//        return keyGetter.apply(x).equals(keyGetter.apply(y))
//                ? new Tie<>(x, y)
//                : new Winner<>(x); // favors (how?)
//    }
//
    static <A,B extends Comparable<B>> Winner<A> compImpact(A x, A y, Function<A,B> keyGetter) {
        return new Winner<>(keyGetter.apply(x).compareTo(keyGetter.apply(y)) > 0 ? x : y);
    }

    /*

    (a,b) -> Winner, Tie

    (a,b) -> Winner, Tie

    compose(g) {
        return (s1, s2) -> {
            switch (f.apply(s1, s2)) {
                case Tie(var x, var y) -> g.apply(x, y);
                case Winner(var value) -> new Winner(var value)
        }
    }

    compare(equality, isPresent) -> Winner<Optional<A>>
        .andThen(equality, customerImpact)
        .andThen(name)
        .choose(x, y)

     */




//    @FunctionalInterface
//    interface Comp<A> {
//        PartialOrder<A> comp(A a, A b);
//
//        default <A,B> Comp<B> comparing(Function<A, B> f) {
//            return (s1, s2) -> f.apply(s1).compareTo(f.apply(s2))
//        }
//    }


    /*
    (a,b) -> Either[Winner, Tie]
    (a,b) -> [a|b, (a,b)]

    Optional -> [Winner, Tie]
    Impact   -> [Winner, Tie]
    Name     -> Winner


     */

    static <A> A max(Comparator<A> comparator, A a, A b) {
        int result = IntStream.range(0, 100).reduce(0, Integer::sum);
        return comparator.compare(a, b) >= 0 ? a : b;
    }

    static class MyRecord<A, B> {

    }

    public record Tuple<A,B>(A left, B right){}

    public static <A> Set<Tuple<A,A>> product(Set<A> xs, Set<A> ys) {
        return xs.stream().flatMap(x -> ys.stream().map(y -> new Tuple<>(x, y))).collect(toSet());
    }

    static <A> List<A> add(List<A> left, List<A> right) {
        return Stream.concat(left.stream(), right.stream()).toList();
    }

    static <K,V> Map<K,List<V>> add(Map<K, List<V>> left, Map<K,List<V>> right) {
        HashMap<K, List<V>> combined = new HashMap<>(left);
        right.forEach((key, value) -> combined.merge(
                key,
                value,
                (list1, list2) -> add(list1, list2)));
        return combined;
    }

    static <K,V> Map<K,V> add_(Map<K, V> left, Map<K,V> right, BinaryOperator<V> op) {
        HashMap<K, V> combined = new HashMap<>(left);
        right.forEach((k, v) -> combined.merge(k, v, op));
        return combined;
    }

    static <K,V> Map<K,List<V>> addVariation2(Map<K, List<V>> left, Map<K,List<V>> right) {
        HashMap<K, List<V>> combined = new HashMap<>(left);
        right.forEach((k, v) -> combined.merge(k, v, Sketch::add));
        return combined;
    }

    static <K,V> Map<K,List<V>> add(List<Map<K, List<V>>> maps) {
        return maps.stream().reduce(Map.of(), Sketch::add);
    }

    static <A, B> B myReduce(List<A> xs, B identity, BiFunction<B, A, B> f) {
        B out = identity;
        for (A x : xs) {
            out = f.apply(out, x);
        }
        return out;
    }

    void aszdfadsf() {
        List<RawData> xs = List.of();
        Map<String, List<RawData>> result = myReduce(xs, Map.<String, List<RawData>>of(), (acc, val) -> {
            return Sketch.add(acc, Map.of(val.id(), List.of(val)));
        });
    }



    @Test
    void asdfasdfasdf() {
        List<Integer> rows = List.of(1, 1, 1, 2, 3, 2, 1, 3);
        Map<Integer, List<Integer>> dupesById = new HashMap<>();
        for (Integer row : rows) {
            dupesById.getOrDefault(row, new ArrayList<>()).add(row);
        }
        List<RawData> xs = List.of();
    }

    static <K,V> Map<K,List<V>> groupBy1(List<V> xs, Function<V,K> keyGetter) {
        Map<K, List<V>> groups = new HashMap<>();
        for (V x : xs) {
            groups.getOrDefault(keyGetter.apply(x), new ArrayList<>()).add(x);
        }
        return groups;
    }

    static <K,V> Map<K,List<V>> groupBy2(List<V> xs, Function<V,K> keyGetter) {
        return xs.stream()
                .map(x -> Map.of(keyGetter.apply(x), List.of(x)))
                .reduce(Map.of(), Sketch::add);
    }

    <K,T> Map<K, List<T>> groupByVariation1(List<T> items, Function<T, K> keyGetter) {
        Map<K, List<T>> groups = new HashMap<>();
        for (T item : items) {
            K key = keyGetter.apply(item);
            if (!groups.containsKey(key)) {
                groups.put(key, new ArrayList<>());
            }
            groups.get(key).add(item);
        }
        return groups;
    }


    // Viewing it as a binary operation on Lists
    <K,T> Map<K, List<T>> groupByVariation2(List<T> items, Function<T, K> keyGetter) {
        Map<K, List<T>> groups = new HashMap<>();
        for (T item : items) {
            K key = keyGetter.apply(item);
            List<T> list1 = groups.getOrDefault(key, new ArrayList<>());
            List<T> list2 = List.of(item);
            groups.put(key, add(list1, list2));
        }
        return groups;
    }

    <K,T> Map<K, List<T>> groupByVariation2_a(List<T> items, Function<T, K> keyGetter) {
        Map<K, List<T>> groups = new HashMap<>();
        for (T item : items) {
            K key = keyGetter.apply(item);
            List<T> list1 = groups.getOrDefault(key, new ArrayList<>());
            List<T> list2 = List.of(item);
            groups.putAll(Map.of(key, add(list1, list2)));
        }
        return groups;
    }

    // Viewing it as a binary operation on Maps
    <K,T> Map<K, List<T>> groupByVariation3(List<T> items, Function<T, K> keyGetter) {
        Map<K, List<T>> result = new HashMap<>();
        for (T item : items) {
            K key = keyGetter.apply(item);
            Map<K, List<T>> anotherMap = Map.of(key, List.of(item));
            result = add(result, anotherMap);
        }
        return result;
    }
    // Huh. That suddenly looks just like the other thing, which looks just like reduce.

    // A brief detour into reduce.
    // No need to explain. Everyone already gets it. What they need to see are the **properties**
    // Talk about reduce + Associativity + Identity here?
    // [[[[[[[[[[[[[Identity + monoid somehwere here???????]]]]]]]]]]]]]

    // Refactoring everything to use Reduce
    // Very respectable modern java. However, there's more we can do.
    // Our noticing is just getting started.
    // Looking at complex things in terms of primitive operatins can reveal
    // new ways of looking at other parts of you program.

    // [PICTURE]
    // What we're doing.
    // Then back to the progression as usual, then BOOM solved.

    // Interestingly, that suddenly looks EXACTLY like reduce
    // Once we know how to add two things, we know how to add ALL things
    <K,T> Map<K, List<T>> groupByVariation4(List<T> items, Function<T, K> keyGetter) {
        return items.stream()
            .map(x -> Map.of(keyGetter.apply(x), List.of(x)))
            .reduce(Map.of(), Sketch::add);
    }

    // Some interesting patterns are emerging.
    // We're really just talking about binary operations
    <K,T> Map<K, List<T>> groupByVariation5(List<T> items, Function<T, K> keyGetter) {
        return items.stream()
                .map(x -> Map.of(keyGetter.apply(x), List.of(x)))// lOOK!
                .reduce(Map.of(), (a,b) -> add_(a, b, Sketch::add));  // LOOK!
    }

    <K,T, U> Map<K, U> groupByVariation6(List<T> items,
                                               Function<T, K> keyGetter,
                                               Function<T, U> valueMapper,
                                               BinaryOperator<U> binaryOperator) {
        return items.stream()
                .map(x -> Map.of(keyGetter.apply(x), valueMapper.apply(x)))// lOOK!
                .reduce(Map.of(), (a,b) -> add_(a, b, binaryOperator));  // LOOK!
    }

    Collection<RawData> implVariation1(List<RawData> items) {
        return items.stream()
                .map(x -> Map.of(x.id(), x))
                .reduce(Map.of(), (a,b) -> add_(a, b, Sketch::add))
                .values();
    }

    Collection<RawData> implVariation2(List<RawData> items) {
        return items.stream()
                .collect(toMap(RawData::id, Function.identity(), Sketch::add))
                .values();
    }

    static RawData add(RawData x, RawData y) {
        return x;
    }


    record Foo22(String id, Integer bar) implements Comparable<Foo22> {

        @Override
        public int compareTo(Foo22 o) {
            return 0;
        }
    }

    @Test
    void floatingPointAddition() {
        double x = 0.02;
        double y = 0.03;
        System.out.println(2.12 + 5.3);
        System.out.println(Stream.concat(Stream.of(1,2), Stream.of(3)).toList());
        Integer xx = 123;
        xx.compareTo(1234);
//        "".compareTo("sdf")
        Double xasdf = 1.2;
        List.of(1,2,3).stream().reduce(0, Integer::sum);
        assertEquals(BigDecimal.ONE.add(BigDecimal.ONE), BigDecimal.TWO);

    }


    @Test
    void booleanLeftAndRightIdentityUsingAND() {
        boolean id = true;
        List.of(true,false).forEach(bool -> {
            assertEquals(id && bool, bool, "left identity");
            assertEquals(bool && id, bool, "right identity");
        });
    }

    @Test
    void booleanLeftAndRightIdentityUsingOR() {
        boolean id = false;
        List.of(true,false).forEach(bool -> {
            assertEquals(id || bool, bool, "left identity");
            assertEquals(bool || id, bool, "right identity");
        });
    }



    static <A> List<List<A>> permutations(List<A> xs) {
        if (xs.size() == 2) {
            return List.of(
                new ArrayList<>(List.of(xs.getFirst(), xs.getLast())),
                new ArrayList<>(List.of(xs.getLast(), xs.getFirst()))
            );
        } else {
            List<List<A>> out = new ArrayList<>();
            for (int i = 0; i < xs.size(); i++) {
                for (List<A> rest : permutations(except(xs, xs.get(i)))) {
                    rest.add(0, xs.get(i));
                    out.add(rest);
                }
            }
            return out;
        }
    }
    static <A> List<A> except(List<A> xs, A elem) {
        ArrayList<A> copy = new ArrayList<>(xs);
        copy.remove(elem);
        return copy;
    }

    static <A> List<List<A>> perms(List<A> xs) {
        if (xs.size() == 1) {
            List<List<A>> asdf = new ArrayList<>();
            asdf.add(xs);
            return asdf;
        } else {
            List<List<A>> out = new ArrayList<>();
            for (int i = 0; i < xs.size(); i++) {
                for (List<A> rest : perms(except(xs, xs.get(i)))) {
                    rest.addFirst(xs.get(i));
                    out.add(rest);
                }
            }
            return out;
        }
    }

    static boolean IS_SPECIAL_AGREEMENT = false;
    /*
    Reasons not to bill:
    * PromotionalPeriod
    * Bankruptcy
    * Write Off
    * Dispute

    Reasons to Bill
    * Bad Payer
    * Dispute

     */

    sealed interface Option<A extends Comparable<A>> permits Option.Nothing, Option.Some {
        record Some<A extends Comparable<A>>(A value) implements Option<A>{}
        record Nothing<A extends Comparable<A>>() implements Option<A>{}
    }



static List<RawData> cleanDuplicates(List<RawData> rows) {
    Map<String, List<RawData>> dupesById = new HashMap<>();
    for (RawData row : rows) {
        String key = row.id();
        if (!dupesById.containsKey(key)) {
            dupesById.put(key, new ArrayList<>());
        }
        dupesById.get(key).add(row);
    }

    List<RawData> cleaned = new ArrayList<>();
    for (List<RawData> duplicates : dupesById.values()) {
        RawData merged = duplicates.getFirst();
        for (RawData other : duplicates.subList(1, duplicates.size())) {
            merged = add(merged, other);
        }
        cleaned.add(merged);
    }
    return cleaned;
}



    static <A> void swap(List<A> xs, int i , int j) {
        A temp = xs.get(j);
        xs.set(j, xs.get(i));
        xs.set(i, temp);
    }

    static int fact(int x) {
        int out = 1;
        for (int i = x; i>1; i--) {
            out = out * i;
        }
        return out;
    }


    static String concat(String a, String b) {
        return a.concat(b);
    }

    static String max(String a, String b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    static RootCause max(RootCause a, RootCause b) {
        return a.name().compareTo(b.name()) >= 0 ? a : b;
    }

    public static void main(String... args) {



        PartialOrder<Optional<CaseStage2>> result = Choose.choosing((Optional<CaseStage2> t) -> t.isPresent())
                .choose(Optional.empty(), Optional.empty());

        System.out.println(result);



        System.out.println(Choose.choosing((CaseStage2 t) -> impact(t).equals(CustomerImpact.FAVORS))
                        .andThen(CaseStage2::name)
                        .choose(CaseStage2.BAR, CaseStage2.FOO)
        );

        Choose<CaseStage2> ffff = Choose.choosing((CaseStage2 t) -> impact(t).equals(CustomerImpact.FAVORS))
                .andThen(CaseStage2::name);



        System.out.println(Choose.choosing((Optional<CaseStage2> x) -> x.isPresent()).choose(Optional.empty(), Optional.empty()));


//        Comparator<Optional<CaseStage2>> totalOrder = comparing((Optional<CaseStage2> t) -> t.isPresent())
//                .thenComparing(Optional::get,
//                        comparing((CaseStage2 x) -> impact(x))
//                                .thenComparing(CaseStage2::name));
//
////        comp().thenComp()
//
//
////                .compare(Optional.of(CaseStage2.FOO), Optional.of(CaseStage2.BAR));
////        Optional<String> a = Optional.empty();
////        Optional<String> b = Optional.empty();
//
//        System.out.println(max(totalOrder, Optional.empty(), Optional.empty()));

//        System.out.println(max("RootCause.DISPUTE", "RootCause.TAX"));
//        System.out.println(max(RootCause.DISPUTE, RootCause.TAX));

//        boolean a = true;
//        boolean b = false;
//        boolean id = true;
//
//        System.out.println(Stream.of("A", "B", "C", "D").reduce("", String::concat));
//        System.out.println(Stream.of("A", "B", "C", "D").reduce("", Sketch::max));
//        System.out.println(max("", "a"));
//        System.out.println(max("a", ""));
//
//
//        List.of(a,b).forEach(bool -> {
//            System.out.println((id && bool) == bool);
//            System.out.println((bool && id) == bool);
//        });

        /*
        [A, B, C]
        [A, C, B]
        [B, A, C]
        [B, C, A]
        [C, B, A]
        [C, A, B]



         */


//        List<String> letters = List.of("A","B","C","D","E");
//        permutations(letters).stream().forEach(System.out::println);
//
////        permutations(List.of("A","B", "C")).stream().forEach(System.out::println);
//
////        System.out.println("expected:")
//
//        System.out.println("expected: " + fact(letters.size()) + " Actual: " + permutations(letters).size());
//        System.out.println("expected: " + fact(letters.size()) + " Actual: " + perms(letters).size());
//        System.out.println("expected: " + (4*3*2*1) + " Actual: " + fact(4));
//        System.out.println(new HashSet<>( permutations(List.of("A","B","C", "D"))));
//
//        System.out.println("Expected:" + (4*3*2*1));

//        System.out.println("Boolean Right identity:" + (b && id == b));
//        System.out.println("Boolean left identity:" + (id && a == a));




//        Optional<String> c = merge(a, b);

//        System.out.println(merge3(Optional.of("A"), Optional.of("B"), String::concat));
//        System.out.println(merge3(Optional.of("A"), Optional.empty(), String::concat));
//        System.out.println(merge3(Optional.empty(), Optional.of("B"), String::concat));
//        System.out.println(merge3(Optional.empty(), Optional.empty(), String::concat));
        List<Data> xs = List.of(
                new Data("1", Optional.of("foo"), Optional.of(1)),
                new Data("1", Optional.of("foo"), Optional.of(2)),
                new Data("2", Optional.of("bar"), Optional.of(1))
        );

        List<Data> canonicalExample = List.copyOf(xs.stream()
                .collect(toMap(Data::id, Function.identity(), Data::mappend))
                .values());


        List<MessyData> ys = List.of(
                new MessyData("1", Optional.of(CaseStage2.FOO), Optional.of(RootCause.DISPUTE), Optional.of(false)),
                new MessyData("1", Optional.of(CaseStage2.FOO), Optional.of(RootCause.DISPUTE), Optional.of(false))
        );

        List<MessyData> canonicalExample2 = List.copyOf(ys.stream()
                .collect(toMap(MessyData::id, Function.identity(), MessyData::merge))
                .values());



//        xs.stream()
//                .collect(reducing(List.of(), ))

//        List<List<Data>> asdf = xs.stream().collect(groupingBy(x -> x.id()))
//                .values().stream()
//                .toList();

//
//
//        List<Data> resultszz = xs.stream().map(DataRow::new).reduce(DataRow.empty(), DataRow::mappend).values();
//
//
//
//
//        Map<String, Squishy> results = xs.stream().collect(
//                groupingBy(Data::id, reducing(Squishy.empty(), Data::row, Sketch::combine)));
//
//        List<Foo> result1x = results.entrySet().stream()
//                .map(x -> new Foo(x.getKey(), x.getValue())).toList();
////        System.out.println(results);
//
//        List<Map<String, Object>> ys = List.of(
//                Map.of("id", 1, "foo", 1, "bar", 2),
//                Map.of("id", 1, "foo", null, "bar", 2),
//                Map.of("id", 2, "foo", 1, "bar", null)
//        );
//
//        Map<String, Map<String, Object>> results2 = ys.stream().collect(groupingBy(x -> (String) x.get("id"), reducing(Map.of(), Sketch::add)));


    }

    // The merge operation is only defined if one or both
    // are null.
    //
    // A + B is undefined for the merge operation
    //
    //
}
