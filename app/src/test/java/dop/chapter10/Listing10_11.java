package dop.chapter10;

public class Listing10_11 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.11
     * ───────────────────────────────────────────────────────
     * How do we get from an abstract interface to a concrete class?
     * ───────────────────────────────────────────────────────
     */

    void example() {
        for (String key : manifest.values()) {
            var fileObject = cloud.getObject(bucket, key);
            // with abstraction comes the problem of dispatch.
            // How do we get the *right* transformer and loader
            // implementations for these?
            var records = transformer.transform(fileObject.read());
            loader.load(records);
            /*...*/
        }
    }














    Manifest manifest;
    Cloud cloud;
    String bucket;
    Transformer transformer;
    Loader loader;

    interface Manifest {
        Iterable<String> values();
    }
    interface Cloud {
        CloudObject getObject(String bucket, String key);
    }
    interface CloudObject {
        Object read();
    }
    interface Transformer {
        Object transform(Object object);
    }
    interface Loader {
        void load(Object records);
    }
}
