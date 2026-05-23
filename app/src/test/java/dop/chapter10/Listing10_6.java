package dop.chapter10;

import java.io.InputStream;

public class Listing10_6 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.6
     * ───────────────────────────────────────────────────────
     * An example of the ObjectStore in action
     * ───────────────────────────────────────────────────────
     */
    class Example {
        CloudStore cloudStore;

        public void example() {
            CloudObject object = cloudStore.getObject(
                "083d757f-74b0-4f46-816d-7ca6e9e8750d.csv"
            );
            InputStream stream = object.read();
            // do something with the stream here.
        }
    }














    interface CloudStore {
        CloudObject getObject(String key);
    }

    interface CloudObject {
        InputStream read();
    }
}
