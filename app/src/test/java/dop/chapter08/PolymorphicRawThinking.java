package dop.chapter08;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static dop.chapter08.PolymorphicRawThinking.Fooo.SalesChannel.External;
import static dop.chapter08.PolymorphicRawThinking.Fooo.Sector.Bar;
import static dop.chapter08.PolymorphicRawThinking.Fooo.Sector.Baz;


public class PolymorphicRawThinking {


    record Attr<A extends Fooo>(String attrName, Function<Account, A> f) {}
    record Attr2<A extends Fooo>(String attrName, Class<A> cls){}

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    sealed interface Fooo {
        enum Sector implements Fooo{
            Foo,
            Bar,
            Baz
        }

        enum SalesChannel implements Fooo{
            Internal,
            External,
        }
    }

    record Account(
            Fooo.SalesChannel channel,
            Fooo.Sector sector
    ){}

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Term.Eq2.class, name = "EQ"),
    })
    sealed interface Term {
//        record Eq<A>(Class<A> xs, A value) implements Term {}
        record Eq2<A extends Fooo>(Attr<A> field, A value) implements Term {}
//        record Eq3<A>(TheFields<A> field, A value) implements Term {}
    }

    static Attr<Fooo.SalesChannel> $channel = new Attr<>("channel", Account::channel);
    static Attr<Fooo.Sector> $sector = new Attr<>("sector", Account::sector);

    static boolean proc(Account account, Term term) {
        return switch (term) {
            case Term.Eq2(var x, var y) -> x.f.apply(account).equals(y);
        };
    };


    @Test
    void asdfasdfasd() throws JsonProcessingException {
//        System.out.println(foo(new Term.Eq<>(Sector.class, Sector.Foo)));
//        System.out.println(foo(new Term.Eq2<>($channel, Internal)));
        System.out.println(proc(new Account(External, Bar), new Term.Eq2<>($sector, Baz)));
        System.out.println(proc(new Account(External, Bar), new Term.Eq2<>($channel, External)));
//        System.out.println(foo(new Term.Eq3<>(new $Sector(), Sector.Bar)));

        ObjectMapper mapper = new ObjectMapper();
        Term expr = new Term.Eq2<>($channel, External);
        String json = mapper.writeValueAsString(expr);
        System.out.println(json);
        Term deserialized = mapper.readValue(json, Term.class);
    }


    static boolean proc2(Account account, Term2 term) {
        return switch (term) {
            case Term2.Eq2(var x, var y) -> get(account, x).equals(y);
        };
    };

    static <A extends Fooo> A get(Account account, Attr2<A> attr) {
        return switch (attr.attrName()) {
            case "channel" -> attr.cls.cast(account.channel());
            case "sector" -> attr.cls.cast(account.sector());
            default -> throw new IllegalArgumentException("ASDSD");
        };
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Term2.Eq2.class, name = "EQ"),
    })
    sealed interface Term2 {
        //        record Eq<A>(Class<A> xs, A value) implements Term {}
        record Eq2<A extends Fooo>(Attr2<A> field, A value) implements Term2 {}
//        record Eq3<A>(TheFields<A> field, A value) implements Term {}
    }

    static Attr2<Fooo.SalesChannel> $channel2 = new Attr2<>("channel", Fooo.SalesChannel.class);
    static Attr2<Fooo.Sector> $sector2 = new Attr2<>("sector", Fooo.Sector.class);

    @Test
    void asdfasdfasdasdfasdf() throws JsonProcessingException {
        System.out.println(proc2(new Account(External, Bar), new Term2.Eq2<>($sector2, Baz)));
        System.out.println(proc2(new Account(External, Bar), new Term2.Eq2<>($channel2, External)));
//        System.out.println(foo(new Term.Eq3<>(new $Sector(), Sector.Bar)));

        ObjectMapper mapper = new ObjectMapper();
        Term2 expr = new Term2.Eq2<>($channel2, External);
        String json = mapper.writeValueAsString(expr);
        System.out.println(json);
        Term2 deserialized = mapper.readValue(json, Term2.class);
        System.out.println(deserialized);
    }

}
