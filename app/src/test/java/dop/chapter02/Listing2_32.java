package dop.chapter02;

public class Listing2_32 {
  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.32
   * ───────────────────────────────────────────────────────
   * Recreating the Vector type as a record
   * ───────────────────────────────────────────────────────
   */
  record Vector(double x, double y){}    // ◄── Recreating our Vector implementation from listing 2.7, but now using a record

  void example() {
        // ┌───── Like good value objects, what they are is determined by their equality
        // ▼
    final Vector a = new Vector(2, 3);
    final Vector b = new Vector(2, 3);
    final Vector c = new Vector(2, 3);

    assert a.equals(b)
        && b.equals(a)
        && a.equals(c);
        // ▲
        // └───── Any value can be substituted for any other value with no observable change in behavior
  }

}
