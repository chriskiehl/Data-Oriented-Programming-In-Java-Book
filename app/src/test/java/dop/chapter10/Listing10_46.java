package dop.chapter10;

public class Listing10_46 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.46
     * ───────────────────────────────────────────────────────
     * A[EH12.1] well scoped object is forced into existence
     * ───────────────────────────────────────────────────────
     */
    class MyRestAPI {
        // **We** are the “gross outside world” that our REST API uses!
        private Ingestor ingestor;

//
//       ┌──── Out here, it knows that HTTP exists and how routing works
//       ▼
        @post("ingest")
//            ┌────────┐      ┌───────┐
        public Response ingest(Request request) {
//            └────────┘      └───────┘
//                ▲                ▲
//                └─────────────   └──── It speaks in Requests and Responses
            Manifest manifest = JSON.parse(request.body(), Manifest.class);
//                               ▲
//                               └──── It deals with JSON and deserialization.
            Ingestor.ingest(manifest);
//                   ▲
//                   └──── And it hides those details from the core program!
//                         It hands it well modeled data in the shape it demands

            return __;
        }
    }
















    static Response __;














    @interface post {
        String value();
    }
    interface Request {
        String body();
    }
    static class Response {}
    record Manifest(){}
    interface Ingestor {
        static void ingest(Manifest manifest) {}
    }
    static class JSON {
        static <A> A parse(String raw, Class<A> type) {
            return null;
        }
    }
}
