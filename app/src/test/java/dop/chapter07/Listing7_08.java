package dop.chapter07;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Listing7_08 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 7.8
   * ───────────────────────────────────────────────────────
   * Adding two lists as a binary operation
   * ───────────────────────────────────────────────────────
   */
  class Example1 {
    static <T> List<T> add(List<T> list1, List<T> list2) {
      return Stream.concat(list1.stream(), list2.stream()).toList(); // ◄── We might implement it like this
    }
  }

  class Example2 {
    // or like this! or some other way! There are no
    // rules here as long as it’s a deterministic function
    static <T> List<T> add(List<T> list1, List<T> list2) {
      List<T> output = new ArrayList(list1);
      output.addAll(list2);
    //           ▲
    //           └──── Does it matter that we use mutation internally while writing
    //                 a deterministic function that takes and returns immutable
    //                 data? I’d argue no! Don’t let purity get in the way of
    //                 practicality.
      return List.copyOf(output);
    }
  }

}
