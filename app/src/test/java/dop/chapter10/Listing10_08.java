package dop.chapter10;

import java.util.List;
import java.util.Map;

public class Listing10_08 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.8
     * ───────────────────────────────────────────────────────
     * with the right abstractions, ingestion can be a simple for-loop
     * ───────────────────────────────────────────────────────
     */
    interface BulkLoader {                               //  ┐
        void load(UpdatesAndInserts records);            //  │◄── These interfaces abstract the core behaviors
    }                                                    //  │
    interface Transformer {                              //  │
        UpdatesAndInserts transform(CloudObject object); //  ┘
    }

    record UpdatesAndInserts(  //  ┐
        List<Insert> inserts,  //  │◄── Even OO designs need helpful data types!
        List<Update> updates   //  ┘
    ){}

    // something like...
    public Response ingest(Request request) {
        Map<String,String> manifest = JSON.parse(
            request.getBody(), Map.class);

//     ┌───────────────────────────────────────┐
        for (String key : manifest.values()) {
//     └───────────────────────────────────────┘
//                    ▲
//                    └──── The abstractions reduce the implementation down to a for-loop
//                          over strategies
            var fileObject = cloudStore.getObject(key);
            var records = transformer.transform(fileObject);
            bulkLoader.load(records);
        }
        return Response.OK();
    }














    CloudStore cloudStore;
    Transformer transformer;
    BulkLoader bulkLoader;

    record Insert(String table, TableRecord record){}
    record Update(String table, TableRecord record){}
    record TableRecord(){}

    interface CloudStore {
        CloudObject getObject(String key);
    }
    interface CloudObject {}
    interface Request {
        String getBody();
    }
    static class Response {
        static Response OK() {
            return new Response();
        }
    }
    static class JSON {
        static <A> A parse(String raw, Class<A> type) {
            return null;
        }
    }
}
