package dop.chapter10;

import static java.lang.String.format;

public class Listing10_7 {
    /**
     * ───────────────────────────────────────────────────────
     * Listing 10.7
     * ───────────────────────────────────────────────────────
     * A small example of this library in action
     * ───────────────────────────────────────────────────────
     */
    class Example {
//        ┌─── We can annotate any method with an HTTP method
//        ▼
        @post("/say-hello")
        public Response helloWorld(Request request) {
//                                    ▲
//                                    └──── Every handler can accept a Request object
            String raw = request.body();
//                  ▲
//                  └──── Request has a body that's just some raw text blob
            return Response.OK(format("We received, %s!", raw));
//                                ▲
//                                └──── We can return any arbitrary object as a Response.
        }
    }














    @interface post {
        String value();
    }

    interface Request {
        String body();
    }

    static class Response {
        static Response OK(Object value) {
            return new Response();
        }
    }
}
