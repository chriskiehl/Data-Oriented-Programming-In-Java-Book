package dop.chapter09;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Utils {
    @SafeVarargs
    public static <K,V> Map<K,V> union(Map<K,V> xs, Map<K,V>... ys) {
        HashMap<K, V> copy = new HashMap<>(xs);
        for (Map<K,V> y : ys) {
            copy.putAll(y);
        }
        return copy;
    }

    public static <K,V> Map<K,V> intersectBy(Map<K,V> xs, Map<K,V> ys, BinaryOperator<V> op) {
        Map<K,V> output = new HashMap<>();
        for (Map.Entry<K,V> x: xs.entrySet()) {
            if (ys.containsKey(x.getKey())) {
                output.put(x.getKey(), op.apply(x.getValue(), ys.get(x.getKey())));
            }
        }
        return output;
    }

    public static <K,V> Map<K,V> except(Map<K,V> xs, Map<K,V> ys) {
        return xs.entrySet().stream()
                .filter(x -> ys.containsKey(x.getKey()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K,V> Map<K,V> select(Map<K,V> xs, Predicate<V> pred) {
        return xs.entrySet().stream()
                .filter(x -> pred.test(x.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static record Split<K,V>(Map<K,V> matched, Map<K,V> notMatched){}

    public static <K,V> Split<K,V> split(Map<K,V> xs, Predicate<V> pred) {
        Map<K,V> matched = new HashMap<>();
        Map<K,V> noMatched = new HashMap<>();
        xs.forEach((key, value) -> {
            if (pred.test(value)) {
                matched.put(key, value);
            } else {
                noMatched.put(key, value);
            }
        });
        return new Split<>(matched, noMatched);
    }

    public static <Partition, K, V> Map<Partition, Map<K,V>> partition(Map<K,V> xs, Function<V, Partition> mapper) {
        Map<Partition, Map<K,V>> partitions = new HashMap<>();
        xs.forEach((key, value) -> {
            Partition part = mapper.apply(value);
            partitions.getOrDefault(part, new HashMap<>()).put(key, value);
        });
        return partitions;
    }

    public static <K,V, V2> Map<K,V2> mapValues(Map<K,V> xs, Function<V, V2> mapper) {
        Map<K,V> output = new HashMap<>();
        return xs.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, (x) -> mapper.apply(x.getValue())));
    }

    public static <K,V> Map<K, V> indexBy(List<V> xs, Function<V, K> keyGetter) {
        return xs.stream().collect(toMap(keyGetter, Function.identity()));
    }

    public static <K,V> Map<K, V> indexBy(Stream<V> xs, Function<V, K> keyGetter) {
        return xs.collect(toMap(keyGetter, Function.identity()));
    }

}
