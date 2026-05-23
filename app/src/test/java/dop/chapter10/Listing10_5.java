package dop.chapter10;

import java.io.InputStream;

public class Listing10_5 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.5
     * ───────────────────────────────────────────────────────
     * Interfaces for the ObjectStore
     * ───────────────────────────────────────────────────────
     */
    interface CloudStore {
        CloudObject getObject(
            String key
//                 ▲
//                 └──── The key tells us where to find the particular object
//                       in the team’s object store
        );
    }

    interface CloudObject {
        InputStream read();
//                  ▲
//                  └──── Reading from this remote file yields a stream[EH3.1]
//                        of bytes.
    }
}
