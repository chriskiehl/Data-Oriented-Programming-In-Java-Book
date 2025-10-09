package dop.chapter08;

import static java.lang.String.format;

public class Sketch8 {
//
//    record Thing(
//            String bizUnit,
//            String region,
//            String geo,
//            String line,
//            String billingCode,
//            Boolean isPremium,
//            Double sales
//    ){}
//
//
//
////    sealed interface Fields {
////        sealed interface StrField {
////            record BizUnit() implements Fields, StrField {}
////            record Region() implements Fields, StrField {}
////            record Geo() implements Fields, StrField {}
////            record Line() implements Fields, StrField {}
////        }
////        sealed interface NumField {
////            record NotString() implements Fields, NumField {}
////        }
////    }
//    enum Attribute {
//        BizUnit,
//        Region,
//        Geo,
//        Line,
//        BillingCode,
//    }
//    enum NumField {
//        Sales
//    }
//
//    static String lookup(Thing thing, String field) {
//        return switch (field) {
//            case "geo" -> thing.geo();
//            case "region" -> thing.region();
//            default -> throw new IllegalArgumentException("Invalid Field Type!");
//        };
//    }
//
//    static String get(Thing thing, Attribute field) {
//        return switch (field) {
//            case Geo -> thing.geo();
//            case Attribute.Region -> thing.region();
//            case BizUnit -> thing.bizUnit();
//            case Line -> thing.line();
//            case Attribute.BillingCode -> thing.billingCode();
//        };
//    }
//    static Double get(Thing thing, NumField field) {
//        return switch (field) {
//            case Sales -> thing.sales();
//        };
//    }
//
//
////    static Function<Thing, ?> accessor(FieldName field) {
////        return switch (field) {
////            case Geo -> Thing::geo;
////            case Region -> Thing::region;
////            case BizUnit -> Thing::bizUnit;
////            case BillingCode -> Thing::billingCode;
////            case Line -> Thing::line;
////            case NotString -> Thing::notString;
////        };
////    }
//    /*
//    eq(foo, 1)
//      .and(eq(bar, 'baz'))
//      .and(lessThan(foo, 13))
//
//     */
//    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
//    @JsonSubTypes({
//        @JsonSubTypes.Type(value = Expr.Eq.class, name = "EQ"),
//        @JsonSubTypes.Type(value = Expr.Not.class, name = "NOT"),
//        @JsonSubTypes.Type(value = Expr.And.class, name = "AND"),
//        @JsonSubTypes.Type(value = Expr.Or.class, name = "OR")
//    })
//    sealed interface Expr {
//        record Const(boolean value) implements Expr{}
//        record Eq(Attribute field, String value) implements Expr {}
//        record Not(Expr expr) implements Expr {}
//        record And(Expr a, Expr b) implements Expr {}
//        record Or(Expr a, Expr b) implements Expr {}
//        record Above(NumField field, double amount) implements Expr {}
////        record NumEq(NumField field, double value) implements Expr {}
////        record LT(NumField field, double value) implements Expr {}
////        record GT(NumField field, double value) implements Expr {}
//
//        default Expr and(Expr other) {
//            return new And(this, other);
//        }
//        default Expr or(Expr other) {
//            return new Or(this, other);
//        }
//    }
//    static Expr.Eq eq(Attribute field, String value) {
//        return new Expr.Eq(field, value);
//    }
//    static Expr.And and(Expr a, Expr b) {
//        return new Expr.And(a, b);
//    }
//
//    static Expr.Or or(Expr a, Expr b) {
//        return new Expr.Or(a, b);
//    }
////    static Expr.LessThan lt(IntField field, Integer value) {
////        return new Expr.LessThan(field, value);
////    }
//
//    static Expr.Not Not(Expr expr) {
//        return new Expr.Not(expr);
//    }
//
//    public sealed interface Either<A,B> {
//        <C> Either<A,C> map(Function<B,C> f);
//        <C> C either(Function<A, C> fLeft, Function<B,C> fRight);
//
//        record Left<A,B>(A value) implements Either<A,B> {
//            public <C> Either<A,C> map(Function<B, C> f) {
//                return new Left<>(this.value());
//            }
//            public <C> C either(Function<A, C> fLeft, Function<B,C> fRight) {
//                return fLeft.apply(this.value);
//            }
//        }
//        record Right<A,B>(B value) implements Either<A,B> {
//            public <C> Either<A,C> map(Function<B, C> f) {
//                return new Right<>(f.apply(this.value));
//            }
//            public <C> C either(Function<A, C> fLeft, Function<B,C> fRight) {
//                return fRight.apply(this.value);
//            }
//        }
//    }
//
//    class V1 {
//        static Boolean interpret(Expr expr, Thing thing) {
//            return switch (expr) {
//                case Expr.Const(var value) -> value;
//                case Expr.Eq(var field, var value) -> get(thing, field).equals(value);
//                case Expr.Not(var subExpr) -> !interpret(subExpr, thing);
//                case Expr.And(var a, var b) -> interpret(a, thing) && interpret(b, thing);
//                case Expr.Or(var a, var b) -> interpret(a, thing) || interpret(b, thing);
//                case Expr.Above(var field, var value) -> get(thing, field) >= value;
//            };
//        }
//    }
//
//    record Why(Boolean b, String why){}
//
//
//
//    class V2 {
//        /**
//         * Abandoned Approach. We don't need the complexity this brings.
//         */
//        static Either<String, Why> interpret(Expr expr, Thing thing) {
//            return switch (expr) {
//                case Expr.Const(var value) -> null; // IGNORE. Just here to compile
//                case Expr.Eq(var field, var value) -> get(thing, field).equals(value)
//                    ? new Right<>(new Why(true, format("MATCHED[%s=%s]", field, value)))
//                    : new Left<>(format("FAILED[expected %s=%s but found %s=%s]", field, value, field, get(thing, field)));
//                case Expr.Not(var subExpr) -> interpret(subExpr, thing).map(x -> new Why(!x.b, "not(" + x.why + ")"));
//                case Expr.And(var a, var b) -> {
//                    Either<String, Why> resA = interpret(a, thing);
//                    Either<String, Why> resB = interpret(b, thing);
//                    if (resA instanceof Right<String, Why> r1 && resB instanceof Right<String, Why> r2) {
//                        yield new Right<>(new Why(true, format("MATCHED[%s AND %s]", r1.value().why, r2.value().why)));
//                    } else {
//                        yield new Left<>("("
//                                + resA.either(Object::toString, (x) ->x.why())
//                                + " AND " + resB.either(Object::toString, Object::toString) + ")");
//                    }
//                }
//                case Expr.Or(var a, var b) -> {
//                    yield interpret(a, thing);
////                    Either<String, Boolean> resA = interpret(a, thing);
////                    Either<String, Boolean> resB = interpret(b, thing);
////                    if (resA instanceof Right || resB instanceof Right) {
////                        yield new Right<>(true);
////                    } else {
////                        yield resA instanceof Left ? resA : resB;
////                    }
//                }
//                case Expr.Above(var field, var value) -> null; // IGNORE
//            };
//        }
//    }
//
//
//    record Bool(Boolean result, Either<String,String> what){}
//
//    enum Status {MATCHED, FAILED}
//    record Result(
//        Boolean matched,
//        String expected,
//        String found
//    ){
//
//        Result or(Result other) {
//            return new Result(this.matched() && other.matched,
//                    format("(%s OR %s)", this.expected, other.expected),
//                    format("(%s OR %s)", this.found, other.found));
//        }
//
//        Result and(Result other) {
//            return new Result(this.matched() && other.matched,
//                    format("(%s AND %s)", this.expected, other.expected),
//                    format("(%s AND %s)", this.found, other.found));
//        }
//
//        static Result not(Result result) {
//            return new Result(!result.matched(), format("not(%s)", result.expected), result.found();
//        }
//    }
//
//    class V3 {
//        static Result interpret2(Expr expr, Thing thing) {
//            return switch (expr) {
//                case Expr.Eq(var field, var value) -> {
//                    String found = get(thing, field);
//                    boolean result = found.equals(value);
//                    yield new Result(result, format("%s=%s", field, value), format("%s=%s", field, found));
//                }
//                case Expr.Not(var subExpr) ->
//                    Result.not(interpret2(subExpr, thing));
//                case Expr.And(var a, var b) ->
//                    interpret2(a, thing).and(interpret2(b, thing));
//                case Expr.Or(var a, var b) ->
//                    interpret2(a, thing).or(interpret2(b, thing));
//                case Expr.Const(var x) -> null;
//                case Expr.Above above -> null;
//            };
//        }
//
//
//        static Result interpret(Expr expr, Thing thing) {
//            return switch (expr) {
//                case Expr.Const(var value) -> new Result(value, "const("+value+")", "const("+value+")");
//                case Expr.Eq(var field, var value) -> {
//                    String found = get(thing, field);
//                    boolean result = found.equals(value);
//                    yield new Result(result, format("%s=%s", field, value), format("%s=%s", field, found));
//                }
//                case Expr.Not(var subExpr) -> {
//                    Result res = interpret(subExpr, thing);
//                    yield new Result(!res.matched(), format("not[%s]", res.expected), res.found());
//                }
//                case Expr.And(var a, var b) -> {
//                    Result resA = interpret(a, thing);
//                    Result resB = interpret(b, thing);
//                    yield new Result(resA.matched() && resB.matched(),
//                        format("(%s AND %s)", resA.expected, resB.expected),
//                        format("(%s AND %s)", resA.found, resB.found));
//                    }
//                case Expr.Or(var a, var b) -> {
//                    Result resA = interpret(a, thing);
//                    Result resB = interpret(b, thing);
//                    yield new Result(resA.matched() || resB.matched(),
//                        format("(%s OR %s)", resA.expected, resB.expected),
//                        format("(%s OR %s)", resA.found, resB.found));
//                }
//                case Expr.Above(var field, var value) -> {
//                    Double found = get(thing, field);
//                    boolean result = found >= value;
//                    yield new Result(result, format("%s>=%s", field, value), format("%s=%s", field, found));
//                }
//            };
//        }
//    }
//
//    class Callbacks {
//        // From Chapter 01
//        // This is data with an "interpreter!"
//        interface Decision {
//            record RetryImmediately() implements Decision {}
//            record Abandon() implements Decision {}
//            record RescheduleLater() implements Decision {}
//        }
//        // The lines between "just writing code" and "writing an interpreter"
//        // become really fuzzy. Well modeled data is great BECAUSE it has no opinion
//        // on what we do with it. We can write increasingly powerful layers on top
//        // of that data, all the while it just sits there, being data.
//
//    }
//
//    // We can make combinators!
//    static Expr any(Expr expr, Expr... exprs) {
//        return Stream.of(exprs).reduce(expr, Expr.Or::new);
//    }
//    static Expr all(Expr expr, Expr... exprs) {
//        return Stream.of(exprs).reduce(expr, Expr.And::new);
//    }
//    // and combinators of combinators!
//    Expr contains(Attribute field, String a, String... strings) {
//        return any(eq(field, a), Stream.of(strings).map(s -> eq(field, s)).toArray(Expr[]::new));
//    }
//
//    Expr any(Attribute field, String a, String... strings) {
//        return any(eq(field, a), Stream.of(strings).map(s -> eq(field, s)).toArray(Expr[]::new));
//    }
//
////    static String interpret(Expr expr) {
////        return switch (expr) {
////            case Expr.Eq(var string, var value) -> string + ":" + value;
////            case Expr.Not(var subExpr) -> "not:" + interpret(subExpr);
////            case Expr.And(var a, var b) -> "("+ interpret(a) + " AND " + interpret(b) + ")";
////            case Expr.Or(var a, var b) -> "("+ interpret(a) + " OR " + interpret(b) + ")";
////            case Expr.Contains(var field, var values) -> "SKIPPED";
////        };
////    }
////
////    Expr.Any contains(StrField field, String... strings) {
////        return new Expr.Any(Stream.of(strings).map(s -> eq(field, s)).toArray(Expr[]::new));
////    }
//
//    @Test
//    void asdfasdasd() {
//        Expr expr = eq(Geo, "bar")
//                .and(eq(BizUnit, "faz"));
////                .and(eq(BizUnit, "faz").or(Not(lt(NotString, 10))));
//        expr = Not(eq(Geo, "batz"))
////                .and(new Expr.Any(eq(Geo, "bar"),
////                                  eq(Geo, "baf"),
////                                  eq(Geo, "bat")))
//                .and(contains(Geo, "bar", "baf", "bat"));
//        Thing thing = new Thing(
//                "faz",
//                "region",
//                "batz",
//                "line",
//                "billingCode",
//                false,
//                10.0
//        );
//
//        System.out.println(expr);
//        System.out.println(V1.interpret(expr, thing));
//
//    }
//
//    @Test
//    void v2() {
//        Expr expr = eq(Geo, "bar")
//                .and(eq(BizUnit, "faz"));
////                .and(eq(BizUnit, "faz").or(Not(lt(NotString, 10))));
//        expr = Not(eq(Geo, "10"))
////                .and(new Expr.Any(eq(Geo, "bar"),
////                                  eq(Geo, "baf"),
////                                  eq(Geo, "bat")))
//                .and(contains(Geo, "bar", "baf", "bat"));
//        expr = and(Not(eq(Geo, "bat")), eq(BizUnit, "fab"));
//        Thing thing = new Thing(
//                "faz",
//                "region",
//                "batz",
//                "line",
//                "billingCode",
//                false,
//                100.0
//        );
//
//        System.out.println(expr);
//        System.out.println(V2.interpret(expr, thing));
//    }
//
//    @Test
//    void asdfasdfasdf() {
//        System.out.println((int)'a');
//        System.out.println((int)'z');
//        System.out.println((int)'A');
//        System.out.println((int)'Z');
//    }
//
//    static Random random = new Random();
//    static String randomStr() {
//        return String.valueOf(random.nextInt());
//    }
//
//    static String foooo(Integer x) {
//        return x.toString();
//    }
//
//    @Test
//    void v3() throws JsonProcessingException {
//        Expr expr = eq(Geo, "bar")
//                .and(eq(BizUnit, "faz"));
////                .and(eq(BizUnit, "faz").or(Not(lt(NotString, 10))));
//        expr = Not(eq(Geo, "10"))
////                .and(new Expr.Any(eq(Geo, "bar"),
////                                  eq(Geo, "baf"),
////                                  eq(Geo, "bat")))
//                .and(contains(Geo, "bar", "baf", "bat"));
//        expr = and(Not(eq(Geo, "bat")), eq(BizUnit, "fab"));
//
//        expr = and(Not(eq(Geo, "10")), eq(BizUnit, "faz"));
//        expr = eq(Geo, "foo").and(new Expr.Above(Sales, 10_000.00));
//
//
////        List<Expr> rules = IntStream.range(1, 100000).boxed().map(__ -> Not(eq(Geo, "10"))
////                .and(or(eq(BizUnit, "fazz"), eq(BizUnit, "baz")))
////                .and(new Expr.Above(Sales, 11.0))).toList();
//        Thing thing = new Thing(
//                "faz",
//                "region",
//                "batz",
//                "line",
//                "billingCode",
//                false,
//                10.0
//        );
//////        rules.stream().filter(r -> V3.interpret(r, thing).matched()).findFirst()
////        // Average: OptionalDouble[276.34] - single
////        List<Long> times = new ArrayList<>();
////        for (int i = 0; i < 100; i++) {
////            long s = System.currentTimeMillis();
////            List<Result> result = rules.stream().parallel().map(x -> V3.interpret(x, thing)).toList();
////            times.add(System.currentTimeMillis() - s);
////        }
////        System.out.println("Average: " + times.stream().mapToDouble(x -> x).average());
//
//        System.out.println(expr);
//        System.out.println(V3.interpret(expr, thing));
//
//        Function<Integer, String> f = Sketch::foooo;
//        Function<Integer,String> g = Sketch::foooo;
//
//        System.out.println(f.equals(g));
//        System.out.println(f.equals(f));
//
////        ObjectMapper mapper = new ObjectMapper();
////        expr = contains(Geo, "foo", "bar").and(Not(eq(Region, "whatever")));
////
////        String json = mapper.writeValueAsString(expr);
////        System.out.println(json);
////        Expr deserialized = mapper.readValue(json, Expr.class);
//    }
}
