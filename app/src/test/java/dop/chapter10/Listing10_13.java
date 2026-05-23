package dop.chapter10;

import java.util.Map;
import java.util.Map.Entry;

public class Listing10_13 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.13
     * ───────────────────────────────────────────────────────
     * Plugging in our factory class
     * ───────────────────────────────────────────────────────
     */
    IngestionFactory factory;
    CloudStore cloudStore;

    public Response ingest(Request request) {
        // As before we get the Manifest info out of the request object
        Map<String, String> manifest = JSON.parse(request.getBody(), Map.class);
        // (and do validation here)
        for (Entry<String, String> entry : manifest.entrySet()) {
//                                                     ▲
//                                                     └──── But now we loop over its keys and values.
            String type = entry.getKey();
//                                ▲
//                                └──── The key name is how our factory loads the correct implementation

            var fileObject = cloudStore.getObject(entry.getValue());
            // Grabbing the appropriate Transformer implementation
            Transformer transformer = factory.getTransformer(type);
            UpdatesAndInserts records = transformer.transform(fileObject);
            // grabbing the appropriate BulkLoader implementation
            BulkLoader bulkLoader = factory.getLoader(type);
            bulkLoader.load(records);
        }


        return __;
    }














    interface IngestionFactory {
        Transformer getTransformer(String type);
        BulkLoader getLoader(String type);
    }
    interface CloudStore {
        CloudObject getObject(String key);
    }
    interface CloudObject {}
    interface Transformer {
        UpdatesAndInserts transform(CloudObject object);
    }
    interface BulkLoader {
        void load(UpdatesAndInserts records);
    }
    record UpdatesAndInserts(){}
    interface Request {
        String getBody();
    }
    static class Response {}
    static class JSON {
        static <A> A parse(String raw, Class<A> type) {
            return null;
        }
    }

    static Response __;
}
