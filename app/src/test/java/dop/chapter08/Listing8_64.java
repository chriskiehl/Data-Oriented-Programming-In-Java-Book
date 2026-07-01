package dop.chapter08;

import java.util.function.Function;

public class Listing8_64 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.64
   * ───────────────────────────────────────────────────────
   * Reminding Java that we’re all speaking the same language here
   * ───────────────────────────────────────────────────────
   */
  <A extends Comparable<A>> int compareTo(
      GreaterThan<A> gt, Account thing) {
//                     ▲
//                     └──── This gives java the needed type information
//                           to get us out of Comparator<?> and into
//                           Comparator<A> where everything is the same
    A found = gt.attr().getter().apply(thing);
    return found.compareTo(gt.value());
  }








  record Account() {}
  record Attr<A>(Attribute attribute, Function<Account, A> getter) {}
  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }
  record GreaterThan<A extends Comparable<A>>(Attr<A> attr, A value) {}

}
