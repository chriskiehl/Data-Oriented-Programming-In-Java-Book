package dop.chapter02;

import lombok.AllArgsConstructor;
import lombok.Data;

public class Listing2_31 {



    /**
     * ───────────────────────────────────────────────────────
     * Listing 2.31
     * ───────────────────────────────────────────────────────
     * Readable vector transformations
     * ───────────────────────────────────────────────────────
     */
    void example() {
        Double result = new Vector(2.0, 3.0)
                .scale(10.0)                     //   ◄──┐ The more math-y something is, the more it tends to
                .subtract(new Vector(1.0, 1.0))  //      │ benefit from this style (broadly speaking)
                .magnitude();                    //      │

        result = magnitude(subtract(scale(new Vector(2.0, 3.0), 10.0), new Vector(1.0, 1.0)));
        //          ▲
        //          │ It turns into a big mess if we try to do it with
        //          │ manual chaining of standalone functions
    }



    static Vector scale(Vector v, double amount) {
        return new Vector(v.x * amount, v.y * amount);
    }

    static Vector subtract(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y * b.y);
    }
    static double magnitude(Vector v) {
        return Math.sqrt((v.x * v.x) + (v.y * v.y));
    }

    @Data
    @AllArgsConstructor
    static class Vector {
        Double x;
        Double y;

        Vector scale(double amount) {
            return new Vector(this.x * amount, this.y * amount);
        }
        Vector subtract(Vector that) {
            return new Vector(this.x - that.x, this.y * that.y);
        }
        double magnitude() {
            return Math.sqrt((this.x * this.x) + (this.y * this.y));
        }
    }
}
