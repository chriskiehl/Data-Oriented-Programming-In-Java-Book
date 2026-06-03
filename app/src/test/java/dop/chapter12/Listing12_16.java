package dop.chapter12;

import java.util.UUID;

import static java.lang.String.format;

public class Listing12_16 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 12.16
     * ───────────────────────────────────────────────────────
     * An example approach to name spacing resources
     * ───────────────────────────────────────────────────────
     */
    record Namespace(String owner, String suite, String testName){
//         ^ This is the data we use to disambuate test resources
        static String ROOT = "integ";
//                    ^ An arbitrary string that's at the root of the "tree." We can use it
//                      to clean up everything integration related
    }

    class Scope {
        public static String everything(Namespace ns) {                              //  ┐
            return Namespace.ROOT;                                                   //  │
        }                                                                            //  │
        public static String ownedBy(Namespace ns) {                                 //  │
            return String.join("-", Namespace.ROOT, ns.owner);                       //  │
        }                                                                            //  │◄── Resources can be cleaned up
        public static String thisSuite(Namespace ns) {                               //  │    at different grains by simple
            return String.join("-", Namespace.ROOT, ns.owner, ns.suite);             //  │    string matching
        }                                                                            //  │
        public static String thisTest(Namespace ns) {                                //  │
            return String.join("-", Namespace.ROOT, ns.owner, ns.suite, ns.testName);
        }
    }

    String randomName(Namespace namespace) {
        // Every resource gets created in a custom namespace.
        return format(
            "%s-%s",
            Scope.thisTest(namespace),
            UUID.randomUUID().toString().substring(0, 8)
        );
    }
}

