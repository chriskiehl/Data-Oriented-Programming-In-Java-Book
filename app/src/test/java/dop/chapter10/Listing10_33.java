package dop.chapter10;

import java.util.List;
import java.util.function.Function;

public class Listing10_33 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.33
     * ───────────────────────────────────────────────────────
     * A better design?
     * ───────────────────────────────────────────────────────
     */
    class FileRepository {
        <A> List<A> load(                 //  ┐
            ResourceName name,            //  │◄── Parameterizing the method opens it
            Function<String[], A> parser  //  │    to extension. We need only pass in
        ){return __;};                    //  │    a strategy for how we parse the
    }                                     //  ┘    underlying CSV file.














    record ResourceName(String value) {}
    static List __;
}
