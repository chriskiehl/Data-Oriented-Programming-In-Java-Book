package dop.chapter08;

import java.util.function.Function;

public class Listing8_63 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.63
   * ───────────────────────────────────────────────────────
   * The problem: Java can’t tell these are the same kind of Comaparable
   * ───────────────────────────────────────────────────────
   */
  static Result eval(Rule rule, Account account) {
    return switch (rule) {
      case Rule.Equals(var field, var value) -> __;
      case Rule.GreaterThan<?> gt -> {
        Comparable<?> found = gt.attr().getter().apply(account);
//      Unlike our equals implementation which could “see”
//      an object, Java “sees” two Comparable instances
//      of possibly any type
        Comparable<?> expected = gt.value();
//      ┌──────────────────!ERROR!─────────────────────┐
//      boolean result = found.compareTo(expected) > 0;
//      └──────────────────────────────────────────────┘
//                                 ▲
//                                 └──── It can’t tell that they’re the
//                                       same, so we get a compile error if
//                                       we try to compare them

        yield __;
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

}
