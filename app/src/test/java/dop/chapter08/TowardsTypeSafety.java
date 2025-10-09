package dop.chapter08;

import org.junit.jupiter.api.Test;

public class TowardsTypeSafety {

    enum Attribute {
        Foo,
        Bar
    }

    sealed interface Rule {
        record Eq<A>(Attribute attribute, A value) implements Rule {}
    }

    @Test
    void asdfasdfasdf() {
        new Rule.Eq<>(Attribute.Foo, 10.123);
        new Rule.Eq<>(Attribute.Foo, "Hello world");
    }
}
