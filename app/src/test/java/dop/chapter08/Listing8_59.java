package dop.chapter08;

import java.util.Arrays;
import java.util.stream.Stream;

public class Listing8_59 {

  /**
   * ───────────────────────────────────────────────────────
   * Listing 8.59
   * ───────────────────────────────────────────────────────
   * Traversing to generating nodes and edges
   * ───────────────────────────────────────────────────────
   */
  @SafeVarargs
  static <A> Stream<A> concat(Stream<A>...ys) {
    return Arrays.stream(ys).reduce(Stream.of(), Stream::concat);
  }
// ▲
// └──── Helper for combining an arbitrary number of streams

  static Stream<String> nodesAndEdges(Rule rule) {
//                       ▲
//                       └──── Traverses over the tree of Rules and generates
//                             a stream of Nodes and Edges.
    return switch (rule) {
      case Rule.Equals e -> Stream.of(node(e));
      case Rule.Not not -> concat(
          Stream.of(node(not)),
          Stream.of(edge(not, not.a)),
          nodesAndEdges(not.a()));
      case Rule.Or or -> concat(
          Stream.of(node(or)),
          Stream.of(edge(or, or.a)),
          Stream.of(edge(or, or.b)),
          nodesAndEdges(or.a()),
          nodesAndEdges(or.b()));
      case Rule.And and -> concat(
          Stream.of(node(and)),
          Stream.of(edge(and, and.a)),
          Stream.of(edge(and, and.b)),
          nodesAndEdges(and.a()),
          nodesAndEdges(and.b()));
    };
  }

  static String toGraphViz(Rule rule) {
//              ▲
//              └──── Finally, we stitch it all together and generate the
//                    contents of the GraphViz file
    return String.format("""
        digraph Rule {
            rankdir=TD;
            %s
        }""", String.join("\n\t", nodesAndEdges(rule).toList()));
  }








  enum Attribute { REGION, COUNTRY, SECTOR, SEGMENT, CHANNEL }

  sealed interface Rule {
    record Equals(Attribute field, String value) implements Rule {}
    record And(Rule a, Rule b) implements Rule {}
    record Or(Rule a, Rule b) implements Rule {}
    record Not(Rule a) implements Rule {}
  }

  static String node(Rule rule) {
    return "";
  }

  static String edge(Rule from, Rule to) {
    return "";
  }

}
