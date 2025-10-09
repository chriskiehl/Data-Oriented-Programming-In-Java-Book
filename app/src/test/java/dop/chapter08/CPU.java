package dop.chapter08;

import dop.chapter08.CPU.Term.Eq;
import dop.chapter08.CPU.Term.Nand;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class CPU {

    static Map<String, String> stuff = Map.of(
        "foo", "bar",
        "region", "usa"
    );

    sealed interface Term {
        record Eq(String field, String value) implements Term {}
        record Nand(Term a, Term b) implements Term {}
    }

    static Term Eq(String field, String value) {return new Term.Eq(field, value);}
    static Term Nand(Term a, Term b) {return new Term.Nand(a, b);}
    static Term Not(Term a) {
        return new Term.Nand(a, a);
    }

    static boolean interp(Term term) {
        return switch (term) {
            case Eq(var field, var value) -> stuff.get(field).equals(value);
            case Nand(var a, var b) -> !(interp(a) && interp(b));
        };
    }


    @Test
    void v1() {
        Term t = Not(Eq("foo", "baz"));
        System.out.println(interp(t));
    }

}
