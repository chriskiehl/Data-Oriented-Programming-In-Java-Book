package dop.chapter06;

import java.util.List;
import java.util.stream.Stream;

public class Listing6_2 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 6.2
     * ───────────────────────────────────────────────────────
     * Mathematical functions deterministically assign inputs to outputs
     * ───────────────────────────────────────────────────────
     */
    public int increment(int x) {            //  ┐
        return x + 1;                        //  │◄── These are completely deterministic. The same input
    }                                        //  │    will return the same output
    public double square(double x) {         //  │
        return x * x;                        //  │
    }                                        //  ┘

    enum Status {ERROR, WARNING, INFO}
    enum Color {RED, YELLOW, GREEN}
    public Color color(Status status) {      //  ┐
        return switch(status) {              //  │
            case ERROR -> Color.RED;         //  │
            case WARNING -> Color.YELLOW;    //  │◄── But mathematical functions don’t mean “doing math.”
            case INFO -> Color.GREEN;        //  │    They’re any function that deterministically map
        };                                   //  │    inputs to outputs.
    }

    public <A> List<A> concat(List<A> xs, List<A> ys) {          //  ┐
        return Stream.concat(xs.stream(), ys.stream()).toList(); //  │ Completely deterministic!
    }                                                            //  ┘
}
