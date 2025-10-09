package dop.chapter07;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Associativity {


    static <B> B reduce(B initial, BinaryOperator<B> combiner, List<B> items) {
        if (items.isEmpty()) {
            return initial;
        } else {
            B item = items.getFirst();
            B rest = reduce(initial, combiner, items.subList(1, items.size()));
            return combiner.apply(item, rest);
        }
    }


    @Test
    void whathappens() {
//        List<String> xs = List.of("Hello", "World", "!", " How", "are", "ya?");
//        System.out.println(reduce("", String::concat, xs));
//        xs.stream().skip(1).parallel().reduce()
        List<Double> xs = IntStream.range(0, 10).boxed().map(x -> (double)x).collect(Collectors.toList());
        Collections.shuffle(xs);
        ArrayList<Double> ys = new ArrayList<>();
        double out = xs.getFirst();
        ys.add(xs.getFirst());
        for (Double v : xs.subList(1, xs.size())) {
            ys.add(v);
        }
        System.out.println(xs.stream().parallel().reduce(0.0, (a,b) -> a - b));
        System.out.println(xs.stream().reduce(0.0, (a,b) -> a - b));
//        System.out.println(xs);
//        System.out.println(ys);
//        for (int i =0; i < 10;i++) {
//            List<Integer> ys = List.copyOf(xs);
//            List<Integer> zs = List.copyOf(xs);
//            System.out.println(zs.stream().parallel().reduce(0, (a,b) -> a - b));
//        }
    }
}
