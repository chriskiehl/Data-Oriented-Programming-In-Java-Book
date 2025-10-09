package dop.chapter07;

import dop.chapter07.Finalizing.Policy;
import org.checkerframework.checker.units.qual.A;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static dop.chapter07.Proving.product2;
import static dop.chapter07.Starting.add;
import static java.lang.String.format;

@FunctionalInterface
interface Function3<A,B,C, D> {
    D apply(A a, B b, C c);
}

public class PropertiesHold {



    public static void main(String... args) {
//        List<List<Policy>> allPolicies = Proving.permutations(Policy.values());
        List<Policy> policies = List.of(Policy.values());
        List<Boolean> res = product2(policies, policies, policies, (a, b, c) ->
                add(a, add(b, c)).equals(add(add(a, b), c))
        );
        System.out.println(res.stream().allMatch(x -> true));

        System.out.println(product2(policies, policies, (a,b) -> add(a, b).equals(add(b, a))).stream().allMatch(x -> true));



    }
}
