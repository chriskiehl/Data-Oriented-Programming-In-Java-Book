package dop.chapter07;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Listing7_23 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 7.23
     * ───────────────────────────────────────────────────────
     * Algebraic operations can sometimes be governed by special laws
     * ───────────────────────────────────────────────────────
     */
    void example() {
        double x = 0.02;
        double y = 0.02;

        assertEquals(                                      //  ┐
            x <= y,                                        //  │◄── Sometimes we want an algebraic operation to
            someUnaryOperation(x) <= someUnaryOperation(y) //  ┘    maintain some aspect of the input values. For
        );                                                  //       instance, if the two inputs are sorted before we
//                                                        apply the operation, we might want their output to
//                                                        "keep" that same ordering
    }














    static double someBinaryOperation(double x, double y) {
        return x;
    }

    static double someUnaryOperation(double x) {
        return x;
    }
}
