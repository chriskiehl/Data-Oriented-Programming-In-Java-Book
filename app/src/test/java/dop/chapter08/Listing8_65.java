package dop.chapter08;

import java.util.function.Function;

import static java.lang.String.format;

public class Listing8_65 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.65
   * ───────────────────────────────────────────────────────
   * Adding support for GreaterThan and LessThan
   * ───────────────────────────────────────────────────────
   */
  static Result eval(Rule rule, Account account) {
    return switch (rule) {
      case Rule.Equals(var field, var value) -> __;
      case Rule.GreaterThan<?> gt -> {
        Comparable<?> found = get(account, gt.attr());  //  ┐
        Comparable<?> expected = gt.value();            //  ┘◄── Out here, we still see them as
                                                        //       wildcards, which is OK, because
                                                        //       we’ll only call toString on them
        boolean result = compareTo(gt, account) > 0;
//                     ▲
//                     └──── Inside of this function call is where
//                           they’ll be “seen” as types which are all
//                           on the same plane
        yield new Result(result,
            format("%s>%s", gt.attr().attribute(), expected),
            format("%s=%s", gt.attr().attribute(), found));
      }
    };
  }








  record Account() {}
  record Result(Boolean matched, String expected, String found) {}
  record Attr<A>(Attribute attribute, Function<Account, A> getter) {}
  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }

  static Result __;

  sealed interface Rule {
    record Equals<A>(Attr<A> field, A value) implements Rule {}
    record GreaterThan<A extends Comparable<A>>(Attr<A> attr, A value) implements Rule {}
  }

  static Comparable<?> get(Account account, Attr<?> attr) {
      return null;
  }

  static <A extends Comparable<A>> int compareTo(Rule.GreaterThan<A> gt, Account thing) {
      A found = gt.attr().getter().apply(thing);
      return found.compareTo(gt.value());
  }

}
