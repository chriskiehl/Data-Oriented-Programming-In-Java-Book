package dop.chapter07;

import dop.chapter07.Finalizing.AuditFinding;
import dop.chapter07.Finalizing.Policy;
import dop.chapter07.Finalizing.RawData;
import dop.chapter07.Starting.Semigroup;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static dop.chapter07.Finalizing.AuditFinding.*;
import static dop.chapter07.Finalizing.policyImpact;
import static java.util.Comparator.comparing;
import static java.util.function.BinaryOperator.maxBy;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Proving {

    static <A> List<List<A>> permutations(A[] xs) {
        return permutations(List.of(xs));
    }

    static <A,B,C> List<C> product2(List<A> xs, List<B> ys, BiFunction<A, B, C> f) {
        return xs.stream().flatMap(x -> ys.stream().map(y -> f.apply(x, y))).toList();
    }

    static <A,B,C,D> List<D> product2(List<A> xs, List<B> ys, List<C> zs, Function3<A, B, C, D> f) {
        return xs.stream().flatMap(
                x -> ys.stream().flatMap(
                        y -> zs.stream().map(
                                z -> f.apply(x, y, z)))).toList();
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

    static <A> Optional<A> empty(A a) {
        return Optional.empty();
    }

    static <A> List<Optional<A>> www(Collection<A> xs) {
        return Stream.concat(
                xs.stream().map(Optional::of),
                Stream.of(Optional.<A>empty())
        ).toList();
    }

    static <K, V> Map<K,V> add(Map<K, V> left, Map<K,V> right, BinaryOperator<V> f) {
        return Stream.concat(
                left.entrySet().stream(),
                right.entrySet().stream()
        ).collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, f));
    }

    static <K, V extends Semigroup<V>> Map<K,V> add2(Map<K, V> left, Map<K,V> right) {
        return Stream.concat(
                left.entrySet().stream(),
                right.entrySet().stream()
        ).collect(toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue, Function::apply));
    }

    static void ordering() {
        var allPolicies = permutations(List.of(Policy.values()));

//        for (List<Policy> policies : allPolicies) {
//            System.out.println(policies.stream().sorted(Comparator.comparing(Finalizing::policyImpact)).toList());
//        }
    }

    static void ordering2() {
        var allPolicies = permutations(www(List.of(Policy.values())));

//        for (List<Optional<Policy>> policies : allPolicies) {
//            System.out.println(policies.stream().sorted(compareOpt(comparing(Finalizing::policyImpact).thenComparing(Policy::name))).toList());
//        }
    }

    static void ordering3() {
        var allPolicies = permutations(www(List.of(Policy.values())));

//        List<Optional<Policy>> xs = List.of(Optional.empty(), Optional.of(Policy.MANUAL_REVIEW), Optional.of(Policy.STRICT), Optional.of(Policy.IMMEDIATE), Optional.of(Policy.FLEXIBLE), Optional.of(Policy.GRACE_PERIOD));
//        xs.stream().reduce(BinaryOperator.maxBy(compareOpt(comparing(Finalizing::policyImpact).thenComparing(Policy::name))));
        for (List<Optional<Policy>> policies : allPolicies) {
//            System.out.println(policies);
//            System.out.println(policies.stream().reduce(BinaryOperator.maxBy(compareOpt(comparing(Finalizing::policyImpact).thenComparing(Policy::name)))));
//            System.out.println("...");
        }
    }

    static void hmm() {
        Map<String, RawData> a = Map.of("Row1", new RawData("row", Optional.empty(), Optional.empty(), Optional.empty()));
        Map<String, RawData> b = Map.of("Row1", new RawData("row", Optional.empty(), Optional.empty(), Optional.empty()));
        Map<String, RawData> c = Map.of("Row1", new RawData("row", Optional.empty(), Optional.empty(), Optional.empty()));

    }

    static void monoticity() {
        //   a1                 a2                  b1                          b2
        // [Optional.empty, Optional[FLEXIBLE], Optional[GRACE_PERIOD], Optional[MANUAL_REVIEW], Optional[IMMEDIATE]
        // a1 <= b1  AND a2 <= b2
        // THEN
        // a1 x a2  = FLEXIBLE
        // b1 x b2  = MANUAL_REVIEW
        // if a1 favors the customer more than b1
        // and a2 favors the customer bore than b2
        // then (a1 x a2) should favor the customer more than (b1 x b2)
    }


    @SafeVarargs
    static <A> List<Optional<A>> everyPossibleValue(A... items) {
        return Stream.concat(
                Stream.of(items).map(Optional::of),
                Stream.of(Optional.<A>empty())
        ).toList();
    }

    @Test
    void aamfasmmath() {
//        for (int i = Integer.MIN_VALUE; i<Integer.MAX_VALUE; i++) {
//            assertEquals(i + i, 2 * i);
//        }

        int i = Integer.MAX_VALUE - 10;
        System.out.println("" + (i + i) + ", " + (2*i));

    }

    static List<RawData> everyPossibleState() {
        List<RawData> output = new ArrayList<>();
        for (var policy : everyPossibleValue(Policy.values())) {
            for (var finding : everyPossibleValue(AuditFinding.values())) {
                for (var premium : everyPossibleValue(true, false)) {
                    output.add(new RawData(
                            "FixedCustomerId",
                            policy,
                            finding,
                            premium)
                    );
                }
            }
        }
        return output;
    }

    @Test
    void asdfasdfasdafsdf() {
        long s = System.currentTimeMillis();
//        List<List<RawData>> result = everyPossibleState().stream().flatMap(x -> everyPossibleState().stream().map(y -> List.of(x, y))).toList();
        List<List<RawData>> result = new ArrayList<>();
        for (RawData x : everyPossibleState()) {
            for (RawData y : everyPossibleState()) {
                for (RawData z : everyPossibleState()) {
                    result.add(List.of(x,y,z));
                }
            }
        }
        int x = result.size();
        System.out.println(System.currentTimeMillis() - s);
        System.out.println(x);
    }

    @Test
    void transitivity() {
        for (RawData a : everyPossibleState()) {
            for (RawData b : everyPossibleState()) {
                if (a.compareTo(b) <= 0 && b.compareTo(a) <= 0) {
                    assertEquals(a, b);
                }
            }
        }
    }

    record NonEmptyList<A>(List<A> items) {
        NonEmptyList {
            if (items.isEmpty()) {
                throw new IllegalArgumentException("Ah!");
            }
        }
    }
    RawData addEverything(NonEmptyList<RawData> duplicates) {
        List<RawData> rows = duplicates.items();
        return rows.stream()
                .skip(1)
                .reduce(rows.getFirst(), RawData::merge);
    }

    static RawData add(RawData x, RawData y) {
        return x;
    }
    static RawData empty() {
        return new RawData("", Optional.empty(),Optional.empty(),Optional.empty());
    }

    <K,T> Map<K, List<T>> imperativeGroupBy(List<T> items, Function<T, K> keyGetter) {
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

    <T, K> Map<K, List<T>> imperativeGroupByConcatLists(List<T> items, Function<T, K> keyGetter) {
        Map<K, List<T>> groups = new HashMap<>();
        for (T item : items) {
            K key = keyGetter.apply(item);
            groups.merge(key, List.of(item), (a,b) -> add(a,b));
        }
        return groups;
    }

    <A,B> Map<B, List<A>> binaryGroupBy2(List<A> items, Function<A, B> keyGetter) {
        Map<B, List<A>> groups = new HashMap<>();
        for (A item : items) {
            groups = add(groups, Map.of(keyGetter.apply(item), List.of(item)));
        }
        return groups;
    }

    <A,B> Map<B, List<A>> binaryGroupBy3(List<A> items, Function<A, B> keyGetter) {
        return items.stream()
                .map(item -> Map.of(keyGetter.apply(item), List.of(item)))
                .reduce(Map.of(), Proving::add);
    }

    static <A> Optional<A> add(
            BinaryOperator<A> operator,
            Optional<A> option1,
            Optional<A> option2) {
        if (option1.isPresent() && option2.isPresent()) {
            return Optional.of(operator.apply(option1.get(), option1.get()));
        } else {
            return option1.isPresent() ? option1 : option2;
        }
    }

    static <A> Optional<A> add2(
            BinaryOperator<A> operator,
            Optional<A> option1,
            Optional<A> option2) {
        return option1.flatMap(x -> option2.map(y -> operator.apply(x, y)))
                .or(() -> option1.isPresent() ? option1 : option2);
    }

    static <A> Optional<A> add3(
            BinaryOperator<A> operator,
            Optional<A> option1,
            Optional<A> option2) {
        return Option1.lift(operator).apply(option1, option2)
                .or(() -> option1.isPresent() ? option1 : option2);
    }

    static <A> BinaryOperator<Optional<A>> withOptional(BinaryOperator<A> operator) {
        return (opt1, opt2) -> Option1.lift(operator).apply(opt1, opt2)
                .or(() -> opt1.isPresent() ? opt1 : opt2);
    }

    <A,B> Map<B, List<A>> streamGroupBy(List<A> items, Function<A, B> keyGetter) {
        return items.stream().collect(toUnmodifiableMap(keyGetter, List::of, Proving::add));
    }

    static <K,V> Map<K,List<V>> add(Map<K,List<V>> xs, Map<K,List<V>> ys) {
        Map<K, List<V>> merged = new HashMap<>(xs);
        for (Map.Entry<K,List<V>> entry : ys.entrySet()) {
            merged.merge(entry.getKey(), entry.getValue(), (a,b) -> add(a,b));
        }
        return merged;
    }

    static <A> List<A> add(List<A> list1, List<A> list2) {
        return Stream.concat(list1.stream(), list2.stream()).toList();
    }

    <A,B> Map<B, List<A>> binaryGroupBy(List<A> items, Function<A, B> keyGetter) {
        Map<B, List<A>> groups = new HashMap<>();
        for (A item : items) {
            B key = keyGetter.apply(item);
            groups.merge(key, List.of(item), Proving::add);
        }
        return groups;
    }



    @Test
    public void testGroupBy() {
        List<AuditFinding> expected = List.of(BILLING_ERROR, INACCURATE, NO_ISSUE, OUT_OF_COMPLIANCE);
        permutations(AuditFinding.values())
                .stream()
                .map(x -> x.stream().sorted().toList())
                .allMatch(sorted -> sorted.equals(expected));
        List<Integer> xs = List.of(1, 2, 1, 1, 3, 4, 5, 4, 5, 6);
        System.out.println(imperativeGroupBy(xs, Function.identity()));
        System.out.println(binaryGroupBy(xs, Function.identity()));
        System.out.println(binaryGroupBy2(xs, Function.identity()));
        System.out.println(binaryGroupBy3(xs, Function.identity()));
        System.out.println(streamGroupBy(xs, Function.identity()));
    }



    static Policy originalAdd(Policy x, Policy y) {
        if (policyImpact(x).equals(policyImpact(y))) {
            return x.name().compareTo(y.name()) >= 0 ? x : y;
        } else {
            return policyImpact(x).equals(Finalizing.CustomerImpact.FAVORS) ? x : y;
        }
    }

    static BinaryOperator<Policy> choosePolicy = maxBy(comparing(Finalizing::policyImpact).thenComparing(Policy::name));
    static BinaryOperator<AuditFinding> chooseFindings = maxBy(comparing(Finalizing::findingsImpact));
    static BinaryOperator<Boolean> choosePremium = maxBy(Boolean::compareTo);

//    static RawData adddd(RawData x, RawData y) {
//        var moreFavorable = maxBy(comparing(Finalizing::policyImpact).thenComparing(Policy::name))
//        return new RawData(
//            x.id(),
//        )
//    }

    @Test
    void testRefactoredIsTheSame() {
        for (Policy a : Policy.values()) {
            for (Policy b : Policy.values()) {
                assertEquals(originalAdd(a, b), choosePolicy.apply(a,b));
            }
        }
    }

    @Test
    void booleanSameAsDisjunction() {
        for (Boolean a : List.of(true, false)) {
            for (Boolean b : List.of(true, false)) {
                assertEquals(
                        Boolean.compare(a, b) > 0 ? a : b,
                        a || b
                );
            }
        }
    }

    static <T,K,U> Map<K,U> _toMap(List<T> items, Function<T,K> k, Function<T,U> v, BinaryOperator<U> f) {
        return items.stream().collect(toMap(k,v,f));
    }

    static <A> A myReduce(List<A> items, A identity, BinaryOperator<A> f) {
        return _toMap(items, (x) -> "FixedID", Function.identity(), f)
                .values().stream()
                .findFirst().orElse(identity);
    }

    public static void main(String... args) {
//        ordering3();
        var allPolicies = permutations(www(List.of(Policy.values())));
        var allFindings = permutations(www(List.of(AuditFinding.values())));
        var allFlags = permutations(www(List.of(true, false)));

//        product2(allPolicies, allFindings, allFlags, (p, f, g) ->
//                allPolicies.stream().flatMap(
//                        policies ->
//                ))

        List<RawData> output = new ArrayList<>();
        for (var policies : allPolicies) {
            for (var findings : allFindings) {
                for (var flags : allFlags) {
                    output.addAll(product2(policies, findings, flags, (p, f, g) -> new RawData("SomeId", p, f, g)));
                }
            }
        }
        Set<RawData> sss = new HashSet<>(output);


//        for (var policies : allPolicies) {
//            for (var p: policies) {
//                for (var findings : allFindings) {
//                    for (var finding : findings) {
//                        for (var flags : allFlags) {
//                            for (var flag : flags) {
//                                output.add(new RawData(
//                                        "someId",
//                                        p,
//                                        finding,
//                                        flag
//                                ));
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        // 600
//        System.out.println(allPolicies.size() + ", " + allPolicies.getFirst().size() + ", " + (allPolicies.size() * allPolicies.getFirst().size()));
//        System.out.println(allFindings.size() + ", " + allFindings.getFirst().size() + ", " + (allFindings.size() * allFindings.getFirst().size()));
//        System.out.println(allFlags.size() + ", " + allFlags.getFirst().size() + ", " + (allFlags.size() * allFlags.getFirst().size()));
        System.out.println(output.size());
        System.out.println(sss.size());
        System.out.println(Optional.of(1));
        System.out.println(Optional.empty());

        Set<Set<RawData>> xss = new HashSet<>();
        for (RawData a : sss) {
            for (RawData b : sss) {
                if (a.equals(b)) {
                    continue;
                }
                HashSet<RawData> pair = new HashSet<>();
                pair.add(a);
                pair.add(b);
                xss.add(pair);
            }
        }
        System.out.println("Unique Pairs: " + xss.size());
    }
}
