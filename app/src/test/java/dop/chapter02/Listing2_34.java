package dop.chapter02;

public class Listing2_34 {
  /**
   * ───────────────────────────────────────────────────────
   * Listing 2.34
   * ───────────────────────────────────────────────────────
   * Transforming arguments during construction
   * ───────────────────────────────────────────────────────
   */
  record Rational(int num, int denom) {
    Rational {
      if (denom == 0) {
        throw new IllegalArgumentException(
            "Hey! That’s not how math works!"
        );
      }
      int gcd = gcd(num, denom);
      num /= gcd;
      denom /= gcd;
      //    ▲
      //    └───── While we’re inside the constructor, we can transform
      //           the incoming argument as much as we want.
      //           Here we’re normalizing them before assignment.
    }
  }




  private static int gcd(int a, int b) {
    a = Math.abs(a);
    b = Math.abs(b);

    while (b != 0) {
      int tmp = b;
      b = a % b;
      a = tmp;
    }

    return a;
  }

}
